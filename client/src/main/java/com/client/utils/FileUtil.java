package com.client.utils;

import org.springframework.core.io.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;

public class FileUtil {

    public static String saveFile(MultipartFile file, String uploadPath) throws IOException {
        String fileName = file.getOriginalFilename();
        Path uploadDir = Paths.get(uploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    public static Resource getFileAsResource(String fileName, String uploadPath) throws IOException {
        Path filePath = Paths.get(uploadPath).resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("File not found or not readable: " + fileName);
        }
    }
}
