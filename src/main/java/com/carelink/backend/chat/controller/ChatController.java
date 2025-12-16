package com.carelink.backend.chat.controller;

import com.carelink.backend.chat.service.SttService;
import com.carelink.backend.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatController {

    private final SttService sttService;

    @PostMapping("/voice")
    public ResponseEntity<BaseResponse<?>> speechToText(@RequestPart("file") MultipartFile audioFile) {
        String text = sttService.speechToText(audioFile);
        return ResponseEntity.ok()
                .body(BaseResponse.success(text));
    }

}
