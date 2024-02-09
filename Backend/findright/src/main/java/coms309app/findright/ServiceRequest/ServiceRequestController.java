package coms309app.findright.ServiceRequest;


//import coms309app.findright.websocket.Message;
import coms309app.findright.Reviews.Review;
import coms309app.findright.Reviews.ReviewRepository;
import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.ServiceProvider.ServiceProviderRepository;
import coms309app.findright.User.User;
import coms309app.findright.User.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/service-requests")
@Api(value = "Service Request API", description = "REST APIs related to managing service requests.")

public class ServiceRequestController {

    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ServiceRequestController(ServiceRequestRepository serviceRequestRepository,
                                    UserRepository userRepository,
                                    ServiceProviderRepository serviceProviderRepository,
                                    ReviewRepository reviewRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.userRepository =userRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.reviewRepository = reviewRepository;
    }


    @PostMapping("/create")
    @ApiOperation(value = "Create a New Service Request", response = ServiceRequest.class)
    public ResponseEntity<ServiceRequest> createServiceRequest(@RequestBody ServiceRequest serviceRequest) {
        User user = userRepository.findByEmail(serviceRequest.getRequesterEmail()).orElse(null);
        if (user != null) {
            serviceRequest.setUser(user); // Set the user for the service request
            ServiceRequest savedRequest = serviceRequestRepository.save(serviceRequest);
            user.addServiceRequest(savedRequest); // Add the saved request to the user's list
            userRepository.save(user); // Save the user to update the changes
            return ResponseEntity.ok(savedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/by-zipcode/{zipCode}")
    @ApiOperation(value = "Get Service Requesters by Zip Code", response = ServiceRequest.class)
    public ResponseEntity<List<ServiceRequest>> getServiceRequestersByZipCode(@PathVariable String zipCode) {
        List<ServiceRequest> requesters = serviceRequestRepository.findByZipCode(zipCode);
        return ResponseEntity.ok(requesters);
    }

    @GetMapping("/by-email/{email}")
    @ApiOperation(value = "Get Service Requests by Email", response = ServiceRequest.class)
    public ResponseEntity<List<ServiceRequest>> getServiceRequestsByEmail(@PathVariable String email) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findByRequesterEmail(email);

        if (!serviceRequests.isEmpty()) {
            return ResponseEntity.ok(serviceRequests);
        } else {
            // Handle the case when there are no service requests connected to the specified email
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Service Request Details by ID", response = ServiceRequest.class)
    public ResponseEntity<ServiceRequest> getServiceRequestDetails(@PathVariable Long id) {
        // Check if the service request with the given ID exists
        if (!serviceRequestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve and return the service request details
        ServiceRequest requester = serviceRequestRepository.findById(id).orElse(null);
        if (requester != null) {
            return ResponseEntity.ok(requester);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get All Service Requests", response = ServiceRequest.class)
    public ResponseEntity<List<ServiceRequest>> getAllServiceRequests() {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findAll();
        return ResponseEntity.ok(serviceRequests);
    }

    @GetMapping("/by-zip-and-service-type")
    @ApiOperation(value = "Get Service Requests by Zip Code and Service Type", response = ServiceRequest.class)
    public ResponseEntity<List<ServiceRequest>> getServiceRequestsByZipAndServiceType(
            @RequestParam String zipCode,
            @RequestParam String serviceType
    ) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findByZipCodeAndServiceTypeNeeded(zipCode, serviceType);

        if (!serviceRequests.isEmpty()) {
            return ResponseEntity.ok(serviceRequests);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update Service Request by ID", response = ServiceRequest.class)
    public ResponseEntity<ServiceRequest> updateServiceRequest(@PathVariable Long id, @RequestBody ServiceRequest updatedRequester) {
        // Check if the service request with the given ID exists
        if (!serviceRequestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the ID for the updated requester
        updatedRequester.setId(id);

        // Save the updated requester
        ServiceRequest savedRequester = serviceRequestRepository.save(updatedRequester);
        return ResponseEntity.ok(savedRequester);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete Service Request by ID")
    public ResponseEntity<Void> deleteServiceRequest(@PathVariable Long id) {
        // Check if the service request with the given ID exists
        Optional<ServiceRequest> optionalServiceRequest = serviceRequestRepository.findById(id);

        if (optionalServiceRequest.isPresent()) {
            ServiceRequest serviceRequest = optionalServiceRequest.get();
            User user = userRepository.findByEmail(serviceRequest.getRequesterEmail()).orElse(null);

            if (user != null) {
                // Remove the service request from the user's list
                user.removeServiceRequest(serviceRequest);
                userRepository.save(user);
            }

            // Delete the service request
            serviceRequestRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{id}/start-chat")
    @ApiOperation(value = "Start a Chat for Service Request by ID")
    public ResponseEntity<?> startChat(@PathVariable Long id) {
        // Optional: Check if the service request exists
        Optional<ServiceRequest> serviceRequestOptional = serviceRequestRepository.findById(id);
        if (!serviceRequestOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Service Request not found");
        }

        ServiceRequest serviceRequest = serviceRequestOptional.get();

        // Optional: Check if there's already a chat session for this service request
        if (serviceRequest.getChatSessionId() != null) {
            return ResponseEntity.ok().body("Chat session already exists with ID: " + serviceRequest.getChatSessionId());
        }

        // Generate a unique chat session ID (could be a UUID or any unique string)
        String chatSessionId = UUID.randomUUID().toString();
        serviceRequest.setChatSessionId(chatSessionId);
        serviceRequestRepository.save(serviceRequest);

        // Return the chat session ID to the client
        return ResponseEntity.ok().body("Chat session started with ID: " + chatSessionId);
    }

    @PostMapping("/{request_id}/assign-service-provider/{sp_id}")
    @ApiOperation(value = "Assign Service Provider to Service Request", response = ServiceRequest.class)
    public ResponseEntity<?> assignServiceProviderToRequest(
            @PathVariable Long request_id,
            @PathVariable Long sp_id
    ) {
        // Check if the service request with the given ID exists
        Optional<ServiceRequest> serviceRequestOptional = serviceRequestRepository.findById(request_id);
        if (!serviceRequestOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Request not found with ID: " + request_id);
        }

        // Check if the service provider with the given ID exists
        Optional<ServiceProvider> serviceProviderOptional = serviceProviderRepository.findById(sp_id);
        if (!serviceProviderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Provider not found with ID: " + sp_id);
        }

        // Get the service request and service provider entities
        ServiceRequest serviceRequest = serviceRequestOptional.get();
        ServiceProvider serviceProvider = serviceProviderOptional.get();

        // Check if the service request already has a service provider assigned
        if (serviceRequest.getServiceProvider() != null) {
            return ResponseEntity.badRequest().body("Service Request already assigned to a Service Provider.");
        }

        // Assign the service provider to the service request
        serviceRequest.setServiceProvider(serviceProvider);
        serviceRequest.setStage(1);
        serviceRequestRepository.save(serviceRequest);

        // Add the service request to the list of myProjects for the service provider
        serviceProvider.addServiceRequest(serviceRequest);
        serviceProviderRepository.save(serviceProvider);

        return ResponseEntity.ok(serviceRequest);
    }

    @PostMapping("/mark-request-complete/{request_id}")
    @ApiOperation(value = "Mark Service Request complete", response = ServiceRequest.class)
    public ResponseEntity<?> markRequestComplete(@PathVariable Long request_id){
        // Check if the service request with the given ID exists
        Optional<ServiceRequest> serviceRequestOptional = serviceRequestRepository.findById(request_id);
        if (!serviceRequestOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Request not found with ID: " + request_id);
        }
        ServiceRequest serviceRequest = serviceRequestOptional.get();
        serviceRequest.setStage(2);
        serviceRequestRepository.save(serviceRequest);
        return ResponseEntity.ok(serviceRequest);
    }


    @DeleteMapping("/{request_id}/unassign-service-provider")
    @ApiOperation(value = "Unassign Service Provider from Service Request")
    public ResponseEntity<?> unassignServiceProviderFromRequest(@PathVariable Long request_id) {
        // Check if the service request with the given ID exists
        Optional<ServiceRequest> serviceRequestOptional = serviceRequestRepository.findById(request_id);
        if (!serviceRequestOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Request not found with ID: " + request_id);
        }

        // Get the service request entity
        ServiceRequest serviceRequest = serviceRequestOptional.get();

        // Check if the service request has a service provider assigned
        if (serviceRequest.getServiceProvider() == null) {
            return ResponseEntity.badRequest().body("Service Request is not assigned to any Service Provider.");
        }

        // Get the assigned service provider
        ServiceProvider assignedServiceProvider = serviceRequest.getServiceProvider();

        // Unassign the service provider from the service request
        serviceRequest.setServiceProvider(null);
        serviceRequest.setStage(0);
        serviceRequestRepository.save(serviceRequest);

        // Remove the service request from the list of myProjects for the service provider
        assignedServiceProvider.removeServiceRequest(serviceRequest);
        serviceProviderRepository.save(assignedServiceProvider);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-stage/{stage}")
    @ApiOperation(value = "Get Service Requests by Stage", response = ServiceRequest.class)
    public ResponseEntity<List<ServiceRequest>> getServiceRequestsByStage(@PathVariable int stage) {
        //make check for stage value should be only 0,1,2
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findByStage(stage);
        return ResponseEntity.ok(serviceRequests);
    }
    @GetMapping("/by-user-and-stage/{userId}/{stage}")
    @ApiOperation(value = "Get Service Requests for User and Stage", response = ServiceRequest.class)
    public ResponseEntity<?> getServiceRequestsForUserAndStage(
            @PathVariable Long userId,
            @PathVariable int stage
    ) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            if(stage>2 || stage<0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stage value needs to be between[0,2]");
            }
            User user = userOptional.get();
            List<ServiceRequest> serviceRequests = serviceRequestRepository.findByUserAndStage(user, stage);
            return ResponseEntity.ok(serviceRequests);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
    }

    @GetMapping("/by-service-provider-and-stage/{providerId}/{stage}")
    @ApiOperation(value = "Get Service Requests for Service Provider and Stage", response = ServiceRequest.class)
    public ResponseEntity<?> getServiceRequestsForServiceProviderAndStage(
            @PathVariable Long providerId,
            @PathVariable int stage
    ) {
        //check if SP is present
        Optional<ServiceProvider> serviceProviderOptional = serviceProviderRepository.findById(providerId);
        if (serviceProviderOptional.isPresent()) {
            //check if stage is in bounds
            if(stage>2 || stage<0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stage value needs to be between[0,2]");
            }
            ServiceProvider serviceProvider = serviceProviderOptional.get();
            List<ServiceRequest> serviceRequests = serviceRequestRepository.findByServiceProviderAndStage(serviceProvider, stage);
            return ResponseEntity.ok(serviceRequests);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Provider not found with ID: " + providerId);
        }
    }

    @PostMapping("{request_id}/add-review")
    @ApiOperation(value = "Give review to a Service Request", response = Review.class)
    public ResponseEntity<?> addReview(@PathVariable Long request_id,
                                       @RequestBody Review review){

        //check if Service Request Exists
        Optional<ServiceRequest> serviceRequestOptional = serviceRequestRepository.findById(request_id);
        if (!serviceRequestOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Request not found with ID: " + request_id);
        }

        // Get the service request entity
        ServiceRequest serviceRequest = serviceRequestOptional.get();

        // Check if the service request has a service provider assigned
        if (serviceRequest.getServiceProvider() == null) {
            return ResponseEntity.badRequest().body("Service Request is not assigned to any Service Provider.");
        }

        //Check if Review already exists or not.
        if(serviceRequest.getReview() != null){
            return ResponseEntity.badRequest().body("Review already exists for this Service Request");
        }

        // Get the assigned service provider
        ServiceProvider serviceProvider = serviceRequest.getServiceProvider();
        review.setServiceProvider(serviceProvider);

        //assign service request to the review for the relation
        review.setServiceRequest(serviceRequest);

        //add review to service request
        serviceRequest.setReview(review);

        //add the review to the SP's list of reviews and update rating
        serviceProvider.addReview(review);

        reviewRepository.save(review);
        serviceProviderRepository.save(serviceProvider);
        serviceRequestRepository.save(serviceRequest);

        return ResponseEntity.ok().body(review);
    }
}