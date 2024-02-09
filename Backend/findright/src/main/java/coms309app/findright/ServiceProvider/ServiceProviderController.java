package coms309app.findright.ServiceProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-providers")
@Api(value = "Service Provider API", description = "REST APIs related to managing service providers.")
public class ServiceProviderController {

    private final ServiceProviderRepository serviceProviderRepository;

    // Constructor injection for ServiceProviderRepository
    public ServiceProviderController(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
    }

    @GetMapping("/getallserviceproviders")
    @ApiOperation(value = "Get All Service Providers", response = ServiceProvider.class)
    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepository.findAll();
    }

    @GetMapping("/getspbyservicetype")
    @ApiOperation(value = "Get Service Providers by Service Type", response = ServiceProvider.class)
    public List<ServiceProvider> getServiceProvidersByServiceType(@RequestParam List<String> services) {
        return serviceProviderRepository.findByServicesOfferedIn(services);
    }

    @GetMapping("getspbyemail/{email}")
    @ApiOperation(value = "Get Service Provider by Email", response = ServiceProvider.class)
    public ResponseEntity<ServiceProvider> getServiceProviderByEmail(@PathVariable String email) {
        ServiceProvider serviceProvider = serviceProviderRepository.findByEmail(email).orElse(null);

        if (serviceProvider != null) {
            return ResponseEntity.ok(serviceProvider);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getserviceproviderbyservicetypeandzip")
    @ApiOperation(value = "Get Service Providers by Service Type and Zip Code", response = ServiceProvider.class)
    public List<ServiceProvider> getServiceProvidersByServiceTypeAndZip(
            @RequestParam List<String> services,
            @RequestParam String zipcode
    ) {
        return serviceProviderRepository.findByServicesOfferedInAndZipcode(services, zipcode);
    }

    @GetMapping("/getspbyzipcode")
    @ApiOperation(value = "Get Service Providers by Zip Code", response = ServiceProvider.class)
    public List<ServiceProvider> getServiceProviderByZipcode(@RequestParam String zipcode) {
        return serviceProviderRepository.findByZipcode(zipcode);
    }

    @PostMapping("/createserviceprovider")
    @ApiOperation(value = "Create a New Service Provider", response = ServiceProvider.class)
    public ResponseEntity<ServiceProvider> createServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        // Create a service provider
        ServiceProvider createdProvider = serviceProviderRepository.save(serviceProvider);
        return ResponseEntity.ok(createdProvider);
    }

    @PutMapping("/changespdetails/{id}")
    @ApiOperation(value = "Change Service Provider Details by ID", response = ServiceProvider.class)
    public ResponseEntity<ServiceProvider> changeServiceProviderDetails(
            @PathVariable Long id,
            @RequestBody ServiceProvider updatedProvider
    ) {
        if (!serviceProviderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        updatedProvider.setId(id);
        ServiceProvider savedProvider = serviceProviderRepository.save(updatedProvider);
        return ResponseEntity.ok(savedProvider);
    }

    @PutMapping("update/password/{email}")
    @ApiOperation(value = "Update User Password by Email", response = ServiceProvider.class)
    public ServiceProvider updateUserPassword(@RequestBody String password, @PathVariable String email) {
        ServiceProvider serviceProvider = serviceProviderRepository.findByEmail(email).orElse(null);

        if (serviceProvider != null) {
            serviceProvider.setPassword(password);
            serviceProviderRepository.save(serviceProvider);
            return serviceProvider;
        } else {
            return null;
        }
    }
}
