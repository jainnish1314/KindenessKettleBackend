package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.PickupCompleted;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.PickupCompletedRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PickupCompletedController {

    private final PickupCompletedRepository pickupCompletedRepository;
    private final UserAccountRepository userAccountRepository;
    private final DonationPostRepository donationPostRepository;

    @Autowired
    public PickupCompletedController(PickupCompletedRepository pickupCompletedRepository,
                                     UserAccountRepository userAccountRepository,
                                     DonationPostRepository donationPostRepository) {
        this.pickupCompletedRepository = pickupCompletedRepository;
        this.userAccountRepository = userAccountRepository;
        this.donationPostRepository = donationPostRepository;
    }

    @PostMapping("/pickup")
    public ResponseEntity<String> insertPickupCompleted(@RequestBody PickupCompletedRequest request) {
        if (request.getPickedUpByUserId() == null || request.getPostId() == null) {
            return ResponseEntity.badRequest().body("User ID or Post ID cannot be null");
        }

        // Check if user exists
        Optional<UserAccount> userExists = userAccountRepository.findById(request.getPickedUpByUserId());

        // Check if post exists
        Optional<DonationPost> postExists = donationPostRepository.findById(request.getPostId());
        if (userExists.isEmpty() && postExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User and Post not found");
        } else if (userExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else if (postExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        // Create a new PickupCompleted object
        PickupCompleted pickupCompleted = new PickupCompleted(request.getPickedUpByUserId(), request.getPostId(), LocalDateTime.now());

        // Save the PickupCompleted object to the database
        pickupCompletedRepository.save(pickupCompleted);

        // Return a success response
        return ResponseEntity.status(HttpStatus.CREATED).body("Pickup completed record created successfully");
    }

    @GetMapping("/getpickup/{userId}")
    public ResponseEntity<List<PickupCompletedWithDonationPostResponse>> getPickupCompletedWithDonationPostByUserId(@PathVariable Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Retrieve pickup completed records by userId
        List<PickupCompleted> pickupCompletedList = pickupCompletedRepository.findByPickedUpByUserId(userId);

        // Check if records exist
        if (pickupCompletedList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Create a list to hold PickupCompletedWithDonationPostResponse objects
        List<PickupCompletedWithDonationPostResponse> responseList = new ArrayList<>();

        // Iterate over pickupCompletedList and fetch details of associated donation posts
        for (PickupCompleted pickupCompleted : pickupCompletedList) {
            Long postId = pickupCompleted.getPostId();
            Optional<DonationPost> donationPostOptional = donationPostRepository.findById(postId);
            donationPostOptional.ifPresent(donationPost -> {
                // Create PickupCompletedWithDonationPostResponse object with details of pickup completed record and associated donation post
                PickupCompletedWithDonationPostResponse response = new PickupCompletedWithDonationPostResponse(pickupCompleted, donationPost);
                responseList.add(response);
            });
        }

        // Return the list of PickupCompletedWithDonationPostResponse objects
        return ResponseEntity.ok(responseList);
    }

    // New method to get PickupCompleted records by postId
    @GetMapping("/getpickupbypost/{postId}")
    public ResponseEntity<List<PickupCompletedWithUserResponse>> getPickupCompletedByPostId(@PathVariable Long postId) {
        if (postId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Retrieve pickup completed records by postId
        List<PickupCompleted> pickupCompletedList = pickupCompletedRepository.findByPostId(postId);

        // Check if records exist
        if (pickupCompletedList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Create a list to hold PickupCompletedWithUserResponse objects
        List<PickupCompletedWithUserResponse> responseList = new ArrayList<>();

        // Iterate over pickupCompletedList and fetch details of associated user accounts
        for (PickupCompleted pickupCompleted : pickupCompletedList) {
            Long userId = pickupCompleted.getPickedUpByUserId();
            Optional<UserAccount> userAccountOptional = userAccountRepository.findById(userId);
            userAccountOptional.ifPresent(userAccount -> {
                // Create PickupCompletedWithUserResponse object with details of pickup completed record and associated user account
                PickupCompletedWithUserResponse response = new PickupCompletedWithUserResponse(pickupCompleted, userAccount);
                responseList.add(response);
            });
        }

        // Return the list of PickupCompletedWithUserResponse objects
        return ResponseEntity.ok(responseList);
    }

    @Transactional
    @DeleteMapping("/pickup")
    public ResponseEntity<String> deletePickupCompleted(@RequestParam Long userId, @RequestParam Long postId) {
        if (userId == null || postId == null) {
            return ResponseEntity.badRequest().body("User ID and Post ID cannot be null");
        }

        // Check if the PickupCompleted record exists
        Optional<PickupCompleted> pickupCompleted = pickupCompletedRepository.findByPickedUpByUserIdAndPostId(userId, postId);
        if (pickupCompleted.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pickup completed record not found");
        }

        // Delete the PickupCompleted record
        pickupCompletedRepository.deleteByPickedUpByUserIdAndPostId(userId, postId);

        // Return a success response
        return ResponseEntity.ok("Pickup completed record deleted successfully");
    }

    // Inner class representing the request for creating a pickup completed record
    static class PickupCompletedRequest {
        private Long pickedUpByUserId;
        private Long postId;

        // Getters and Setters
        public Long getPickedUpByUserId() {
            return pickedUpByUserId;
        }

        public void setPickedUpByUserId(Long pickedUpByUserId) {
            this.pickedUpByUserId = pickedUpByUserId;
        }

        public Long getPostId() {
            return postId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }
    }

    // Inner class representing the response including PickupCompleted and DonationPost details
    static class PickupCompletedWithDonationPostResponse {
        private PickupCompleted pickupCompleted;
        private DonationPost donationPost;

        // Constructor
        public PickupCompletedWithDonationPostResponse(PickupCompleted pickupCompleted, DonationPost donationPost) {
            this.pickupCompleted = pickupCompleted;
            this.donationPost = donationPost;
        }

        // Getters
        public PickupCompleted getPickupCompleted() {
            return pickupCompleted;
        }

        public DonationPost getDonationPost() {
            return donationPost;
        }
    }

    // Inner class representing the response including PickupCompleted and UserAccount details
    static class PickupCompletedWithUserResponse {
        private PickupCompleted pickupCompleted;
        private UserAccount userAccount;

        // Constructor
        public PickupCompletedWithUserResponse(PickupCompleted pickupCompleted, UserAccount userAccount) {
            this.pickupCompleted = pickupCompleted;
            this.userAccount = userAccount;
        }

        // Getters
        public PickupCompleted getPickupCompleted() {
            return pickupCompleted;
        }

        public UserAccount getUserAccount() {
            return userAccount;
        }
    }
}
