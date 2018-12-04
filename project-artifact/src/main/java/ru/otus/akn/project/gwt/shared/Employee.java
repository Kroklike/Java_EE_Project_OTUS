package ru.otus.akn.project.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements IsSerializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String departmentName;
    private String positionName;
    private BigDecimal salary;

    public boolean isReadyToSave() {
        return (firstName != null && !firstName.isEmpty()) &&
                (lastName != null && !lastName.isEmpty()) &&
                (departmentName != null && !departmentName.isEmpty()) &&
                (positionName != null && !positionName.isEmpty()) &&
                salary != null;
    }
}
