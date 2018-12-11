package ru.otus.akn.project.db.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Data
@Entity
@Table(name = "POSITIONS")
@ToString
@XmlRootElement(name = "position")
@XmlAccessorType(XmlAccessType.FIELD)
public class PositionEntity {

    @Id
    @Column(name = "POSITION_ID")
    @SequenceGenerator(name = "POSITIONS_SEQ", sequenceName = "POSITIONS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POSITIONS_SEQ")
    @XmlAttribute(required = true)
    private Long positionId;

    @Basic
    @Column(name = "POSITION_NAME", nullable = false)
    @XmlElement(required = true)
    private String positionName;
}
