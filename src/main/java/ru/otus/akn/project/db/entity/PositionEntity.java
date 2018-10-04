package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "POSITIONS")
public class PositionEntity {
    @Id
    @Column(name = "POSITION_ID")
    private long positionId;

    @Basic
    @Column(name = "POSITION_NAME")
    private String positionName;
}
