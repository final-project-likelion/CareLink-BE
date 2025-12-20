package com.carelink.backend.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 s3Client;

    @Value("${aws.bucket}")
    public String AWS_BUCKET;

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            s3Client.putObject(AWS_BUCKET, fileName, file.getInputStream(), metadata);
            return s3Client.getUrl(AWS_BUCKET, fileName).toString();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.IMAGE_UPLOAD_ERROR);
        }
    }

}
