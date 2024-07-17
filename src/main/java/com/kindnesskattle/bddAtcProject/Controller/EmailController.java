package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.EmailRequest;
import com.kindnesskattle.bddAtcProject.Services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/emails")
@Slf4j
public class EmailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmails(@RequestBody EmailRequest emailRequest) {
        List<String> toList = Arrays.asList(emailRequest.getTo().split(","));
        log.info("Received request to send emails to {} recipients with subject: {}", toList.size(), emailRequest.getSubject());
        try {
            mailService.sendEmails(toList, emailRequest.getSubject(), emailRequest.getBody());
            log.info("Emails sent successfully to {} recipients", toList.size());
            return ResponseEntity.ok("Emails sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send emails: " + e.getMessage());
        }
    }
}

