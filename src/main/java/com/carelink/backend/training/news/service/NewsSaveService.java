package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.ai.AiSummaryClient;
import com.carelink.backend.training.news.crawler.CrawledNews;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.user.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsSaveService {

    private final NewsRepository newsRepository;
    private final ThumbnailUploader thumbnailUploader;
    private final AiSummaryClient aiSummaryClient;

    /**
     * 뉴스 저장 전용
     * - 반드시 DB에 커밋됨
     * - AI/S3 실패해도 News는 저장
     */
    @Transactional
    public News saveNewsOnly(
            CrawledNews crawled,
            String title,
            String content,
            Category category
    ) {

        News news = new News(title, content, category);

        // 1. 썸네일 업로드 (실패 허용)
        try {
            if (crawled.thumbnailImageUrl() != null) {
                String s3Url =
                        thumbnailUploader.uploadFromUrl(
                                crawled.thumbnailImageUrl()
                        );
                if (s3Url != null) {
                    news.updatePreview(null, s3Url);
                }
            }
        } catch (Exception e) {
            log.warn("썸네일 업로드 실패 - 무시", e);
        }

        // 2. 한 줄 요약 (실패 허용)
        try {
            String preview =
                    aiSummaryClient.generatePreviewSummary(content);
            news.updatePreview(preview);
        } catch (Exception e) {
            log.warn("미리보기 요약 실패 - 무시", e);
        }

        // 3. 뉴스 저장 (여기서 INSERT)
        return newsRepository.save(news);
    }
}
