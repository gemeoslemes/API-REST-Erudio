package br.com.erudio.integrationtests.controllers.withjson;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationtests.AbstraticIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstraticIntegrationTest {

    private static TokenVO tokenVO;

    @Test
    @Order(0)
    public void testSegnin() {
        AccountCredentialsVO user = new AccountCredentialsVO("Victor", "12345678");

        tokenVO = given()
                .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                    .when()
                .post()
                    .then()
                        .statusCode(200)
                            .extract()
                            .body()
                                .as(TokenVO.class);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(1)
    public void testRefreshToken() {
        AccountCredentialsVO user = new AccountCredentialsVO("Victor", "12345678");

       var newTokenVO = given()
               .basePath("/auth/refresh")
               .port(TestConfigs.SERVER_PORT)
               .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParam("username", tokenVO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
               .when()
                    .put("{username}")
               .then()
                    .statusCode(200)
               .extract()
                    .body()
                    .as(TokenVO.class);

       assertNotNull(newTokenVO.getAccessToken());
       assertNotNull(newTokenVO.getRefreshToken());
    }
}
