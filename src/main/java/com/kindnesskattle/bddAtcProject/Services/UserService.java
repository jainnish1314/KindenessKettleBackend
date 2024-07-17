package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.DTO.UserDto;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private S3UploadService s3UploadService;

    public UserAccount getUserByEmail(String emailAddress) {
        return userAccountRepository.findByEmailAddress(emailAddress);
    }

    public boolean emailExists(String email) {
        return userAccountRepository.findByEmailAddress(email) != null;
    }

    public boolean usernameExists(String username) {
        return userAccountRepository.findByUsername(username) != null;
    }

    public List<String> getAllUserEmails() {
        return userAccountRepository.findAllEmails();
    }


    public UserAccount registerUser(UserDto userDto) {
        // You can perform any additional validation or business logic here before saving the user
                UserAccount userAccount = new UserAccount();

                userAccount.setFirstName(userDto.getFirstName());
                userAccount.setLastName((userDto.getLastName()));
                userAccount.setImageUrl(userDto.getImageUrl());
                userAccount.setUsername(userDto.getUsername());
                userAccount.setEmailAddress(userDto.getEmailAddress());
                userAccount.setProfileDescription(userDto.getProfileDescription());
                userAccount.setIsActive(userDto.isIs_active());

        return userAccountRepository.save(userAccount);


    }

    public UserAccount updateUserProfile(String emailAddress, UserDto updatedUserDto) {
        UserAccount user = userAccountRepository.findByEmailAddress(emailAddress);
        if (user != null) {
            user.setFirstName(updatedUserDto.getFirstName());
            user.setLastName(updatedUserDto.getLastName());
            user.setImageUrl(updatedUserDto.getImageUrl());
            user.setUsername(updatedUserDto.getUsername());
            user.setProfileDescription(updatedUserDto.getProfileDescription());
            user.setIsActive(updatedUserDto.isIs_active());
            return userAccountRepository.save(user);
        }
        return null;
    }

    public String getEmailByUserId(Long userId) {
        return userAccountRepository.findEmailById(userId);
    }

}

