package coms309app.findright.User;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//haven't used this class yet
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    public List<User> list(){
        return userRepository.findAll();
    }
}
