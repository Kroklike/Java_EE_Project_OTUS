package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PERMISSIONS")
public class PermissionEntity {
    @Id
    @Column(name = "PERMISSION_ID")
    private long permissionId;

    @Basic
    @Column(name = "PERMISSION_NAME")
    private String permissionName;

    @Basic
    @Column(name = "PERMISSION_CODE")
    private String permissionCode;

    @Basic
    @Column(name = "PERMISSION_DESCRIPTION")
    private String permissionDescription;
}
