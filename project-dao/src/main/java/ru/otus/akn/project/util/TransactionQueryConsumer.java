package ru.otus.akn.project.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class TransactionQueryConsumer {
    private final EntityManager manager;

    public TransactionQueryConsumer(EntityManager manager) {
        this.manager = manager;
    }

    public final void processQueryInTransaction() {
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            needToProcessData();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Exception has arisen during query processing.", e);
        }
    }

    public abstract void needToProcessData() throws Exception;
}
