package ru.otus.akn.project.db.entity;

import lombok.Data;
import lombok.ToString;
import ru.otus.akn.project.xml.adapter.DateTimeAdapter;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.otus.akn.project.xml.adapter.DateTimeAdapter.DATE_FORMAT;

@Data
@Entity
@Table(name = "EMPLOYEES")
@ToString
@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeEntity {

    @Id
    @Column(name = "EMPLOYEE_ID")
    @SequenceGenerator(name = "EMPLOYEES_SEQ", sequenceName = "EMPLOYEES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEES_SEQ")
    @XmlAttribute(required = true)
    private Long employeeId;

    @ManyToOne
    @JoinColumn(name = "POSITION_ID", nullable = false, foreignKey = @ForeignKey(name = "EMPLOYEE_POSITION_FK"))
    @XmlElement(required = true, name = "position")
    @JsonbProperty("position")
    private PositionEntity positionEntity;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false, foreignKey = @ForeignKey(name = "EMPLOYEE_DEPARTMENT_FK"))
    @XmlElement(required = true, name = "department")
    @JsonbProperty("department")
    private DepartmentEntity departmentEntity;

    @Basic
    @Column(name = "FIRST_NAME", nullable = false)
    @XmlElement(required = true)
    private String firstName;

    @Basic
    @Column(name = "LAST_NAME", nullable = false)
    @XmlElement(required = true)
    private String lastName;

    @Basic
    @Column(name = "EMPLOYMENT_DATE", nullable = false)
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonbDateFormat(value = DATE_FORMAT)
    private LocalDate employmentDate;

    @Basic
    @Column(name = "BIRTHDAY_DATE", nullable = false)
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonbDateFormat(value = DATE_FORMAT)
    private LocalDate birthdayDate;

    @Basic
    @Column(name = "SALARY", nullable = false)
    @XmlElement(required = true)
    private BigDecimal salary;

    @Basic
    @Column(name = "BONUS_PERCENT", nullable = false)
    @XmlElement(required = true)
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
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonbDateFormat(value = DATE_FORMAT)
    private LocalDate dismissalDate;

    @OneToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "EMPLOYEE_USER_FK"))
    private UserEntity userEntity;
}
