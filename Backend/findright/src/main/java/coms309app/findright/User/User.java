package coms309app.findright.User;

import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.ServiceRequest.ServiceRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @NotEmpty
    @Column(name = "name",nullable = false)
    private String name;


    @NotEmpty
    @Column(name="email",unique = true, nullable = false)
    private String email;

    @NotEmpty
    @Column(name="phone",unique = true)
    @Digits(fraction = 0, integer = 10)
    private String phone;

    @NotEmpty
    @Column(name="password",nullable = false)
    private String password;

    @NotEmpty
    @Column(nullable = false)
    private String address;

    @NotEmpty
    @Column(name="zipcode",nullable = false)
    private String zipcode;

    @ManyToMany
    @JoinTable(name = "fav_service_provider",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "sp_id")
    )
    @Column(name = "fav_sp")
    private Set<ServiceProvider> favoriteServiceProviders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_id")
    private List<ServiceRequest> myRequests = new ArrayList<>();


    public User(String name,
                String email,
                String phone,
                String password,
                String address,
                String zipcode) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.zipcode = zipcode;
    }

    public void addFavoriteServiceProvider(ServiceProvider serviceProvider){
        favoriteServiceProviders.add(serviceProvider);
        serviceProvider.addFavoriteBy(this);
    }

    public void addServiceRequest(ServiceRequest serviceRequest){
        this.myRequests.add(serviceRequest);
    }
    public void removeServiceRequest(ServiceRequest serviceRequest){
        this.myRequests.remove(serviceRequest);
    }

}
