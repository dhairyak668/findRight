package coms309app.findright;

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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {


    @Autowired
    private UserRepository userRepository;


    @Before
    public void setUp() {
        // Set the base URI to localhost with port 8080
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testGetAllUsers() {
        // Send a GET request to /users/all
        Response response = when().get("/users/all")
                .then().extract().response();

        // Check that the response is successful (HTTP status code 200)
        assertEquals(200, response.getStatusCode());


    }


    @Test
    public void testCreateUser() {
        // Create a new user object
        User newUser = new User(
                "Test User",
                "testuser@example.com",
                "1234567890",
                "testpassword",
                "Test Address",
                "12345"
        );

        // Send a POST request to create the user
        Response createUserResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/users/post");

        int statusCode = createUserResponse.getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    public void testGetUserById() {
        // Create a new user object
        User newUser = new User(
                "John Doe",
                "johndoe@example.com",
                "9876543210",
                "strongpassword",
                "123 Main Street",
                "54321"
        );

        // Send a POST request to create the user and capture the created user's ID
        Response createUserResponse = given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/users/post");

        int statusCode = createUserResponse.getStatusCode();
        assertEquals(HttpStatus.OK.value(), statusCode);

        // Extract the user ID from the response
        Long userId = createUserResponse.jsonPath().getLong("id");
        assertNotNull(userId);

        // Send a GET request to retrieve the user by the valid ID
        Response getUserResponse = when()
                .get("/users/id/" + userId) // Use the valid ID of the user created in this test
                .then()
                .extract().response();

        // Check that the response status code is 200 when the user is found
        assertEquals(HttpStatus.OK.value(), getUserResponse.getStatusCode());
    }

    @Test
    public void testGetUserByEmail() {
        // Create a new user object with different details
        User newUser = new User(
                "Alice Johnson",
                "alice@example.com",
                "5551234567",
                "securepassword",
                "456 Oak Avenue",
                "67890"
        );

        // Send a POST request to create the user
        Response createUserResponse = given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/users/post");

        int statusCode = createUserResponse.getStatusCode();
        assertEquals(HttpStatus.OK.value(), statusCode);

        // Send a GET request to retrieve the user by email
        Response getUserResponse = when()
                .get("/users/email/alice@example.com") // Use the user's email for retrieval
                .then()
                .extract().response();

        // Check that the response status code is 200 when the user is found
        assertEquals(HttpStatus.OK.value(), getUserResponse.getStatusCode());

        // Extract user details from the response and verify them
        String name = getUserResponse.jsonPath().getString("name");
        String email = getUserResponse.jsonPath().getString("email");
        String phone = getUserResponse.jsonPath().getString("phone");
        String address = getUserResponse.jsonPath().getString("address");
        String zipcode = getUserResponse.jsonPath().getString("zipcode");

        assertEquals(newUser.getName(), name);
        assertEquals(newUser.getEmail(), email);
        assertEquals(newUser.getPhone(), phone);
        assertEquals(newUser.getAddress(), address);
        assertEquals(newUser.getZipcode(), zipcode);
    }

    @Test
    public void testUpdateUserById() {
        // Create a new user object with unique email and password
        User newUser = new User(
                "John Doe",
                "uniquejohndoe@example.com", // Unique email
                "9876543210",
                "uniquestrongpassword", // Unique password
                "123 Main Street",
                "54321"
        );

        // Send a POST request to create the user
        Response createUserResponse = given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/users/post");

        int statusCode = createUserResponse.getStatusCode();
        assertEquals(HttpStatus.OK.value(), statusCode);

        // Extract the user ID from the response
        Long userId = createUserResponse.jsonPath().getLong("id");
        assertNotNull(userId);

        // Create an updated user object with new unique email and password
        User updatedUser = new User(
                "Updated John Doe",
                "updateduniquejohndoe@example.com", // New unique email
                "5555555555",
                "newuniquestrongpassword", // New unique password
                "456 Elm Street",
                "67890"
        );

        // Send a PUT request to update the user by ID
        Response updateUserResponse = given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when()
                .put("/users/update/" + userId); // Use the user's ID for update

        int updateStatusCode = updateUserResponse.getStatusCode();
        assertEquals(HttpStatus.OK.value(), updateStatusCode);

        // Send a GET request to retrieve the updated user by ID
        Response getUserResponse = when()
                .get("/users/id/" + userId)
                .then()
                .extract().response();

        // Check that the response status code is 200 when the user is found
        assertEquals(HttpStatus.OK.value(), getUserResponse.getStatusCode());

        // Extract user details from the response and verify they match the updated user
        String name = getUserResponse.jsonPath().getString("name");
        String email = getUserResponse.jsonPath().getString("email");
        String phone = getUserResponse.jsonPath().getString("phone");
        String address = getUserResponse.jsonPath().getString("address");
        String zipcode = getUserResponse.jsonPath().getString("zipcode");

        assertEquals(updatedUser.getName(), name);
        assertEquals(updatedUser.getEmail(), email);
        assertEquals(updatedUser.getPhone(), phone);
        assertEquals(updatedUser.getAddress(), address);
        assertEquals(updatedUser.getZipcode(), zipcode);
    }

    

}
