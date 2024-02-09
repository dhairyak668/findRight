package coms309app.findright.Reviews;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.ServiceRequest.ServiceRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "sp_id")
    @JsonIgnore
    private ServiceProvider serviceProvider = null;

    @OneToOne
    @JoinColumn(name = "service_request_id")
    @JsonIgnore
    private ServiceRequest serviceRequest = null;
}
