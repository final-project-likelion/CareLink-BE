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
            "의혹", "논란", "공방", "여야", "국회", "파면", // "대통령", "정당",
            // 사건 / 사고
            "사고", "파열", "붕괴", "화재", "추락", "참사",
            // 종교 / 교회
            "목회", "목사", "신학",
            "장로", "예배",
            // 이외 직접 확인하면서 추가
            "오늘의 운세", "김건희", "피해", "통일교", "체포", "과로사",
            "내일 아침", "내일 밤", "아침 기온", "낮 기온", "성인용품", "성인 용품",
            "체감 온도", "체감온도", "눈비", "암 환자", "출마", "선거", "의원", "서울시장", "이재명",
            "더불어민주당", "국민의힘", "비뇨의학과"
    );

    public static boolean isBlocked(String title) {
        return HARD_BLOCK_KEYWORDS.stream()
                .anyMatch(title::contains);
    }
}
