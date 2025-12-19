package com.carelink.backend.medicine.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.medicine.dto.IntakeTimeAddRequestDto;
import com.carelink.backend.medicine.dto.MedicineInfoDto;
import com.carelink.backend.medicine.dto.MedicineUpdateRequestDto;
import com.carelink.backend.medicine.dto.MedicineUpsertRequestDto;
import com.carelink.backend.medicine.service.MedicineService;
import com.carelink.backend.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<BaseResponse<?>> addMedicine(@Valid @RequestBody MedicineUpsertRequestDto medicineUpsertRequestDto,
                                                       @AuthenticationPrincipal User user) {
        Long id = medicineService.addMedicine(user, medicineUpsertRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약을 정상적으로 추가했습니다.", id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getUserMedicines(@AuthenticationPrincipal User user) {
        List<MedicineInfoDto> medicineInfoByUserId = medicineService.getMedicineInfoByUserId(user.getId());
        return ResponseEntity.ok()
                .body(BaseResponse.success("약 목록을 정상적으로 불러왔습니다.", medicineInfoByUserId));
    }

    @DeleteMapping("/{medicineId}")
    public ResponseEntity<BaseResponse<?>> deleteMedicine(@PathVariable Long medicineId,
                                                          @AuthenticationPrincipal User user) {
        medicineService.deleteMedicine(user.getId(), medicineId);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약을 정상적으로 삭제했습니다.", null));
    }

    @PatchMapping("/{medicineId}")
    public ResponseEntity<BaseResponse<?>> modifyMedicineInfo(@PathVariable Long medicineId,
                                                          @RequestBody MedicineUpdateRequestDto updateRequestDto,
                                                          @AuthenticationPrincipal User user) {
        medicineService.updateMedicineInfo(user.getId(), medicineId, updateRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약 정보를 정상적으로 업데이트했습니다.", null));
    }

    @PostMapping("/{medicineId}")
    public ResponseEntity<BaseResponse<?>> addIntakeTime(@PathVariable Long medicineId,
                                                         @Valid @RequestBody IntakeTimeAddRequestDto intakeTimeAddRequestDto,
                                                         @AuthenticationPrincipal User user) {
        List<MedicineInfoDto.MedicineIntakeTimeDto> medicineIntakeTimeDtos = medicineService.addIntakeTime(user.getId(), medicineId, intakeTimeAddRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("복용시간을 정상적으로 추가했습니다.", medicineIntakeTimeDtos));
    }

}
