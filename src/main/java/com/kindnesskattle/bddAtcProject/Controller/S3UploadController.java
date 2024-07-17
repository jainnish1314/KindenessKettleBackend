package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.Services.S3UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/kindnessKettle")
@Slf4j
public class S3UploadController {

    private final S3UploadService s3UploadService;

    @Autowired
    public S3UploadController(S3UploadService s3UploadService) {
        this.s3UploadService = s3UploadService;
    }

    @PostMapping("/uploadPhotoToProfiles")
    public String uploadPhotoToProfiles(@RequestPart("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        log.info("Received request to upload photo to Profiles folder. File Name: {}, Content Type: {}, File Size: {} bytes",
                fileName, file.getContentType(), file.getSize());

        String fileUrl = s3UploadService.uploadPhotoToProfiles(file);

        if (fileUrl != null) {
            return  fileUrl;
        } else {
            return "Failed to upload photo to Profiles folder.";
        }
    }


    @PostMapping("/uploadPhotoToFoodPost")
    public String uploadPhotoToFoodPost(@RequestPart("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        log.info("Received request to upload photo to Profiles folder. File Name: {}, Content Type: {}, File Size: {} bytes",
                fileName, file.getContentType(), file.getSize());

        String fileUrl = s3UploadService.uploadPhotoToFoodPost(file);

        if (fileUrl != null) {
            return  fileUrl;
        } else {
            return "Failed to upload photo to Profiles folder.";
        }
    }

    @GetMapping("/downloadFromProfiles")
    public ResponseEntity<InputStreamResource> downloadFromProfiles(@RequestParam String fileName) {
        log.info("Received request to download file '{}' from Profiles folder.", fileName);

        File file = s3UploadService.downloadFileFromProfiles(fileName);
        if (file != null) {
            try {
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                        .contentLength(file.length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } catch (FileNotFoundException e) {
                log.error("File '{}' not found in Profiles folder.", fileName);
                return ResponseEntity.notFound().build();
            }
        } else {
            log.error("Failed to download file '{}' from Profiles folder.", fileName);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/downloadFromFoodPost")
    public ResponseEntity<InputStreamResource> downloadFromFoodPost(@RequestParam String fileName) {
        log.info("Received request to download file '{}' from FoodPost folder.", fileName);

        File file = s3UploadService.downloadFileFromFoodPost(fileName);
        if (file != null) {
            try {
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                        .contentLength(file.length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } catch (FileNotFoundException e) {
                log.error("File '{}' not found in FoodPost folder.", fileName);
                return ResponseEntity.notFound().build();
            }
        } else {
            log.error("Failed to download file '{}' from FoodPost folder.", fileName);
            return ResponseEntity.notFound().build();
        }
    }
}
