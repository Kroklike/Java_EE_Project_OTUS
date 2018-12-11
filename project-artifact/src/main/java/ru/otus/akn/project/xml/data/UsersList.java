package ru.otus.akn.project.xml.data;

import lombok.Data;
import lombok.ToString;
import ru.otus.akn.project.db.entity.UserEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@ToString
@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersList {

    @XmlElement(name = "user", required = true)
    private List<UserEntity> userEntities;

}
