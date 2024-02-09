package com.findRight.findRightapp.SignUp;

import com.findRight.findRightapp.Service.SignUpService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/signup")
@AllArgsConstructor
public class UserSignUpController {

    private SignUpService signUpService;

    public String signUp(@RequestBody SignUpRequest request) {
        return SignUpService.register(request);
    }
}
