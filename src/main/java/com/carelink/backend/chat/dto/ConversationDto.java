package com.carelink.backend.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationDto {

    private String question;

    private String answer;

}
