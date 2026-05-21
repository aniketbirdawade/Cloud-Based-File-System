package com.Cloud.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileService {

    @Autowired
    private S3Client s3Client;

    private final String bucketName = "cloud-based-file-system";

    public String uploadFile(MultipartFile file) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getOriginalFilename())
                    .build();

            s3Client.putObject(request,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            return "Uploaded Successfully";

        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
//--------------------------------------------------------------
    
    public byte[] downloadFile(String fileName) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object =
                    s3Client.getObject(request);

            return s3Object.readAllBytes();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
