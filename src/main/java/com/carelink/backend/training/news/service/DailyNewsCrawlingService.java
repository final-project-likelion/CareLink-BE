package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.ai.AiNewsFilterClient;
import com.carelink.backend.training.news.crawler.CrawledNews;
import com.carelink.backend.training.news.crawler.NaverNewsCrawler;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.filter.NewsHardFilter;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.user.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyNewsCrawlingService {

    private final NaverNewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final NewsSaveService newsSaveService;
    private final NewsAnswerGenerateService answerGenerateService;
    private final AiNewsFilterClient aiNewsFilterClient;


    public void crawlDailyNews() {

        Set<String> usedUrls = new HashSet<>();

        for (Category category : Category.values()) {

            boolean saved = false;
            int tryCount = 0;

            while (!saved && tryCount < 15) {
                tryCount++;

                CrawledNews crawled;
                try {
                    crawled = crawler.crawlOneByCategory(
                            mapToNaverCode(category),
                            usedUrls
                    );
                } catch (Exception e) {
                    log.warn("크롤링 실패 - category={}, try={}", category, tryCount);
                    continue;
                }

                if (crawled == null) break;

                String title = crawled.title();
                String content = crawled.content();

                log.error("🟢 크롤링 성공 title={}", title);

                if (NewsHardFilter.isBlocked(title)) {
                    log.error("❌ 하드필터 탈락");
                    continue;
                }

//
//                boolean usable = true;
//                try {
//                    usable = aiNewsFilterClient.isUsable(title, content);
//                    log.error("🟡 AI 필터 결과={}", usable);
//                } catch (Exception e) {
//                    log.error("⚠️ AI 필터 예외 → 통과 처리", e);
//                }
//                if (!usable) continue;


                if (newsRepository.existsByTitle(title)) continue;
                log.error("🟣 저장 조건 통과");

                log.error("🚨 SAVE 직전 도달");
                // ✅ 1단계: 뉴스 저장 (여기서 커밋됨)
                News news =
                        newsSaveService.saveNewsOnly(
                                crawled, title, content, category
                        );
                log.error("🚨 SAVE 직후 도달 id={}", news.getId());

                log.error("🚨 엔티티 생성 완료");

                // ✅ 2단계: 정답 생성 (실패해도 영향 없음) < 하 근데 정답 생성 실패하면 당연히 안 되는 거 아님? ;;
                try {
                    answerGenerateService.generateAnswers(news);
                } catch (Exception e) {
                    log.warn("정답 생성 전체 실패 - 무시됨", e);
                }

                saved = true;
            }
        }
    }

    private String mapToNaverCode(Category category) {
        return switch (category) {
            case HEALTH, PETS, PLANTS, FOOD, TRAVEL, HOBBY_CULTURE -> "103";
            case WELFARE_POLICY -> "100";
            case SOCIETY -> "102";
        };
    }
}
