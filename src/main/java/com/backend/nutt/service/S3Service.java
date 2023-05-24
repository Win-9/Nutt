package com.backend.nutt.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile) throws IOException {
        // 중복되지 않는 파일이름 설정
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        // 파일 사이즈 검사
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        // 업로드
        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
