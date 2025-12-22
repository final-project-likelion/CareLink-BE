package com.carelink.backend.userDiary.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.user.service.AuthService;
import com.carelink.backend.userDiary.dto.DiaryCreateRequestDto;
import com.carelink.backend.userDiary.dto.MonthlyDiaryDto;
import com.carelink.backend.userDiary.dto.UserDiaryDto;
import com.carelink.backend.userDiary.service.UserDiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class UserDiaryController {

    private final UserDiaryService userDiaryService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<BaseResponse<?>> createUserDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @Valid @ModelAttribute DiaryCreateRequestDto diaryCreateRequestDto) {
        Long userDiaryId = userDiaryService.createUserDiary(authService.getCurrentUser(customUserDetails.getId()), diaryCreateRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("일기를 정상적으로 저장했습니다.", userDiaryId));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getMonthlyUserDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                               @RequestParam String year,
                                                               @RequestParam String month) {
        MonthlyDiaryDto monthlyUserDiary = userDiaryService.getMonthlyUserDiary(customUserDetails.getId(), year, month);
        return ResponseEntity.ok()
                .body(BaseResponse.success(String.format("%s년 %s월의 일기를 정상적으로 불러왔습니다.", year, month), monthlyUserDiary));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<BaseResponse<?>> getUserDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                               @PathVariable Long diaryId) {
        UserDiaryDto userDiary = userDiaryService.getUserDiary(customUserDetails.getId(), diaryId);
        return ResponseEntity.ok()
                .body(BaseResponse.success("일기를 정상적으로 불러왔습니다.", userDiary));
    }

}
