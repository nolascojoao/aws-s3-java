package com.example;

import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public class ListBuckets {

	public static void main(String[] args) {
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder()
				.region(region)
				.build();
		listAllBuckets(s3);
	}
	
	public static void listAllBuckets(S3Client s3) {
		ListBucketsResponse response = s3.listBuckets();
		List<Bucket> bucketList = response.buckets();
		for (Bucket bucket: bucketList) {
			System.out.println("Bucket name " + bucket.name());
		}
	}
}
