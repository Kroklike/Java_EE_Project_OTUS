package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PERMISSIONS")
public class PermissionEntity {
    @Id
    @Column(name = "PERMISSION_ID")
    @SequenceGenerator(name = "PERMISSIONS_SEQ", sequenceName = "PERMISSIONS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSIONS_SEQ")
    private long permissionId;

    @Basic
    @Column(name = "PERMISSION_NAME", nullable = false)
    private String permissionName;

    @Basic
    @Column(name = "PERMISSION_CODE", nullable = false)
    private String permissionCode;

    @Basic
    @Column(name = "PERMISSION_DESCRIPTION", nullable = false)
    private String permissionDescription;
}
