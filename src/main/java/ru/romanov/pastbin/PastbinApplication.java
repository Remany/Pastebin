package ru.romanov.pastbin;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.romanov.pastbin.services.S3Service;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.util.List;

@SpringBootApplication
public class PastbinApplication {

	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(PastbinApplication.class, args);
	}
	@PostConstruct
	public void init() {
		s3Service.createBucket("some");

		ListBucketsResponse bucketsResponse = s3Service.listBuckets();
		List<Bucket> buckets = bucketsResponse.buckets();
		for (Bucket bucket : buckets) {
			System.out.println("Bucket: " + bucket.name());
		}
	}
}
