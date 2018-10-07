package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "AUDIT_HISTORIES")
public class AuditHistoryEntity {

    @Id
    @Column(name = "AUDIT_HISTORY_ID")
    @SequenceGenerator(name = "AUDIT_HISTORIES_SEQ", sequenceName = "AUDIT_HISTORIES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_HISTORIES_SEQ")
    private long auditHistoryId;

    @Basic
    @Column(name = "EXECUTION_DATE", nullable = false)
    private LocalDate executionDate;

    @ManyToOne
    @JoinColumn(name = "AUDIT_OPERATION_ID", nullable = false, foreignKey=@ForeignKey(name = "AUDIT_HISTORY_OPERATION_FK"))
    private AuditOperationEntity auditOperationEntity;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey=@ForeignKey(name = "AUDIT_HISTORY_USER_FK"))
    private UserEntity userEntity;

    @Basic
    @Column(name = "IS_ERROR", nullable = false)
    private Boolean isError;

    @Basic
    @Column(name = "ERROR_INFO")
    private String errorInfo;
}
