package com.carelink.backend.medicine.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.medicine.dto.MedicineInfoDto;
import com.carelink.backend.medicine.dto.MedicineUpdateRequestDto;
import com.carelink.backend.medicine.dto.MedicineUpsertRequestDto;
import com.carelink.backend.medicine.service.MedicineService;
import com.carelink.backend.user.service.AuthService;
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
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<BaseResponse<?>> addMedicine(@Valid @RequestBody MedicineUpsertRequestDto medicineUpsertRequestDto,
                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = medicineService.addMedicine(authService.getCurrentUser(customUserDetails.getId()), medicineUpsertRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약을 정상적으로 추가했습니다.", id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getUserMedicines(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MedicineInfoDto> medicineInfoByUserId = medicineService.getMedicineInfoByUserId(customUserDetails.getId());
        return ResponseEntity.ok()
                .body(BaseResponse.success("약 목록을 정상적으로 불러왔습니다.", medicineInfoByUserId));
    }

    @DeleteMapping("/{medicineId}")
    public ResponseEntity<BaseResponse<?>> deleteMedicine(@PathVariable Long medicineId,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        medicineService.deleteMedicine(customUserDetails.getId(), medicineId);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약을 정상적으로 삭제했습니다.", null));
    }

    @PutMapping("/{medicineId}")
    public ResponseEntity<BaseResponse<?>> updateMedicineInfo(@PathVariable Long medicineId,
                                                              @RequestBody MedicineUpdateRequestDto updateRequestDto,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        MedicineInfoDto updatedMedicineInfo = medicineService.updateMedicineInfo(customUserDetails.getId(), medicineId, updateRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약 정보를 정상적으로 수정했습니다.", updatedMedicineInfo));
    }

}
