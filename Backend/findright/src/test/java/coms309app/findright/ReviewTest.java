package coms309app.findright;

import coms309app.findright.Reviews.Review;
import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.ServiceRequest.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewTest {

    private Review review;

    @BeforeEach
    public void setUp() {
        review = new Review();
    }

    @Test
    public void testSetAndGetId() {
        review.setId(1L);
        assertEquals(1L, review.getId());
    }

    @Test
    public void testSetAndGetComment() {
        review.setComment("This is a test comment");
        assertEquals("This is a test comment", review.getComment());
    }

    @Test
    public void testSetAndGetRating() {
        review.setRating(4);
        assertEquals(4, review.getRating());
    }

    @Test
    public void testSetAndGetServiceProvider() {
        ServiceProvider serviceProvider = new ServiceProvider();
        review.setServiceProvider(serviceProvider);
        assertEquals(serviceProvider, review.getServiceProvider());
    }

    @Test
    public void testSetAndGetServiceRequest() {
        ServiceRequest serviceRequest = new ServiceRequest();
        review.setServiceRequest(serviceRequest);
        assertEquals(serviceRequest, review.getServiceRequest());
    }

}
