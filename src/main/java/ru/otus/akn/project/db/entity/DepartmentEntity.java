package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DEPARTMENTS")
public class DepartmentEntity {

    @Id
    @Column(name = "DEPARTMENT_ID")
    private long departmentId;

    @Basic
    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Basic
    @Column(name = "CITY")
    private String city;

    @Basic
    @Column(name = "FULL_ADDRESS")
    private String fullAddress;
}
