package ru.otus.akn.project.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic implements IsSerializable {

    private String pageName;
    private String ipAddress;
    private String browser;
    private Date date;
    private String cookies;
    private String requestData;

}
