package ru.romanov.pastbin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Service
public class S3Service {
    @Value("${s3.bucketName}")
    private String bucketName;
    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void createBucket(String bucketName) {
        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        s3Client.createBucket(createBucketRequest);
    }

    public ListBucketsResponse listBuckets() {
        return s3Client.listBuckets();
    }

    public String uploadText(String text, Principal principal) throws S3Exception {
        String objectKey = System.currentTimeMillis() + "_" + principal.hashCode();
        byte[] contentBytes = text.getBytes(StandardCharsets.UTF_8);
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType("text/plain")
                .contentLength((long) contentBytes.length)
                .build(), RequestBody.fromBytes(contentBytes));

        System.out.println("Loading is complete");
        return objectKey;
    }
}
