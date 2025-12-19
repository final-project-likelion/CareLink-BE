package com.carelink.backend.medicine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MedicineIntakeTime {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_medicine_id", nullable = false)
    private UserMedicine userMedicine;

    @OneToMany(mappedBy = "medicineIntakeTime")
    private List<MedicineIntakeLog> medicineIntakeLogs = new ArrayList<>();

    /** 복용시간 변경 */
    public void updateTime(LocalTime time) {
        if (time != null)
            this.time = time;
    }

}
