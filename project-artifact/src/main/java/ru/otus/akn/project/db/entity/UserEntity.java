package ru.otus.akn.project.db.entity;

import lombok.Data;
import lombok.ToString;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Data
@Entity
@Table(name = "USERS")
@ToString
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    @SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @XmlAttribute(required = true)
    private long userId;

    @Basic
    @Column(name = "LOGIN", nullable = false)
    @XmlElement(required = true)
    private String login;

    @Basic
    @Column(name = "PASSWORD_HASH", nullable = false)
    @XmlElement(required = true)
    private String passwordHash;

    @Basic
    @Column(name = "IS_ACTIVE", nullable = false)
    @XmlElement(required = true)
    private Boolean isActive;
}
