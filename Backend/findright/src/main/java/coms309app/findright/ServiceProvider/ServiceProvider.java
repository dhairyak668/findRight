package coms309app.findright.ServiceProvider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309app.findright.Reviews.Review;
import coms309app.findright.ServiceRequest.ServiceRequest;
import coms309app.findright.User.User;
import jakarta.persistence.*;
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
public class ServiceProvider {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sp_id")
    private Long id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;


    @NotEmpty
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotEmpty
    @Column(unique = true)
    //@Digits(fraction = 0, integer = 10)
    private String phoneNumber;


    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @NotEmpty
    @Column(nullable = false)
    private String address;

    @NotEmpty
    @Column(nullable = false)
    private String zipcode;

    @ElementCollection
    private List<String> servicesOffered = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "favoriteServiceProviders")
    private Set<User> favoriteByUsers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "my_projects", referencedColumnName = "sp_id")
    @JsonIgnore
    private List<ServiceRequest> myProjects = new ArrayList<>();

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    private double rating = 0.0;

    public ServiceProvider(String name, String phoneNumber, String email, String password, String address, String zipcode, List<String> servicesOffered) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
        this.zipcode = zipcode;
        this.servicesOffered = servicesOffered;
    }


    public void addFavoriteBy(User user) {
        favoriteByUsers.add(user);
    }

    public void removeFavoriteBy(User user) {
        favoriteByUsers.remove(user);
    }

    public void addServiceRequest(ServiceRequest serviceRequest) {
        this.myProjects.add(serviceRequest);
    }

    public void removeServiceRequest(ServiceRequest serviceRequest) {
        this.myProjects.remove(serviceRequest);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        this.rating = calculateAggregateRating();
    }

    public double calculateAggregateRating() {
        double totalRating = reviews.stream().mapToInt(Review::getRating).sum();
        double averageRating = reviews.isEmpty() ? 0.0 : totalRating / reviews.size();
        return Math.round(averageRating * 10.0) / 10.0;
    }
}