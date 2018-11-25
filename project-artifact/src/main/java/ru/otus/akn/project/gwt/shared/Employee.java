package ru.otus.akn.project.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements IsSerializable {

    @NonNull
    private String fullName;
    @NonNull
    private String departmentName;
    @NonNull
    private String positionName;
    @NonNull
    private BigDecimal salary;
}
