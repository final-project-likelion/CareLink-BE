package com.carelink.backend.medicine.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({
        "morningMedicines",
        "noonMedicines",
        "eveningMedicines"
})
public class DailyMedicineLogDto {

    private List<DailyMedicineInfoDto> morningMedicines;

    private List<DailyMedicineInfoDto> noonMedicines;

    private List<DailyMedicineInfoDto> eveningMedicines;

    /** 개별 약 정보 + 복용 여부 */
    @Data
    @Builder
    public static class DailyMedicineInfoDto {
        private Long id;

        private String name;

        private String time;

        private Boolean isTaken;
    }

}
