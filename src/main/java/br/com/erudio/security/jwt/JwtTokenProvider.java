package br.com.erudio.security.jwt;

import br.com.erudio.data.vo.v1.TokenVO;
import br.com.erudio.execeptions.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;


public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMillisecounds = 3600000;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    public String getSecretKey() {
        return secretKey;
    }

    public long getValidityInMillisecounds() {
        return validityInMillisecounds;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenVO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validaty = new Date(now.getTime() + validityInMillisecounds);
        var accessToken = getAcessToken(username, roles, now, validaty);
        var refreshToken = getRefreshToken(username, roles, now);
        return new TokenVO(username, true, now, validaty, accessToken, refreshToken);
    }

    private String getAcessToken(String username, List<String> roles, Date now, Date validaty) {
        String issuerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validaty)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date validatyRefreshToken = new Date(now.getTime() + (validityInMillisecounds * 3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);

        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }
}
