package ru.otus.akn.project.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Filter implements IsSerializable {

    private String firstName;
    private String lastName;
    private String middleName;
    private String position;
    private String town;
    private String ageFrom;
    private String ageTo;

}
