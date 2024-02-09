package coms309app.findright.ServiceRequest;

import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByZipCode(String zipCode);
    List<ServiceRequest> findByRequesterEmail(String requesterEmail);

    List<ServiceRequest> findAll();

    //  void deleteByEmail(String email);

    //  void deleteByZipCode(String zipCode);

    // List<ServiceRequest> findByEmail(String email);

    List<ServiceRequest> findByZipCodeAndServiceTypeNeeded(String zipCode, String serviceType);

    List<ServiceRequest> findByStage(int stage);

    List<ServiceRequest> findByUserAndStage(User user, int stage);

    List<ServiceRequest> findByServiceProviderAndStage(ServiceProvider serviceProvider, int stage);



}