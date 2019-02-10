package ru.otus.akn.project.ejb.api.stateless;

import javax.ejb.Remote;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Remote
public interface TableService {

    void cleanTables();

    String fillUpDepartmentsTable(@NotNull ServletContext context) throws IOException;

    String fillUpUsersTable(@NotNull ServletContext context) throws IOException;

    String fillUpPositionsTable(@NotNull ServletContext context) throws IOException;

    String fillUpEmployeesTable(@NotNull ServletContext context) throws IOException;

}
