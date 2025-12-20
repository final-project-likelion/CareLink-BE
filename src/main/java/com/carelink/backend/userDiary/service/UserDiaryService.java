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

    public List<MonthlyDiaryDto> getMonthlyUserDiary(Long userId, String year, String month) {
        LocalDate start = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
        LocalDate end = start.plusMonths(1);

        List<MonthlyDiaryDto> diaryDtos = new ArrayList<>();
        userDiaryRepository.findByDateBetweenAndUserId(userId, start, end).forEach(userDiary -> {
            MonthlyDiaryDto dto = MonthlyDiaryDto.builder().id(userDiary.getId())
                    .title(userDiary.getTitle())
                    .date(userDiary.getDate()).build();
            diaryDtos.add(dto);
        });

        return diaryDtos;
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
