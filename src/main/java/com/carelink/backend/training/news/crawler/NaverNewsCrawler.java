package com.carelink.backend.training.news.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NaverNewsCrawler {

    // usedUrls를 파라미터로 받음 (기존 그대로)
    public CrawledNews crawlOneByCategory(String categoryCode, Set<String> usedUrls) {
        try {
            String listUrl =
                    "https://news.naver.com/main/list.naver?mode=LSD&mid=sec&sid1=" + categoryCode;

            Document listDoc = Jsoup.connect(listUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(5000)
                    .get();

            // 여러 개 후보 기사 가져오기 (기존 그대로)
            Elements articleLinks = listDoc.select("ul.type06_headline li a");

            for (Element link : articleLinks) {
                String articleUrl = link.attr("href");

                // 이미 사용된 기사면 스킵 (기존 그대로)
                if (usedUrls.contains(articleUrl)) {
                    continue;
                }

                usedUrls.add(articleUrl);

                Document articleDoc = Jsoup.connect(articleUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        .timeout(5000)
                        .get();

                Element titleEl = articleDoc.selectFirst("#title_area span");
                Element contentEl = articleDoc.selectFirst("#dic_area");

                // 🔧 [추가] 썸네일(meta og:image) 추출
                Element thumbnailMeta =
                        articleDoc.selectFirst("meta[property=og:image]");
                String thumbnailImageUrl =
                        thumbnailMeta != null ? thumbnailMeta.attr("content") : null;

                if (titleEl == null || contentEl == null) {
                    continue; // 이 기사 스킵하고 다음 후보 크롤링
                }

                // 🔧 수정: CrawledNews에 썸네일 URL 포함
                return new CrawledNews(
                        titleEl.text(),
                        contentEl.text(),
                        thumbnailImageUrl
                );
            }

            return null; // 이 카테고리에서 새 기사 없음

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("뉴스 크롤링 실패", e);
        }
    }

}
