package com.carelink.backend.user;

public enum Category {

    HEALTH("건강/병원"),
    WELFARE_POLICY("복지/정책"),
    PETS("반려동물"),
    SOCIETY("사회/시사"),
    PLANTS("식물/원예"),
    FOOD("음식/요리"),
    TRAVEL("여행"),
    HOBBY_CULTURE("취미/문화");

    private String label;

    Category(String label) {
        this.label = label;
    }
}
