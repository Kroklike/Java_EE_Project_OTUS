package ru.otus.akn.project.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements IsSerializable {

    @NotNull
    @Size(min = 4, message = "Login must contain at least 4 characters.")
    private String login;

    @NotNull
    private String password;
}