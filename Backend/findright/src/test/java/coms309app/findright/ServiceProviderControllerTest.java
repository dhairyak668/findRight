package coms309app.findright;

import coms309app.findright.Reviews.Review;
import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.ServiceProvider.ServiceProviderRepository;
import coms309app.findright.ServiceRequest.ServiceRequest;
import coms309app.findright.ServiceRequest.ServiceRequestRepository;
import coms309app.findright.User.User;
import coms309app.findright.User.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ServiceProviderControllerTest {

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Before
    public void setUp() {
        // Set the base URI to localhost with port 8080
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testGetAllUsers() {
        // Send a GET request
        Response response = when().get("/service-providers/getallserviceproviders")
                .then().extract().response();

        // Check that the response is successful (HTTP status code 200)
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testCreateServiceProvider() {
        // Create a new service provider object
        ServiceProvider newServiceProvider = new ServiceProvider(
                "Test Provider",
                "123-456-7890",
                "testprovider@example.com",
                "testpassword",
                "Test Address",
                "12345",
                new ArrayList<>(Arrays.asList("Service1", "Service2"))
        );

        // Send a POST request to create the service provider
        Response createServiceProviderResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newServiceProvider)
                .when()
                .post("/service-providers/createserviceprovider");

        // Check the response status code
        int statusCode = createServiceProviderResponse.getStatusCode();
        assertEquals(200, statusCode);


    }





}
