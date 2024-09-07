package com.example;

import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

public class ListObjects {

	public static void main(String[] args) {
		final String usage = "Usage:\n" +
                "  <bucketName>\n\n" +
                "Where:\n" +
                "  bucketName - The Amazon S3 bucket from which objects are read.\n";
		
		if (args.length != 1) {
			System.out.println(usage);;
			System.exit(1);
		}
		
		String bucketName = args[0];
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder()
				.region(region)
				.build();
		listBucketObjects(s3, bucketName);
		s3.close();
	}
	
	public static void listBucketObjects(S3Client s3, String bucketName) {
		try {
			ListObjectsRequest listObjects = ListObjectsRequest
					.builder()
					.bucket(bucketName)
					.build();
			ListObjectsResponse res = s3.listObjects(listObjects);
			List<S3Object> objects = res.contents();
			for (S3Object myValue : objects) {
				System.out.print("\n The name of the key is " + myValue.key());
				System.out.print("\n The object is " + calkb(myValue.size()) + " KBs");
				System.out.print("\n The owner is " + myValue.owner());
			}
		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}
	
	// Convert bytes to kbs.
	private static long calkb(Long val) {
		return val / 1024;
	}
}
