package com.nbnp.trip_to_go.controller;

import com.nbnp.trip_to_go.model.FolderType;
import com.nbnp.trip_to_go.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(@Qualifier("S3FileService") FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/user-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFileToUserAvatarFolder(@RequestParam("file") MultipartFile file) {
        var result = fileService.upload(file, FolderType.userAvatar);

        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not uploaded");
        }
    }

    @PostMapping( value = "/trip-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFileToTripAvatarFolder(@RequestParam("file") MultipartFile file) {
        var result = fileService.upload(file, FolderType.tripAvatar);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not uploaded");
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFile(@RequestParam String fileName) {
        if (fileService.delete(fileName)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found or could not be deleted");
        }
    }

    @GetMapping
    public ResponseEntity<String> getURL(@RequestParam String fileName) {
        var result = fileService.getFileURL(fileName);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}