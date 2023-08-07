package ru.romanov.pastbin.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    private S3Client s3Client;
    @InjectMocks
    private S3Service s3Service;
    private final String bucketName = "some";

    @Test
    void shouldCreatedBucket() {
        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();
        s3Service.createBucket(bucketName);
        verify(s3Client, times(1)).createBucket(createBucketRequest);
    }

    @Test
    void shouldUploadedText() {
        // TODO
    }

    @Test
    void getTextFromS3() {
    }

    @Test
    void deleteObjectFromS3() {
    }
}