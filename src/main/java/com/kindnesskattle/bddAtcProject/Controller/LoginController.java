package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.AuthResponse;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private final UserService userService;


    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth")
    public AuthResponse generateToken(@RequestParam String email) {

        System.out.println("checking email is in our system or not");
        UserAccount user_id = userService.getUserByEmail(email);
        System.out.println("user_id is "+user_id);

        if(user_id==null)
        {
            return null;
        }

        AuthResponse response = new AuthResponse();
        response.setUserAccount(user_id);
        return response;
    }
}