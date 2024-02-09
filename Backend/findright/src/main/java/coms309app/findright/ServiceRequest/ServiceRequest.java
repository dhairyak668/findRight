package coms309app.findright.ServiceRequest;


import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309app.findright.Reviews.Review;
import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.User.User;
import jakarta.persistence.*;



@Entity
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sp_id")
    private ServiceProvider serviceProvider;

    private String requesterName;

    private String requesterEmail;

    private String requesterPhoneNumber;
    private String address;
    private String zipCode;
    private String description;
    private String serviceTypeNeeded;
    @Column(name = "chat_session_id")
    private String chatSessionId;

    //stage feature to know current position of each SR in app
    @Column(columnDefinition = "integer default 0")
    private int stage;

    @OneToOne(mappedBy = "serviceRequest", cascade = CascadeType.ALL)
    private Review review;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterPhoneNumber() {
        return requesterPhoneNumber;
    }

    public void setRequesterPhoneNumber(String requesterPhoneNumber) {
        this.requesterPhoneNumber = requesterPhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceTypeNeeded() {
        return serviceTypeNeeded;
    }

    public void setServiceTypeNeeded(String serviceTypeNeeded) {
        this.serviceTypeNeeded = serviceTypeNeeded;
    }

    public String getChatSessionId() {
        return chatSessionId;
    }

    public void setChatSessionId(String chatSessionId) {
        this.chatSessionId = chatSessionId;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage){
        this.stage = stage;
    }

    public void bumpUpStage(){
        if(this.stage < 2 && this.stage>-1)
            this.stage++;
    }

    public void bumpDownStage(){
        if(this.stage>0 && this.stage<3){
            this.stage--;
        }
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}