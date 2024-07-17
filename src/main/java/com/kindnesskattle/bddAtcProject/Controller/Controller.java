package com.kindnesskattle.bddAtcProject.Controller;
import com.kindnesskattle.bddAtcProject.DTO.LikesSummaryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;


@RestController
@RequestMapping("/kindnessKettle")
@Slf4j
public class Controller {



    @GetMapping("/home")
    public ResponseEntity<String> home() {
        System.out.println("Welcome to kindnessKettle");
        return ResponseEntity.ok("<html><body style='background-color: #f4f4f4; color: #333; font-family: Arial, sans-serif; text-align: center;'>" +
                "<h1 style='color: #4285f4;'>Welcome to Kindness Kettle! 🌟</h1>" +
                "<p style='font-size: 18px;'>Meet our amazing team:</p>" +
                "<ul style='list-style-type: none; padding: 0;'>" +
                "<li style='font-size: 16px; color: #4285f4;'>Krishna Singh</li>" +
                "<li style='font-size: 16px; color: #4285f4;'>Ajay Singh</li>" +
                "<li style='font-size: 16px; color: #4285f4;'>Nisha Jain</li>" +
                "</ul>" +
                "<p style='font-size: 18px;'>We're here to make your experience delightful. Feel free to explore and share kindness with our community. If you have any questions, reach out to us. Cheers to a journey filled with positivity and warmth!</p>" +
                "</body></html>");

    }

}





