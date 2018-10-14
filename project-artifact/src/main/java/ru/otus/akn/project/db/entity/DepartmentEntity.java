package ru.otus.akn.project.db.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Data
@Entity
@Table(name = "DEPARTMENTS")
@ToString
@XmlRootElement(name = "department")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepartmentEntity {

    @Id
    @Column(name = "DEPARTMENT_ID")
    @SequenceGenerator(name = "DEPARTMENTS_SEQ", sequenceName = "DEPARTMENTS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENTS_SEQ")
    @XmlAttribute(required = true)
    private long departmentId;

    @Basic
    @Column(name = "DEPARTMENT_NAME", nullable = false)
    @XmlElement(required = true)
    private String departmentName;

    @Basic
    @Column(name = "CITY")
    private String city;

    @Basic
    @Column(name = "FULL_ADDRESS")
    private String fullAddress;
}
