package com.carelink.backend.training.news.filter;
import java.util.List;

public class NewsHardFilter {

    private static final List<String> HARD_BLOCK_KEYWORDS = List.of(
            // 날씨 / 기상
            //"한파", "폭염", "기온", "날씨", "태풍", "미세먼지", "특보", "경보",
            // 운세 / 미신
            "운세", "띠별", "사주",
            // 사망 / 부고
            "별세", "부고", "사망", "숨진", "영면",
            // 정치 / 사회 갈등
            "의혹", "논란", "공방", "여야", "국회", // "대통령", "정당",
            // 사건 / 사고
            "사고", "파열", "붕괴", "화재", "추락", "참사",
            // 종교 / 교회
            "목회", "목사", "신학",
            "장로", "예배",
            // 이외 네이버 뉴스에서 자주 나오는 일상적인 주제들
            "오늘의 운세"
    );

    public static boolean isBlocked(String title) {
        return HARD_BLOCK_KEYWORDS.stream()
                .anyMatch(title::contains);
    }
}
