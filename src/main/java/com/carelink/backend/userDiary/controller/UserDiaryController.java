package com.carelink.backend.userDiary.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.userDiary.dto.DiaryCreateRequestDto;
import com.carelink.backend.userDiary.dto.MonthlyDiaryDto;
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

    @PostMapping
    public ResponseEntity<BaseResponse<?>> createUserDiary(@AuthenticationPrincipal User user,
                                                           @Valid @ModelAttribute DiaryCreateRequestDto diaryCreateRequestDto) {
        Long userDiaryId = userDiaryService.createUserDiary(user, diaryCreateRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("일기를 정상적으로 저장했습니다.", userDiaryId));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getMonthlyUserDiary(@AuthenticationPrincipal User user,
                                                               @RequestParam String year,
                                                               @RequestParam String month) {
        List<MonthlyDiaryDto> monthlyUserDiary = userDiaryService.getMonthlyUserDiary(user.getId(), year, month);
        return ResponseEntity.ok()
                .body(BaseResponse.success(String.format("%s년 %s월의 일기를 정상적으로 불러왔습니다.", year, month), monthlyUserDiary));
    }

}
