package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "EMPLOYEES")
public class EmployeeEntity {

    @Id
    @Column(name = "EMPLOYEE_ID")
    @SequenceGenerator(name = "EMPLOYEES_SEQ", sequenceName = "EMPLOYEES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEES_SEQ")
    private long employeeId;

    @ManyToOne
    @JoinColumn(name = "POSITION_ID", nullable = false, foreignKey=@ForeignKey(name = "EMPLOYEE_POSITION_FK"))
    private PositionEntity positionEntity;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false, foreignKey=@ForeignKey(name = "EMPLOYEE_DEPARTMENT_FK"))
    private DepartmentEntity departmentEntity;

    @Basic
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Basic
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Basic
    @Column(name = "EMPLOYMENT_DATE", nullable = false)
    private LocalDate employmentDate;

    @Basic
    @Column(name = "SALARY", nullable = false)
    private BigDecimal salary;

    @Basic
    @Column(name = "BONUS_PERCENT", nullable = false)
    private BigDecimal bonusPercent;

    @Basic
    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Basic
    @Column(name = "TELEPHONE_NUMBER")
    private String telephoneNumber;

    @Basic
    @Column(name = "EMAIL")
    private String email;

    @Basic
    @Column(name = "DISMISSAL_DATE")
    private LocalDate dismissalDate;

    @OneToOne
    @JoinColumn(name = "USER_ID", foreignKey=@ForeignKey(name = "EMPLOYEE_USER_FK"))
    private UserEntity userEntity;
}
