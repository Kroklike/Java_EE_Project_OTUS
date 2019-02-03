package ru.otus.akn.project.rest.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

@Data
@FieldNameConstants
public class Department {

    @FormParam(FIELD_DEPARTMENT_ID)
    private long departmentId;

    @FormParam(FIELD_DEPARTMENT_NAME)
    @NotNull
    private String departmentName;

    @FormParam(FIELD_CITY)
    private String city;

    @FormParam(FIELD_FULL_ADDRESS)
    private String fullAddress;

}
