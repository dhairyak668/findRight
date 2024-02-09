package com.findRight.findRightapp.Service;


import com.findRight.findRightapp.SignUp.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    public static String register(SignUpRequest request) {
        return "works";
    }
}
