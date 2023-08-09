package ru.romanov.pastbin.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    private S3Client s3Client;
    @InjectMocks
    private S3Service s3Service;
    private final String bucketName = "some";
    private final String objectKey = "someKey";

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
    void shouldGettingTextFromS3() {
        byte[] expectedBytes = {1, 2, 3, 4, 5};
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes.fromInputStream(
                GetObjectResponse.builder().build(), inputStream);

        when(s3Client.getObject(any(GetObjectRequest.class),
                eq(ResponseTransformer.toBytes())))
                .thenReturn(responseBytes);

        String expectedString = new String(expectedBytes, StandardCharsets.UTF_8);
        String result = s3Service.getTextFromS3(objectKey);

        assertEquals(expectedString, result);
    }

    @Test
    void deleteObjectFromS3() {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        DeleteObjectResponse deleteObjectResponse = DeleteObjectResponse.builder().build();
        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(deleteObjectResponse);

        s3Service.deleteObjectFromS3(objectKey);

        verify(s3Client, times(1)).deleteObject(deleteObjectRequest); // TODO не работает, исправить
    }
}