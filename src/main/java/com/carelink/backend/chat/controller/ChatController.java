package com.carelink.backend.chat.controller;

import com.carelink.backend.chat.dto.ChatRequestDto;
import com.carelink.backend.chat.dto.ChatRoomDto;
import com.carelink.backend.chat.service.ChatService;
import com.carelink.backend.chat.service.SttService;
import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatController {

    private final SttService sttService;
    private final ChatService chatService;

    @PostMapping("/voice")
    public ResponseEntity<BaseResponse<?>> speechToText(@RequestPart("file") MultipartFile audioFile) {
        String text = sttService.speechToText(audioFile);
        return ResponseEntity.ok()
                .body(BaseResponse.success(text));
    }

    @PostMapping("/chat")
    public ResponseEntity<BaseResponse<?>> generateResponse(@Valid @RequestBody ChatRequestDto chatRequestDto,
                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String response = chatService.generateResponse(customUserDetails.getId(), chatRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getChatRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ChatRoomDto chatRoom = chatService.getChatRoom(customUserDetails.getId());
        return ResponseEntity.ok()
                .body(BaseResponse.success("채팅방을 정상적으로 불러왔습니다.", chatRoom));
    }

}
