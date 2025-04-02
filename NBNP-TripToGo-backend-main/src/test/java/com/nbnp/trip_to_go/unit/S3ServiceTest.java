package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.exception.S3BucketException;
import com.nbnp.trip_to_go.model.FolderType;
import com.nbnp.trip_to_go.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    private S3Service s3Service;

    private final String publicBucket = "public-bucket";
    private final String privateBucket = "private-bucket";
    private final String[] allowedExtensions = {"jpg", "png", "jpeg"};
    private final long maxFileSizeInMB = 5;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        s3Service = new S3Service("mockAccessKey", "mockSecretKey");

        setField(s3Service, "publicBucket", publicBucket);
        setField(s3Service, "privateBucket", privateBucket);
        setField(s3Service, "allowedExtensions", allowedExtensions);
        setField(s3Service, "maxFileSizeInMB", maxFileSizeInMB);
        setField(s3Service, "s3Client", s3Client);
    }

    private void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void upload_ValidFile_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test content".getBytes());
        FolderType folderType = FolderType.userAvatar;

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String result = s3Service.upload(file, folderType);

        assertNotNull(result);
        verify(s3Client, times(2)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void upload_FileTooLarge_ThrowsException() {
        byte[] largeFile = new byte[(int) (maxFileSizeInMB * 1024 * 1024 + 100)];
        MockMultipartFile file = new MockMultipartFile("file", "large.jpg", "image/jpeg", largeFile);

        assertThrows(IllegalArgumentException.class, () -> s3Service.upload(file, FolderType.userAvatar));
    }

    @Test
    void upload_InvalidFileExtension_ThrowsException() {
        MockMultipartFile file = new MockMultipartFile("file", "test.exe", "application/octet-stream", "test content".getBytes());

        assertThrows(IllegalArgumentException.class, () -> s3Service.upload(file, FolderType.userAvatar));
    }

    @Test
    void delete_ExistingFile_Success() {
        String fileName = "folder/test.jpg";

        // Мокінг headObject для імітації існування файлу
        when(s3Client.headObject(any(HeadObjectRequest.class)))
                .thenReturn(HeadObjectResponse.builder().build());
        // Мокінг deleteObject, який повертає DeleteObjectResponse
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(DeleteObjectResponse.builder().build());

        assertTrue(s3Service.delete(fileName));
        verify(s3Client, times(1)).headObject(any(HeadObjectRequest.class));
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void delete_NonExistingFile_ThrowsException() {
        String fileName = "folder/missing.jpg";

        when(s3Client.headObject(any(HeadObjectRequest.class))).thenThrow(NoSuchKeyException.class);

        assertThrows(S3BucketException.class, () -> s3Service.delete(fileName));
    }
}