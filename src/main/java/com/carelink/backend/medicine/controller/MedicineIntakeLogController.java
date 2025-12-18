package com.carelink.backend.medicine.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.medicine.dto.DailyMedicineIntakeCheckDto;
import com.carelink.backend.medicine.dto.DailyMedicineLogDto;
import com.carelink.backend.medicine.service.MedicineIntakeLogService;
import com.carelink.backend.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medicines/today")
public class MedicineIntakeLogController {

    private final MedicineIntakeLogService medicineIntakeLogService;

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getDailyMedicineLog(@AuthenticationPrincipal User user) {
        DailyMedicineLogDto dailyMedicineLog = medicineIntakeLogService.getDailyMedicineLog(user.getId());
        return ResponseEntity.ok()
                .body(BaseResponse.success("오늘 복용해야할 약 리스트를 정상적으로 불러왔습니다.", dailyMedicineLog));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<?>> checkDailyMedicineIntake(@AuthenticationPrincipal User user,
                                                                    @RequestBody DailyMedicineIntakeCheckDto dailyMedicineIntakeCheckDto) {
        medicineIntakeLogService.checkDailyMedicineIntake(user.getId(), dailyMedicineIntakeCheckDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약 복용 현황을 정상적으로 업데이트했습니다.", null));
    }

}
