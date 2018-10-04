package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "EMPLOYEES")
public class EmployeeEntity implements Serializable {

    @Id
    @Column(name = "EMPLOYEE_ID")
    private long employeeId;

    @ManyToOne
    @JoinColumn(name="POSITION_ID", nullable=false)
    private PositionEntity positionEntity;

    @ManyToOne
    @JoinColumn(name="DEPARTMENT_ID", nullable=false)
    private DepartmentEntity departmentEntity;

    @Basic
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Basic
    @Column(name = "LAST_NAME")
    private String lastName;

    @Basic
    @Column(name = "EMPLOYMENT_DATE")
    private LocalDate employmentDate;

    @ManyToOne
    @JoinColumn(name="SALARY_INFO_ID", nullable=false)
    private SalaryInfoEntity salaryInfoEntity;

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

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
    private UserEntity userEntity;
}
