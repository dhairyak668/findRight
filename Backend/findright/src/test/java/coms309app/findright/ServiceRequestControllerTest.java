package coms309app.findright;

import coms309app.findright.Reviews.Review;
import coms309app.findright.ServiceRequest.ServiceRequest;
import coms309app.findright.ServiceRequest.ServiceRequestRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ServiceRequestControllerTest {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Before
    public void setUp() {
        // Set the base URI to localhost with port 8080
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testGetAllUsers() {
        // Send a GET request
        Response response = when().get("/service-requests/all")
                .then().extract().response();

        // Check that the response is successful (HTTP status code 200)
        assertEquals(200, response.getStatusCode());


    }

    @Test
    public void testCreateServiceRequest() {
        // Create a new service request object
        ServiceRequest newServiceRequest = new ServiceRequest();
        newServiceRequest.setRequesterName("John Doe");
        newServiceRequest.setRequesterEmail("johndoe@example.com");
        newServiceRequest.setRequesterPhoneNumber("1254507890");
        newServiceRequest.setAddress("123 Main Street");
        newServiceRequest.setZipCode("12345");
        newServiceRequest.setDescription("I need help with a plumbing issue");
        newServiceRequest.setServiceTypeNeeded("Plumbing");

        // Send a POST request to create the service request
        Response createServiceRequestResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newServiceRequest)
                .when()
                .post("/service-requests/create");

        // Check the response status code
        int statusCode = createServiceRequestResponse.getStatusCode();
        assertEquals(200, statusCode);


    }

    @Test
    public void testGetServiceRequestsByZipCode() {
        // Given a zip code (you might want to pre-populate the database with test data)
        String zipCode = "12345";

        // When sending a GET request to the endpoint
        Response response = when().get("/service-requests/by-zipcode/" + zipCode)
                .then().extract().response();

        // Then the response should be successful and the data should be correct
        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void testGetServiceRequestsByEmail() {
        // Given an email (you might want to pre-populate the database with test data)
        String email = "johndoe@example.com";

        // When sending a GET request to the endpoint
        Response response = when().get("/service-requests/by-email/" + email)
                .then().extract().response();

        // Then the response should be successful and the data should be correct
        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void testGetServiceRequestDetailsById() {
        // Given a service request ID (ensure this ID is present in your test database)
        Long requestId = 1L;

        // When sending a GET request to the endpoint
        Response response = when().get("/service-requests/" + requestId)
                .then().extract().response();

        // Then the response should be successful and the data should be correct
        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void testGetServiceRequestsByZipCodeAndServiceType() {
        // Given a zip code and a service type (ensure these are present in your test database)
        String zipCode = "12345";
        String serviceType = "Plumbing";

        // When sending a GET request to the endpoint
        Response response = given().param("zipCode", zipCode)
                .param("serviceType", serviceType)
                .when().get("/service-requests/by-zip-and-service-type")
                .then().extract().response();

        // Then the response should be successful and the data should be correct
        assertEquals(200, response.getStatusCode());

    }



    @Test
    public void testGetServiceRequestsByStage() {
        // Given a stage (ensure this stage is present in your test database)
        int stage = 1; // Example stage

        // When sending a GET request to the endpoint
        Response response = given()
                .when().get("/service-requests/by-stage/" + stage)
                .then().extract().response();

        // Then the response should be successful
        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void testGetServiceRequestsForUserByStage() {
        // Given a user ID and a stage (ensure the user and stage are present in your test database)
        Long userId = 123L; // Example user ID
        int stage = 1; // Example stage

        // When sending a GET request to the endpoint
        Response response = given()
                .when().get("/service-requests/by-user-and-stage/" + userId + "/" + stage)
                .then().extract().response();

        // Then the response should be successful
        assertEquals(200, response.getStatusCode());

    }
    @Test
    public void testGetServiceRequestsForServiceProviderByStage() {
        // Given a service provider ID and a stage (ensure the provider and stage are present in your test database)
        Long providerId = 456L; // Example provider ID
        int stage = 1; // Example stage

        // When sending a GET request to the endpoint
        Response response = given()
                .when().get("/service-requests/by-service-provider-and-stage/" + providerId + "/" + stage)
                .then().extract().response();


        assertEquals(200, response.getStatusCode());

    }


}
