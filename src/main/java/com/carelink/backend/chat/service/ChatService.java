package com.carelink.backend.chat.service;

import com.carelink.backend.chat.dto.AIChatResponseDto;
import com.carelink.backend.chat.dto.ChatRequestDto;
import com.carelink.backend.chat.dto.ChatRoomDto;
import com.carelink.backend.chat.dto.ConversationDto;
import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.medicine.repository.MedicineIntakeLogRepository;
import com.carelink.backend.userCondition.repository.UserConditionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MedicineIntakeLogRepository medicineIntakeLogRepository;
    private final UserConditionRepository userConditionRepository;
    @Value("${chat.api.url}")
    private String CHAT_API_URL;

    private final ChatRedisService chatRedisService;

    /** 응답 생성 */
    public String generateResponse(Long userId, ChatRequestDto chatRequestDto) {
        String question = chatRequestDto.getQuestion();

        // 응답 생성
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatRequestDto> requestEntity = new HttpEntity<>(chatRequestDto, headers);

        AIChatResponseDto aiChatResponseDto;

        try {
            aiChatResponseDto =
                    restTemplate
                            .exchange(CHAT_API_URL, HttpMethod.POST, requestEntity, AIChatResponseDto.class)
                            .getBody();

            if (aiChatResponseDto == null)
                throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API로부터 응답을 받아오지 못했습니다.");

        } catch (ResourceAccessException e) {
            log.error("외부 API 연결 에러 - " + e.getMessage());
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API 연결에 실패했습니다.");
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error("외부 API 에러 - " + e.getMessage());
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API에서 알 수 없는 오류가 발생했습니다.");
        }

        String response = aiChatResponseDto.getAnswer();

        // Redis에 저장
        chatRedisService.saveQnA(userId, question, response);

        return response;
    }

    /** 채팅방 조회 */
    public ChatRoomDto getChatRoom(Long userId) {
        // 약 복용 체크 여부
        Boolean isMedicineChecked = medicineIntakeLogRepository.existsByMedicineIntakeTime_UserMedicine_User_IdAndDate(userId, LocalDate.now());

        // 컨디션 체크 여부
        Boolean isConditionChecked = userConditionRepository.existsByUserIdAndDate(userId, LocalDate.now());

        // TODO: 퀴즈 여부

        // 채팅 내용
        List<ConversationDto> allConversationsByUserId = chatRedisService.getAllQnasByUserId(userId);

        return ChatRoomDto.builder()
                .isMedicineChecked(isMedicineChecked)
                .isConditionChecked(isConditionChecked)
                .isQuizChecked(true)
                .conversations(allConversationsByUserId).build();
    }

}
