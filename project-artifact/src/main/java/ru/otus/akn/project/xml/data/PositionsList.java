package ru.otus.akn.project.xml.data;

import lombok.Data;
import lombok.ToString;
import ru.otus.akn.project.db.entity.PositionEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@ToString
@XmlRootElement(name = "positions")
@XmlAccessorType(XmlAccessType.FIELD)
public class PositionsList {

    @XmlElement(name = "position", required = true)
    private List<PositionEntity> positionEntities;

}
