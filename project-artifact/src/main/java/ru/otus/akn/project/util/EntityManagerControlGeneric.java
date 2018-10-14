package ru.otus.akn.project.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class EntityManagerControlGeneric<T> {

    private final EntityManagerFactory managerFactory;

    public EntityManagerControlGeneric(EntityManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

    public T processRequest() throws Exception {
        EntityManager manager = managerFactory.createEntityManager();
        try {
            return requestMethod(manager);
        } finally {
            manager.close();
        }
    }

    public abstract T requestMethod(EntityManager manager) throws Exception;

}
