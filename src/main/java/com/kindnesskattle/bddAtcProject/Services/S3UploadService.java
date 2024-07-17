package com.kindnesskattle.bddAtcProject.Services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import java.io.*;

@Service
public class S3UploadService {

    private final AmazonS3 s3Client;

    public S3UploadService() {
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
    }

    public String uploadPhotoToProfiles(MultipartFile file) {
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        String keyName = "Profiles/" + uniqueFileName;
       return uploadPhoto("unique-kindnesskettle-image", keyName, file);
    }


    public String uploadPhotoToFoodPost( MultipartFile file) {
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        String keyName = "FoodPost/" + uniqueFileName;
        return uploadPhoto("unique-kindnesskettle-image", keyName, file);
    }

    public File downloadFileFromProfiles(String fileName) {
        String keyName = "Profiles/" + fileName;
        return downloadFile("unique-kindnesskettle-image", keyName);
    }

    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = getFileExtension(originalFileName);
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID + "." + fileExtension;
    }

    public File downloadFileFromFoodPost(String fileName) {
        String keyName = "FoodPost/" + fileName;
        return downloadFile("unique-kindnesskettle-image", keyName);
    }

    private String uploadPhoto(String bucketName, String keyName, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata));

            // Construct and return the URL of the uploaded file
            String fileUrl = s3Client.getUrl(bucketName, keyName).toString();
            return fileUrl;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private File downloadFile(String bucketName, String keyName) {
        File tempFile = null;
        try {
            S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
            InputStream inputStream = object.getObjectContent();
            String[] keyParts = keyName.split("/");
            String fileName = keyParts[keyParts.length - 1]; // Extract the file name from the key
            tempFile = File.createTempFile("temp", getFileExtension(fileName)); // Use the file extension from the original file
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException | AmazonS3Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }


    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return ""; // No file extension found
        }
        return fileName.substring(lastIndexOfDot);
    }


}
