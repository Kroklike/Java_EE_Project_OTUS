package ru.otus.akn.project.xml.data;

import lombok.Data;
import lombok.ToString;

import javax.json.bind.annotation.JsonbProperty;

@Data
@ToString
public class EmployeeListFromJson {

    @JsonbProperty("employees")
    private EmployeesList employeesList;

}
