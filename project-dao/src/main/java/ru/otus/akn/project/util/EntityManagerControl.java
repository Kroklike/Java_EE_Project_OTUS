package ru.otus.akn.project.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class EntityManagerControl {

    private final EntityManagerFactory managerFactory;

    public EntityManagerControl(EntityManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

    public void processRequest() throws Exception {
        EntityManager manager = managerFactory.createEntityManager();
        try {
            requestMethod(manager);
        } finally {
            manager.close();
        }
    }

    public abstract void requestMethod(EntityManager manager) throws Exception;
}
