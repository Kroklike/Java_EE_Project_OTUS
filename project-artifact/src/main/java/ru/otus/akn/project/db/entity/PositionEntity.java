package ru.otus.akn.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "POSITIONS")
public class PositionEntity {
    @Id
    @Column(name = "POSITION_ID")
    @SequenceGenerator(name = "POSITIONS_SEQ", sequenceName = "POSITIONS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POSITIONS_SEQ")
    private long positionId;

    @Basic
    @Column(name = "POSITION_NAME", nullable = false)
    private String positionName;
}
