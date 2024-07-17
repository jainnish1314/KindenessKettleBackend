package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.UserAnalyticsDTO;
import com.kindnesskattle.bddAtcProject.DTO.UserDto;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import com.kindnesskattle.bddAtcProject.Services.UserAnalyticsService;
import com.kindnesskattle.bddAtcProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kindnesskettle/useranalytics")
public class UserAnalyticsController {


    @Autowired
    public UserService userService;

    private final UserAnalyticsService userAnalyticsService;


    @Autowired
    public UserAnalyticsController(UserAnalyticsService userAnalyticsService) {
        this.userAnalyticsService = userAnalyticsService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserAnalyticsDTO> getUserAnalytics(@PathVariable Long userId) {
        System.out.println("user id "+userId);
        UserAnalyticsDTO userAnalyticsDTO = userAnalyticsService.getUserAnalytics(userId);
        if (userAnalyticsDTO != null) {
            return new ResponseEntity<>(userAnalyticsDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
