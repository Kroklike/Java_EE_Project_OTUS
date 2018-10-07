package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "AUDIT_OPERATIONS")
public class AuditOperationEntity {

    @Id
    @Column(name = "AUDIT_OPERATION_ID")
    @SequenceGenerator(name = "AUDIT_OPERATIONS_SEQ", sequenceName = "AUDIT_OPERATIONS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_OPERATIONS_SEQ")
    private long auditOperationId;

    @Basic
    @Column(name = "OPERATION_NAME", nullable = false)
    private String operationName;

    @Basic
    @Column(name = "OPERATION_CODE", nullable = false)
    private String operationCode;

    @Basic
    @Column(name = "OPERATION_DESCRIPTION", nullable = false)
    private String operationDescription;
}
