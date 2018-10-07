package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DEPARTMENTS")
public class DepartmentEntity {

    @Id
    @Column(name = "DEPARTMENT_ID")
    @SequenceGenerator(name = "DEPARTMENTS_SEQ", sequenceName = "DEPARTMENTS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENTS_SEQ")
    private long departmentId;

    @Basic
    @Column(name = "DEPARTMENT_NAME", nullable = false)
    private String departmentName;

    @Basic
    @Column(name = "CITY")
    private String city;

    @Basic
    @Column(name = "FULL_ADDRESS")
    private String fullAddress;
}
