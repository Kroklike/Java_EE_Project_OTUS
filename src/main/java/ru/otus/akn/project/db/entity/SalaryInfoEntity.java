package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "SALARY_INFO")
public class SalaryInfoEntity {

    @Id
    @Column(name = "SALARY_INFO_ID")
    private long salaryInfoId;

    @Basic
    @Column(name = "SALARY")
    private BigDecimal salary;

    @Basic
    @Column(name = "BONUS_PERCENT")
    private BigDecimal bonusPercent;
}
