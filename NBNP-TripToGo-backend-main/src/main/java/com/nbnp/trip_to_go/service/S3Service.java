package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.exception.InvalidFileException;
import com.nbnp.trip_to_go.exception.S3BucketException;
import com.nbnp.trip_to_go.model.FolderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service("S3FileService")
public class S3Service implements FileService {

    @Value("${aws.public.bucket}")
    private String publicBucket;

    @Value("${aws.private.bucket}")
    private String privateBucket;

    @Value("${file.extensions}")
    private String[] allowedExtensions;

    @Value("${file.maxFileSize}")
    private long maxFileSizeInMB;
    private final S3Client s3Client;


    public S3Service(@Value("${aws.access.key}") String accessKey,
                     @Value("${aws.secret.key}") String secretKey) {
        this.s3Client = S3Client.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();


    }

    @Override
    public String upload(MultipartFile file, FolderType folderType) {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new InvalidFileException("File name cannot be null or empty");
            }

            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!Arrays.asList(allowedExtensions).contains(extension)) {
                throw new IllegalArgumentException("The file must be one of " + Arrays.toString(allowedExtensions));
            }

            if (file.getSize() > (maxFileSizeInMB * 1024 * 1024)) {
                throw new IllegalArgumentException("The file size must be less than " + maxFileSizeInMB + " MB");
            }

            String uniqueName = UUID.randomUUID().toString().replace("-", "");
            String key = folderType + "/" + uniqueName + "." + extension;


            uploadToPublicBucket(file, key);
            uploadToPrivateBucket(file,key);

            return key;

    }


    @Override
    public Boolean delete(String fileName) {
        try {
            deleteFromPublicBucket(fileName);
            return true;
        } catch (NoSuchKeyException e) {
            throw new S3BucketException("File not found: " + fileName);
        } catch (Exception e) {
            throw new S3BucketException("Error deleting file: " + e.getMessage());
        }
    }


    private void deleteFromPrivateBucket(String fileName) {
        s3Client.headObject(HeadObjectRequest.builder()
                .bucket(privateBucket)
                .key(fileName)
                .build());

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(privateBucket)
                .key(fileName)
                .build());


    }

    private void deleteFromPublicBucket(String fileName) {
        s3Client.headObject(HeadObjectRequest.builder()
                .bucket(publicBucket)
                .key(fileName)
                .build());

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(publicBucket)
                .key(fileName)
                .build());


    }

    private void uploadToPrivateBucket(MultipartFile file, String key) {
        try {

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(privateBucket)
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
        }
        catch (IOException e) {
            throw new S3BucketException("Error uploading file to private bucket: " + e.getMessage());

        }
    }

    private void uploadToPublicBucket(MultipartFile file, String key) {
        try {

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(publicBucket)
                            .key(key)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
        }
        catch (IOException e) {
            throw new S3BucketException("Error uploading file to public bucket: " + e.getMessage());

        }
    }

    @Override
    public String getFileURL(String fileName) {
        return "https://" + publicBucket + ".s3.amazonaws.com/" + fileName;
    }
}
