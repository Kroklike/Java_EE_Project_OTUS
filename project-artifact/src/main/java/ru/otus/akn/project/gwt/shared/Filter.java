package ru.otus.akn.project.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return Objects.equals(firstName, filter.firstName) &&
                Objects.equals(lastName, filter.lastName) &&
                Objects.equals(middleName, filter.middleName) &&
                Objects.equals(position, filter.position) &&
                Objects.equals(town, filter.town) &&
                Objects.equals(ageFrom, filter.ageFrom) &&
                Objects.equals(ageTo, filter.ageTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, middleName, position, town, ageFrom, ageTo);
    }
}
