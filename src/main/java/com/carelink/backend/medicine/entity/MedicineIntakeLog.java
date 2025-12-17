package com.carelink.backend.medicine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"medicine_time_id", "date"})}
)
public class MedicineIntakeLog {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_intake_time_id", nullable = false)
    private MedicineIntakeTime medicineIntakeTime;

}
