package com.carelink.backend.userDiary.service;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.global.service.FileService;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.userDiary.dto.DiaryCreateRequestDto;
import com.carelink.backend.userDiary.dto.MonthlyDiaryDto;
import com.carelink.backend.userDiary.dto.UserDiaryDto;
import com.carelink.backend.userDiary.entity.UserDiary;
import com.carelink.backend.userDiary.repository.UserDiaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDiaryService {

    private final FileService fileService;
    private final UserDiaryRepository userDiaryRepository;

    @Transactional
    public Long createUserDiary(User user, DiaryCreateRequestDto diaryCreateRequestDto) {
        if (userDiaryRepository.existsByUserIdAndDate(user.getId(), LocalDate.now()))
            throw new BaseException(ErrorCode.USER_DIARY_ALREADY_EXISTS);

        String imageUrl = fileService.uploadFile(diaryCreateRequestDto.getImage());

        UserDiary userDiary = UserDiary.builder()
                .title(diaryCreateRequestDto.getTitle())
                .date(LocalDate.now())
                .user(user)
                .imageUrl(imageUrl).build();

        userDiaryRepository.save(userDiary);

        return userDiary.getId();
    }

    public MonthlyDiaryDto getMonthlyUserDiary(Long userId, String year, String month) {
        LocalDate start = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
        LocalDate end = start.plusMonths(1);

        List<MonthlyDiaryDto.DiaryDto> diaryDtos = new ArrayList<>();
        userDiaryRepository.findByDateBetweenAndUserId(userId, start, end).forEach(userDiary -> {
            MonthlyDiaryDto.DiaryDto dto = MonthlyDiaryDto.DiaryDto.builder()
                    .id(userDiary.getId())
                    .title(userDiary.getTitle())
                    .date(userDiary.getDate()).build();
            diaryDtos.add(dto);
        });

        int count = diaryDtos.size();
        String message;
        if (count <= 5)
            message = "짧게라도 기록해 주신 마음이 정말 소중해요. 한 줄 한 줄이 기억을 지키는 큰 도움이 됩니다. 오늘의 생각을 남겨 주셔서 감사합니다.";
        else if (count > 6 && count <= 15)
            message = "일기를 꾸준히 써 오고 계세요. 정말 대단합니다! 기억을 챙기는 좋은 습관이 만들어지고 있어요. 차분하게 잘 이어가고 계십니다. 꾸준한 기록이 기억력 유지에 큰 도움이 됩니다.";
        else
            message = "기억을 지키는 훌륭한 습관을 잘 유지하고 계십니다. 이만큼 꾸준히 하신 건 쉽지 않은 일이죠! 스스로를 잘 돌보고 계신 모습이 느껴져요.";

        return MonthlyDiaryDto.builder()
                .message(message)
                .diaries(diaryDtos).build();
    }

    public UserDiaryDto getUserDiary(Long userId, Long diaryId) {
        UserDiary userDiary = userDiaryRepository.findByUserIdAndId(userId, diaryId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_DIARY_NOT_FOUND));

        return UserDiaryDto.builder()
                .id(userDiary.getId())
                .title(userDiary.getTitle())
                .localDate(LocalDate.now())
                .imageUrl(userDiary.getImageUrl()).build();
    }

}
