package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.Entities.City;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.CityRepository;
import com.kindnesskattle.bddAtcProject.Repository.SubscriptionRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import com.kindnesskattle.bddAtcProject.Services.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
@Slf4j
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @PostMapping("/subscribe/{userId}/{cityId}")
    public ResponseEntity<String> subscribeToCity(@PathVariable Long userId, @PathVariable Long cityId) {
        try {
            log.info("Log message :- userID= "+userId +"cityId = "+ cityId );

            UserAccount user = userAccountRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + cityId));

            log.info("calling subscription method" );

            subscriptionService.subscribe(user, city);

            log.info("subscription method finished" );
            return ResponseEntity.ok("Subscribed to city successfully");
        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/unsubscribe/{userId}/{cityId}")
    public ResponseEntity<String> unsubscribeFromCity(@PathVariable Long userId, @PathVariable Long cityId) {
        try {
            UserAccount user = userAccountRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + cityId));

            subscriptionService.unsubscribe(user, city);
            return ResponseEntity.ok("Unsubscribed from city successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
