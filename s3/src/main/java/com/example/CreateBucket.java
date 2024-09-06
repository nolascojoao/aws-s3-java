package com.example;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

public class CreateBucket {

	public static void main(String[] args) {
		final String usage = "bucketName - The name of the bucket to create. The bucket name must be unique, or an error occurs.";
		
		if (args.length != 1) {
			System.out.println(usage);
			System.exit(1);
		}
		
		String bucketName = args[0];
		System.out.format("Creating a bucket named %s\n", bucketName);
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder()
				.region(region)
				.build();
		
		createBucket(s3, bucketName);
		s3.close();
	}
	
	public static void createBucket(S3Client s3Client, String bucketName) {
		try {
			S3Waiter s3Waiter = s3Client.waiter();
			CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
					.bucket(bucketName)
					.build();
			s3Client.createBucket(bucketRequest);
			HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
					.bucket(bucketName)
					.build();
			
			// Wait until the buckeHeadBucketResponserint out the response.
			WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
			waiterResponse.matched().response().ifPresent(System.out::println);
			System.out.println(bucketName + " is ready");
		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}
}
