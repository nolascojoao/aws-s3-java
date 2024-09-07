package com.example;

import java.util.List;
import java.util.stream.Collectors;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

public class DeleteMultiObjects {

    public static void main(String[] args) {
        final String usage = "Usage:\n" +
                "  <bucketName>\n\n" +
                "Where:\n" +
                "  bucketName - The Amazon S3 bucket name.";
        
        if (args.length != 1) {
            System.out.println(usage);
            System.exit(1);
        }

        String bucketName = args[0];
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();

        deleteBucketObjects(s3, bucketName);
        s3.close();
    }

    public static void deleteBucketObjects(S3Client s3, String bucketName) {
        try {
            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsV2Response listObjectsResponse = s3.listObjectsV2(listObjectsRequest);
            
            // Collect objects to delete
            List<ObjectIdentifier> objectsToDelete = listObjectsResponse.contents().stream()
                    .map(S3Object::key)
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .collect(Collectors.toList());
            
            if (!objectsToDelete.isEmpty()) {
                // Prepare delete request
                Delete delete = Delete.builder()
                        .objects(objectsToDelete)
                        .build();
                DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                        .bucket(bucketName)
                        .delete(delete)
                        .build();

                // Delete the objects
                s3.deleteObjects(deleteObjectsRequest);
                System.out.println("Objects deleted!");
            } else {
                System.out.println("No objects found to delete.");
            }
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
