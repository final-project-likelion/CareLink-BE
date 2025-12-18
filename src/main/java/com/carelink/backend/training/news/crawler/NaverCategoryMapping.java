package com.carelink.backend.training.news.crawler;

import com.carelink.backend.user.Category;

import java.util.List;

// 🆕 추가
public enum NaverCategoryMapping {

    HEALTH(Category.HEALTH, List.of("102", "103")),
    WELFARE_POLICY(Category.WELFARE_POLICY, List.of("100", "102")),
    PETS(Category.PETS, List.of("103")),
    SOCIETY(Category.SOCIETY, List.of("102", "100")),
    PLANTS(Category.PLANTS, List.of("103")),
    FOOD(Category.FOOD, List.of("103")),
    TRAVEL(Category.TRAVEL, List.of("103")),
    HOBBY_CULTURE(Category.HOBBY_CULTURE, List.of("103", "106"));

    private final Category category;
    private final List<String> naverCodes;

    NaverCategoryMapping(Category category, List<String> naverCodes) {
        this.category = category;
        this.naverCodes = naverCodes;
    }

    public Category getCategory() {
        return category;
    }

    public List<String> getNaverCodes() {
        return naverCodes;
    }
}
