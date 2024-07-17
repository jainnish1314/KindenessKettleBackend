package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.UserDto;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Services.S3UploadService;
import com.kindnesskattle.bddAtcProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private S3UploadService s3UploadService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam(value = "firstName", required = true) String firstName,
            @RequestParam(value = "lastName", required = true) String lastName,
            @RequestParam(value = "emailAddress", required = true) String email,
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "profileDescription", required = true) String description,
            @RequestParam(value = "imageUrl", required = false) MultipartFile profilePicture) {

        if (firstName == null || lastName == null || email == null || username == null || description == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"success\": false, \"message\": \"Missing required fields\"}");
        }

        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmailAddress(email);
        userDto.setUsername(username);
        userDto.setProfileDescription(description);

        String imageUrl = null;
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                imageUrl = s3UploadService.uploadPhotoToProfiles(profilePicture);
                userDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"success\": false, \"message\": \"Failed to upload profile picture\"}");
            }
        }




        try {

            // Check if email already exists
            if (userService.emailExists(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"success\": false, \"message\": \"Email address already exists\"}");
            }

            // Check if username already exists
            if (userService.usernameExists(username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"success\": false, \"message\": \"Username already exists\"}");
            }


            UserAccount registeredUser = userService.registerUser(userDto);
            if (registeredUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("{\"success\": true, \"message\": \"User registered successfully\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"success\": false, \"message\": \"Failed to register user\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"success\": false, \"message\": \"An error occurred during registration\"}");
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"success\": false, \"message\": \"An unexpected error occurred\"}");
    }
}
