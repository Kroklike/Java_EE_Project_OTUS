package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USERS")
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    @SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    private long userId;

    @Basic
    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Basic
    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Basic
    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
}
