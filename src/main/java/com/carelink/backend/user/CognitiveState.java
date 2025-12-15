package com.carelink.backend.user;

public enum CognitiveState {

    SUSPECTED("치매 의심"),
    MILD("치매 경도"),
    SEVERE("치매 중증"),
    IMPAIRMENT("인지장애");

    private final String label;

    CognitiveState(String label) {
        this.label = label;
    }

}
