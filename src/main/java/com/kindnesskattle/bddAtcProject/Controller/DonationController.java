package com.kindnesskattle.bddAtcProject.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindnesskattle.bddAtcProject.DTO.DonationPostDetailsDTO;
import com.kindnesskattle.bddAtcProject.DTO.DontationAddressDTO;
import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Services.CreateDonationService;
import com.kindnesskattle.bddAtcProject.Services.MailService;
import com.kindnesskattle.bddAtcProject.Services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class DonationController {

    @Autowired
    CreateDonationService createDonationPost;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;
    @GetMapping("/checking_pin_code/{pin_code}")
    public ResponseEntity<?> pincodechecking(@PathVariable String pin_code) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://www.postalpincode.in/api/pincode/" + pin_code;
        String response;
        try {
            response = restTemplate.getForObject(apiUrl, String.class);
            System.out.println(response);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while calling the external API");
        }
        if (response != null && !response.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response);
                String status = jsonNode.get("Status").asText();
                if ("Error".equals(status)) {
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.ok(response); // Adjust the response format as needed
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Empty response from the external API");
        }
    }


    @PostMapping("/donationPosts")
    public ResponseEntity<String> createDonationPost(@RequestBody DontationAddressDTO request) {
        try {
             long userid = request.getUserId();

            String currentemail = userService.getEmailByUserId(userid);
            DonationPost donationPost = createDonationPost.createDonationPost(request);
            List<String> emails = userService.getAllUserEmails();

            if (emails.contains(currentemail)) {
                emails.remove(currentemail);
            }

            String subject = "New Opportunity for Giving";
            String body = "<html><head><style>body { font-family: Arial, sans-serif; }</style></head><body>" +
                    "<p>Dear Sir/madam,</p>" +
                    "<p>I hope this message finds you well.</p>" +
                    "<p style=\"font-weight: bold;\">I'm excited to share that a new donation is now available on our platform. You can explore it by clicking <a href=\"https://kindnesskettle.projects.bbdgrad.com/web/\" style=\"color: #007bff; text-decoration: none;\">here</a>.</p>" +
                    "<p>This presents a chance for us to come together and make a difference in the lives of others. Whether it's through a small act of kindness or a larger gesture, every contribution counts.</p>" +
                    "<p>I encourage you to explore this opportunity and consider how you can participate in spreading positivity and support.</p>" +
                    "<p>Thank you for your ongoing support and generosity.</p>" +
                    "<p>Best regards,<br>KindnessKettle Team</p>" +
                    "</body></html>";

            System.out.println(emails);
            try {
                mailService.sendEmails(emails, subject, body);
                System.out.println("email sent successfully");
            } catch (MessagingException e) {
                System.out.println("failed to send mail");
            }

            return ResponseEntity.ok("Donation post added successfully");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add donation post: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/donationPosts/{postId}")
    public ResponseEntity<String> deleteDonationPost(@PathVariable Long postId) {
        try {
            createDonationPost.deleteDonationPost(postId);
            return ResponseEntity.ok("Donation post and associated address deleted successfully");
        } catch (IllegalArgumentException e) {
            throw new  IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/fetchDonationPosts/{postId}")
    public ResponseEntity<DonationPostDetailsDTO> getDonationPostDetails(@PathVariable Long postId) {
        try {
            DonationPostDetailsDTO detailsDTO = createDonationPost.getDonationPostDetails(postId);
            return ResponseEntity.ok(detailsDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/fetchAllDonationPosts")
    public ResponseEntity<List<DonationPostDetailsDTO>> getAllDonationPostsDetails() {
        List<DonationPostDetailsDTO> donationPostsDetails = createDonationPost.getAllDonationPostsDetails();
        return ResponseEntity.ok(donationPostsDetails);
    }

    @PutMapping("/updatePost/{postId}")
    public ResponseEntity<DonationPost> updateDonationPost(@PathVariable Long postId, @RequestBody DontationAddressDTO dontationAddressDTO) {
        DonationPost updatedDonationPost = createDonationPost.updateDonationPost(postId, dontationAddressDTO);
        return ResponseEntity.ok(updatedDonationPost);


    }

    @PutMapping("/updateactive/{postId}/status")
    public ResponseEntity<DonationPost> updateDonationPostStatus(@PathVariable Long postId, @RequestParam boolean isPicked) {
        DonationPost updatedDonationPost = createDonationPost.updateDonationPostStatus(postId, isPicked);
        return ResponseEntity.ok(updatedDonationPost);
    }

}
