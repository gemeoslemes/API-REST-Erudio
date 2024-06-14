package br.com.erudio.integrationtests.controllers.withjson;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationtests.AbstraticIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.BookVO;
import br.com.erudio.integrationtests.vo.PersonVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstraticIntegrationTest {

    private static RequestSpecification specification;

    private static ObjectMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("Victor", "12345678");

        var accessToken = given()
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
                .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/books/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getKey());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getLaunchDate());

        assertTrue(persistedBook.getKey() > 0);


        assertEquals("Clean code", persistedBook.getTitle());
        assertEquals("Robert Cecil Martin", persistedBook.getAuthor());
        assertEquals(80.0, persistedBook.getPrice());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        book.setTitle("Código Limpo");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(book)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getKey());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getLaunchDate());

        assertEquals(book.getKey(), persistedBook.getKey());

        assertEquals("Código Limpo", persistedBook.getTitle());
        assertEquals("Robert Cecil Martin", persistedBook.getAuthor());
        assertEquals(80.0, persistedBook.getPrice());
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", book.getKey())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getKey());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getLaunchDate());

        assertEquals(book.getKey(), persistedBook.getKey());

        assertEquals("Código Limpo", persistedBook.getTitle());
        assertEquals("Robert Cecil Martin", persistedBook.getAuthor());
        assertEquals(80.0, persistedBook.getPrice());
    }

    @Test
    @Order(4)
    public void testDelete() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", book.getKey())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonProcessingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                //.as(new TypeRef<List<PersonVO>>() {});
                .asString();

        List<BookVO> persistedBook = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {});
        BookVO foundBookOne = persistedBook.get(1);
        assertEquals(2,foundBookOne.getKey());

        assertNotNull(foundBookOne);
        assertNotNull(foundBookOne.getKey());
        assertNotNull(foundBookOne.getPrice());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getLaunchDate());


        assertEquals(45.00, foundBookOne.getPrice());
        assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm", foundBookOne.getAuthor());
        assertEquals("Design Patterns", foundBookOne.getTitle());

        BookVO foundBookSeven = persistedBook.get(3);
        assertEquals(4, foundBookSeven.getKey());

        assertNotNull(foundBookSeven);
        assertNotNull(foundBookSeven.getKey());
        assertNotNull(foundBookSeven.getPrice());
        assertNotNull(foundBookSeven.getAuthor());
        assertNotNull(foundBookSeven.getTitle());
        assertNotNull(foundBookSeven.getLaunchDate());

        assertEquals("Crockford", foundBookSeven.getAuthor());
        assertEquals("JavaScript", foundBookSeven.getTitle());
        assertEquals(67.00, foundBookSeven.getPrice());
    }

    @Test
    @Order(6)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("api/books/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockBook() {
        book.setAuthor("Robert Cecil Martin");
        book.setPrice(80.0);
        book.setTitle("Clean code");
        book.setLaunchDate(new Date());
    }
}
