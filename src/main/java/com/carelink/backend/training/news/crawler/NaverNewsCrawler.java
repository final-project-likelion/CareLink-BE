package com.carelink.backend.training.news.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NaverNewsCrawler {

    // usedUrls를 파라미터로 받음
    public CrawledNews crawlOneByCategory(String categoryCode, Set<String> usedUrls) {
        try {
            String listUrl =
                    "https://news.naver.com/main/list.naver?mode=LSD&mid=sec&sid1=" + categoryCode;

            Document listDoc = Jsoup.connect(listUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(5000)
                    .get();

            // 첫 번째가 아니라 여러 개 후보를 가져옴 -> 중복 없는 크롤링을 구현하기 위함!
            Elements articleLinks = listDoc.select("ul.type06_headline li a");

            for (Element link : articleLinks) {
                String articleUrl = link.attr("href");

                // 이미 사용된 기사면 스킵
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

                if (titleEl == null || contentEl == null) {
                    continue; // 이 기사 스킵하고 다음 후보 크롤링하기
                }

                return new CrawledNews(
                        titleEl.text(),
                        contentEl.text()
                );
            }

            return null; // 이 카테고리에서 새 기사 없음

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("뉴스 크롤링 실패", e);
        }
    }

    public record CrawledNews(String title, String content) {}
}
