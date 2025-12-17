package com.carelink.backend.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatRoomDto {

    // 약 복용 확인 여부
    private Boolean isMedicineChecked;

    // 컨디션 확인 여부
    private Boolean isConditionChecked;

    // 퀴즈 풀었는지 여부
    private Boolean isQuizChecked;

    private List<ConversationDto> conversations;

}
