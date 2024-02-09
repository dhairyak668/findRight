package coms309app.findright;

import coms309app.findright.User.User;
import coms309app.findright.User.UserRepository;
import coms309app.findright.User.UserServices;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserServicesIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServices userServices;

    @BeforeEach
    public void setUp() {
        // Set the base URI to localhost with port 8080 (or the port your Spring Boot app is running on)
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }


}
