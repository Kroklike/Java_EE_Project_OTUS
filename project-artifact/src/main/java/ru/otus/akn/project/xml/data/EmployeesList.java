package ru.otus.akn.project.xml.data;

import lombok.Data;
import lombok.ToString;
import ru.otus.akn.project.db.entity.EmployeeEntity;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@ToString
@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeesList {

    @XmlElement(name = "employee", required = true)
    @JsonbProperty("employee")
    private List<EmployeeEntity> employeeEntities;

}
