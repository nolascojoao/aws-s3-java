package com.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class PutObject {

	public static void main(String[] args) {
		final String usage = "Usage:\n" +
                "  <bucketName> <objectKey> <objectPath>\n\n" +
                "Where:\n" +
                "  bucketName - The Amazon S3 bucket to upload an object into.\n" +
                "  objectKey - The object to upload (for example, book.pdf).\n" +
                "  objectPath - The path where the file is located (for example, C:/AWS/book2.pdf).\n";
		
		if (args.length != 3) {
			System.out.println(usage);
			System.exit(1);
		}
		
		String bucketName = args[0];
		String objectKey = args[1];
		String objectPath = args[2];
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder()
				.region(region)
				.build();
		putS3Object(s3, bucketName, objectKey, objectPath);
		s3.close();
	}
	
	public static void putS3Object(S3Client s3, String bucketName, String objectKey, String objectPath) {
		try {
			Map<String, String> metadata = new HashMap<>();
			metadata.put("x-amz-meta-myVal", "test");
			PutObjectRequest putOb = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(objectKey)
					.metadata(metadata)
					.build();
			s3.putObject(putOb, RequestBody.fromFile(new File(objectPath)));
			System.out.println("Successfully placed " + objectKey + " into bucket " + bucketName);
		} catch (S3Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
