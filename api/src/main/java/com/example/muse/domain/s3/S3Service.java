package com.example.muse.domain.s3;

import com.example.muse.global.common.exception.CustomS3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {
    private static final String FILE_PATH_PREFIX = "review/";
    private static final String IMAGE_URL_FORMAT = "https://%s.s3.%s.amazonaws.com/%s";
    private final S3Client s3Client;
    @Value("${BUCKET_NAME}")
    private String bucketName;
    @Value("${REGION}")
    private String region;

    public Map<String, String> uploadFile(MultipartFile file) {

        String s3Key = FILE_PATH_PREFIX + UUID.randomUUID() + "_" + file.getOriginalFilename();
        uploadFileToS3(file, s3Key);
        String imageUrl = String.format(IMAGE_URL_FORMAT, bucketName, region, s3Key);

        return Map.of(
                "imageUrl", imageUrl,
                "s3Key", s3Key
        );
    }

    private void uploadFileToS3(MultipartFile file, String s3Key) {

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new CustomS3Exception();
        }
    }

    public void deleteFile(String s3Key) {

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new CustomS3Exception();
        }
    }

}


