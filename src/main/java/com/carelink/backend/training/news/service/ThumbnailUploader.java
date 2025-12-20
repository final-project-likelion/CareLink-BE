package com.carelink.backend.training.news.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ThumbnailUploader {

    private final AmazonS3 amazonS3;

    @Value("${AWS_BUCKET}")
    private String bucketName;

    public String uploadFromUrl(String imageUrl) {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {

            String key = "news-thumbnail/" + UUID.randomUUID() + ".jpg";

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            amazonS3.putObject(bucketName, key, inputStream, metadata);

            return amazonS3.getUrl(bucketName, key).toString();

        } catch (Exception e) {
            // 썸네일 실패는 전체 크롤링 실패로 만들지 않음
            return null;
        }
    }
}
