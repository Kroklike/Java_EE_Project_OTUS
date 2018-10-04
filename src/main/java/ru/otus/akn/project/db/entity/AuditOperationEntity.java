package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "AUDIT_OPERATIONS")
public class AuditOperationEntity {

    @Id
    @Column(name = "AUDIT_OPERATION_ID")
    private long auditOperationId;

    @Basic
    @Column(name = "OPERATION_NAME")
    private String operationName;

    @Basic
    @Column(name = "OPERATION_CODE")
    private String operationCode;

    @Basic
    @Column(name = "OPERATION_DESCRIPTION")
    private String operationDescription;
}
