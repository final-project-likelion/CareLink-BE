package com.carelink.backend.medicine.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.medicine.dto.MedicineInfoDto;
import com.carelink.backend.medicine.service.MedicineService;
import com.carelink.backend.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<BaseResponse<?>> addMedicine(@Valid @RequestBody MedicineInfoDto medicineInfoDto,
                                                       @AuthenticationPrincipal User user) {
        Long id = medicineService.addMedicine(user, medicineInfoDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("약을 정상적으로 추가했습니다.", id));
    }

}
