package ru.otus.akn.project.db.filters;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeFilter {

    private String firstName;
    private String lastName;
    private String middleName;
    private String position;
    private String town;
    private String ageFrom;
    private String ageTo;

}
