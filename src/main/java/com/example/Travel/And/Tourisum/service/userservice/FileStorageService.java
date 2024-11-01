package com.example.Travel.And.Tourisum.service.userservice;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    
    private static String uploadDir = "./src/main/resources/static/uploads/profiles";

    public static String saveFile(MultipartFile file, String username) throws IOException {
        // Ensure directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Save file with a unique name (e.g., username as part of the filename)
        String fileName = username + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path to the stored file
        return fileName;
    }

    private static String uploaddir = "src/main/resources/static/uploads/places"; // Adjust the upload directory as needed

    public static String savePlaceFile(MultipartFile file, String placeId) throws IOException {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(uploaddir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save file with a unique name using placeId and original filename
        String fileName = placeId + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        
        // Copy the file to the specified location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path to the stored file
        return fileName; // Adjust if needed based on your folder structure
    }

}
