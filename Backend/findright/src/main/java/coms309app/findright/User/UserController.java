package coms309app.findright.User;


import coms309app.findright.ServiceProvider.ServiceProvider;
import coms309app.findright.ServiceProvider.ServiceProviderRepository;
import coms309app.findright.ServiceRequest.ServiceRequest;
import coms309app.findright.ServiceRequest.ServiceRequestRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Api(value = "User API", description = "REST APIs related to managing users.")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    @GetMapping("all")
    @ApiOperation(value = "Get All Users", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("id/{id}")
    @ApiOperation(value = "Get User by ID", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("email/{email}")
    @ApiOperation(value = "Get User by Email", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<User> getUserByEmail(
            @PathVariable("email")
            @ApiParam(value = "The email address of the user to retrieve.", example = "user@example.com")
            String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("post")
    @ApiOperation(value = "Create a New User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public User postUser(@RequestBody User newUser) {
        userRepository.save(newUser);
        return newUser;
    }

    //delete user entity
    @DeleteMapping("delete_id/{id}")
    @ApiOperation(value = "Delete User by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "User deleted successfully"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        // Check if the user with the given ID exists
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Fetch the user
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Remove associated service requests
            List<ServiceRequest> userRequests = user.getMyRequests();
            userRequests.forEach(request -> {
                serviceRequestRepository.deleteById(request.getId());
            });

            // Delete the user
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("update/{id}")
    @ApiOperation(value = "Update User by ID", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public User updateUser(
            @PathVariable Long id,
            @RequestBody
            @ApiParam(value = "Updated user information.", required = true)
            User updatedUser) {
        User serviceUser = userRepository.findById(id).orElse(null);

        if (serviceUser != null) {
            serviceUser.setName(updatedUser.getName());
            serviceUser.setEmail(updatedUser.getEmail());
            serviceUser.setPassword(updatedUser.getPassword());
            serviceUser.setPhone(updatedUser.getPhone());
            serviceUser.setAddress(updatedUser.getAddress());
            serviceUser.setZipcode(updatedUser.getZipcode());
            userRepository.save(serviceUser);
            return serviceUser;
        } else {
            return null;
        }
    }

    @PutMapping("update/password/{email}")
    @ApiOperation(value = "Update User Password by Email", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password updated successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public User updateUserPassword(
            @RequestParam
            @ApiParam(value = "The new password for the user.", example = "new_password", required = true)
            String password,
            @PathVariable String email) {
        User serviceUser = userRepository.findByEmail(email).orElse(null);

        if (serviceUser != null) {
            serviceUser.setPassword(password);
            return userRepository.save(serviceUser);
        } else {
            return null;
        }
    }

    @PutMapping("add_favorite")
    @ApiOperation(value = "Add a Favorite Service Provider to User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Favorite added successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "User or Service Provider not found")
    })
    public ResponseEntity<?> addFavorite(
            @RequestParam
            @ApiParam(value = "The ID of the user.", example = "123", required = true)
            Long user_id,
            @RequestParam
            @ApiParam(value = "The ID of the service provider to add as a favorite.", example = "456", required = true)
            Long sp_id) {
        User user = userRepository.findById(user_id).orElse(null);
        ServiceProvider serviceProvider = serviceProviderRepository.findById(sp_id).orElse(null);
        Set<ServiceProvider> favServiceProviders = null;
        Set<User> favByUsers = null;

        if (user != null && serviceProvider != null) {
            //add fav SP to user list
            favServiceProviders = user.getFavoriteServiceProviders();
            favServiceProviders.add(serviceProvider);
            userRepository.save(user);

            //add user favorited by to SP list
            favByUsers = serviceProvider.getFavoriteByUsers();
            favByUsers.add(user);
            serviceProviderRepository.save(serviceProvider);

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("remove_favorite")
    @ApiOperation(value = "remove a Favorite Service Provider for a User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Favorite removed successfully"),
            @ApiResponse(code = 400, message = "Bad Request. Favorite Does Not Exist"),
            @ApiResponse(code = 404, message = "User or Service Provider not found")
    })
    public ResponseEntity<?> removeFavorite(
            @RequestParam
            @ApiParam(value = "The ID of the user.", example = "123", required = true)
            Long user_id,
            @RequestParam
            @ApiParam(value = "The ID of the service provider to remove as a favorite.", example = "456", required = true)
            Long sp_id) {
        User user = userRepository.findById(user_id).orElse(null);
        ServiceProvider serviceProvider = serviceProviderRepository.findById(sp_id).orElse(null);

        if (user != null && serviceProvider != null) {

            Set<ServiceProvider> favServiceProviders = user.getFavoriteServiceProviders();
            Set<User> favByUsers  = serviceProvider.getFavoriteByUsers();

            //check if SP is already a fav or not
            if(!favServiceProviders.contains(serviceProvider))
                return ResponseEntity.badRequest().build();

            //remove fav SP from user list
            favServiceProviders.remove(serviceProvider);
            userRepository.save(user);

            //remove user favorited by from SP list
            favByUsers.remove(user);
            serviceProviderRepository.save(serviceProvider);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/get_all_favorite")
    @ApiOperation(value = "Get Favorite Service Providers for User", response = Set.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<Set<ServiceProvider>> getFavoriteServiceProvidersForUser(@PathVariable Long userId) {
        // Check if the user with the given ID exists
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Return the set of favorite service providers for the user
            return ResponseEntity.ok(user.getFavoriteServiceProviders());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}