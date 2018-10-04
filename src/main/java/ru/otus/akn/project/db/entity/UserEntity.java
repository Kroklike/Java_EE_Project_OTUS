package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USERS")
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    private long userId;

    @Basic
    @Column(name = "LOGIN")
    private String login;

    @Basic
    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    @Basic
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
}
