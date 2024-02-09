package coms309app.findright;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WelcomeControllerTest {

    @Before
    public void setUp() {
        // Set the base URI to localhost with port 8080 (or the port your Spring Boot app is running on)
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testIndexEndpoint() {
        // Send a GET request to the root ("/") endpoint and assert the expected response
        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(equalTo("FindRight"));
    }
}


