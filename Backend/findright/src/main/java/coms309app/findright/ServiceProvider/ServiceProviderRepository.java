package coms309app.findright.ServiceProvider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    List<ServiceProvider> findByServicesOfferedIn(List<String> services);

    List<ServiceProvider> findByServicesOfferedInAndZipcode(List<String> services, String zipcode);

    // You can add more custom query methods as needed
    List<ServiceProvider> findByZipcode(String zipcode);
    Optional<ServiceProvider> findByEmail(String email);


}

