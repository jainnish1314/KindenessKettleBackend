package com.kindnesskattle.bddAtcProject.Controller;


import com.kindnesskattle.bddAtcProject.DTO.UserDto;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Services.S3UploadService;
import com.kindnesskattle.bddAtcProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private S3UploadService s3UploadService;

    @PutMapping("/update/{emailAddress}")
    public ResponseEntity<UserAccount> updateUserProfile(
            @PathVariable String emailAddress,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "profileDescription", required = false) String profileDescription,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) {

        System.out.println("updating profile");

        // Get the existing user details
        UserAccount user = userService.getUserByEmail(emailAddress);

        // Create UserDto with updated data
        UserDto updatedUserDto = new UserDto();
        if (firstName != null) {
            updatedUserDto.setFirstName(firstName);
        } else {
            updatedUserDto.setFirstName(user.getFirstName());
        }
        if (lastName != null) {
            updatedUserDto.setLastName(lastName);
        } else {
            updatedUserDto.setLastName(user.getLastName());
        }
        if (profileDescription != null) {
            updatedUserDto.setProfileDescription(profileDescription);
        } else {
            updatedUserDto.setProfileDescription(user.getProfileDescription());
        }

        // Upload profile image to S3 if provided
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3UploadService.uploadPhotoToProfiles(profileImage);
            updatedUserDto.setImageUrl(imageUrl);
        } else {
            // Keep the previous image URL if profile image is not provided
            updatedUserDto.setImageUrl(user.getImageUrl());
        }

        // Exclude the username field from being updated
        updatedUserDto.setUsername(user.getUsername());

        // Update user profile
        UserAccount updatedUser = userService.updateUserProfile(emailAddress, updatedUserDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
