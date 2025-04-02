package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.exception.FileStorageException;
import com.nbnp.trip_to_go.exception.InvalidFileException;
import com.nbnp.trip_to_go.exception.FileNotFoundException;

import com.nbnp.trip_to_go.model.FolderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;


@Service("LocalService")
@Component
public class LocalFileServiceImpl implements FileService {


    @Value("${file.extensions}")
    private String[] allowedExtensions;

    @Value("${file.maxFileSize}")
    private long maxFileSize;

    @Value("${file.storageDirectory}")
    private String storageDirectory;


    public String upload(MultipartFile file, FolderType folderType) {

        if (storageDirectory == null || storageDirectory.isEmpty()) {
            throw new FileStorageException("Storage directory is not configured");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new InvalidFileException("File name cannot be null or empty");
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (!Arrays.asList(allowedExtensions).contains(extension)) {
            throw new IllegalArgumentException("The file must be one of " + Arrays.toString(allowedExtensions));
        }

        if (file.getSize() > maxFileSize * 1024 * 1024) {
            throw new IllegalArgumentException("The file size must be less than " + maxFileSize + " MB");
        }


        try {
            String uniqueName = UUID.randomUUID().toString().replace("-", "");
            File destinationFile = new File(storageDirectory, uniqueName + "." + extension);
            file.transferTo(destinationFile);
            return uniqueName;

        } catch (IOException e) {

            throw new FileStorageException("File storage error: " + e.getMessage(), e);

        }

    }

    public Boolean delete(String fileName) {

        if (fileName == null || fileName.isEmpty()) {
            throw new InvalidFileException("File name cannot be null or empty");
        }

        if (storageDirectory == null || storageDirectory.isEmpty()) {
            throw new FileStorageException("Storage directory is not configured");
        }

        File file = new File(storageDirectory, fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        if (!file.delete()) {
            throw new FileStorageException("Failed to delete file: " + fileName);
        }

        return true;
    }

    @Override
    public String getFileURL(String fileName) {
        return storageDirectory + "/" + fileName;
    }


}