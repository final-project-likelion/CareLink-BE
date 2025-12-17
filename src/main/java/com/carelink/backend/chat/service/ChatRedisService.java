package com.carelink.backend.chat.service;

import com.carelink.backend.chat.dto.ConversationDto;
import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static String generateKey(Long userId) {
        return "chat:messages:" + userId;
    }

    /** Redis에 채팅 저장 */
    public void saveQnA(Long userId, String question, String answer) {
        String key = generateKey(userId);

        ConversationDto conversationDto = ConversationDto.builder()
                .question(question).answer(answer).build();

        try {
            String json = objectMapper.writeValueAsString(conversationDto);
            redisTemplate.opsForList().rightPush(key, json);
        } catch (Exception e) {
            log.error("채팅 저장 오류 - " + e.getMessage());
            throw new BaseException(ErrorCode.CHAT_SAVE_ERROR);
        }
    }

    /** 사용자 id 기준으로 Redis에서 채팅 조회 */
    public List<ConversationDto> getAllQnasByUserId(Long userId) {
        String key = generateKey(userId);

        List<String> conversations = redisTemplate.opsForList().range(key, 0, -1);
        if (conversations == null || conversations.isEmpty()) return List.of();

        return conversations.stream()
                .map(c -> {
                    try {
                        return objectMapper.readValue(c, ConversationDto.class);
                    } catch (Exception e) {
                        throw new BaseException(ErrorCode.JSON_MAPPING_ERROR);
                    }
                }).toList();
    }

}
