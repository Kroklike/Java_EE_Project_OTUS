package ru.otus.akn.project.db.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtil {
    private static final String PERSISTENCE_UNIT_NAME = "jpa";

    public static final EntityManagerFactory MANAGER_FACTORY =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // for Tomcat

    //    @PersistenceUnit(unitName = PERSISTENCE_UNIT_NAME)
//    EntityManagerFactory emf; // for Glassfish
}
