package com.kindnesskattle.bddAtcProject.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/verify")
public class VerifyTokenController {

    private final JwtDecoder jwtDecoder;

    public VerifyTokenController(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @GetMapping
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix from the token
            String jwtToken = token.substring(7);

            // Parse and verify the token
            Jwt decodedJwt = jwtDecoder.decode(jwtToken);

            // Extract the email from the token claims
            Map<String, Object> claims = decodedJwt.getClaims();
            String email = (String) claims.get("email");

            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email claim is missing in token");
            }

            // Token is valid, return the email
            return ResponseEntity.ok("Token is valid for email: " + email);
        } catch (Exception e) {
            // Token verification failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + e.getMessage());
        }
    }
}