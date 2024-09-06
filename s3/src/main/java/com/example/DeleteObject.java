package com.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class DeleteObject {

	public static void main(String[] args) {
		final String usage = "Usage:\n" +
                "  <bucketName> <objectKey>\n\n" +
                "Where:\n" +
                "  bucketName - The Amazon S3 bucket name.\n" +
				"  objectKey - The object to delete (for example, book.pdf).";
		
		if (args.length != 2) {
			System.out.println(usage);
			System.exit(1);
		}
		
		String bucketName = args[0];
		String objectKey = args[1];
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder()
				.region(region)
				.build();
		deleteBucketObject(s3, bucketName, objectKey);
		s3.close();
	}
	
	 /**
     * Deletes a single object from the specified S3 bucket.
     * This method only supports deleting one object at a time.
     * 
     * @param s3         The S3Client used to interact with S3.
     * @param bucketName The name of the S3 bucket.
     * @param objectKey  The object to delete.
     */
	public static void deleteBucketObject(S3Client s3, String bucketName, String objectKey) {
		try {
			DeleteObjectRequest objectDeleteRequest = DeleteObjectRequest.builder()
					.bucket(bucketName)
					.key(objectKey)
					.build();
			s3.deleteObject(objectDeleteRequest);
			System.out.println("Object deleted!");
		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}
}
