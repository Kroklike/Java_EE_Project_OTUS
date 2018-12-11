package ru.otus.akn.project.xml.data;

import lombok.Data;
import lombok.ToString;
import ru.otus.akn.project.db.entity.DepartmentEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@ToString
@XmlRootElement(name = "departments")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepartmentsList {

    @XmlElement(name = "department", required = true)
    private List<DepartmentEntity> departmentEntities;

}
