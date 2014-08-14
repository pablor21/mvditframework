/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.database.datasources;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Pablo Ramírez
 */
public class JPADatasource {

    public static final String DEFAULT_DB_ID = "default";
    private Map<String, EntityManagerFactory> dataSources;

    /**
     * Singleton reference of JDBCDatasource.
     */
    private static JPADatasource instance;

    /**
     * Constructs singleton instance of JDBCDatasource.
     */
    private JPADatasource() {
        this.dataSources = new HashMap<>();
    }

    /**
     * Provides reference to singleton object of JDBCDatasource.
     *
     * @return Singleton instance of JDBCDatasource.
     */
    public static final JPADatasource getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (JPADatasource.class) {
                if (instance == null) {
                    instance = new JPADatasource();
                }
            }
        }
        return instance;
    }

    private EntityManagerFactory initEntityManagerFactory(String dbId) {
        if (this.dataSources.containsKey(dbId)) {
            try {
                this.dataSources.get(dbId).close();
            } catch (Exception ex) {
            }
            this.dataSources.remove(dbId);
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dbId);
        this.dataSources.put(dbId, emf);
        return emf;
    }

    public synchronized EntityManagerFactory addEntityManagerFactory(String dbId, EntityManagerFactory emf) {
        if (this.dataSources.containsKey(dbId)) {
            try {
                this.dataSources.get(dbId).close();
            } catch (Exception ex) {
            }
            this.dataSources.remove(dbId);
        }
        this.dataSources.put(dbId, emf);
        return emf;
    }

    public synchronized EntityManager getEntityManager(String dbId) {
        try {
            if (MvditUtils.stringEmpty(dbId)) {
                dbId = JPADatasource.DEFAULT_DB_ID;
            }
            EntityManagerFactory emf = null;
            if (!this.dataSources.containsKey(dbId)) {
                this.initEntityManagerFactory(dbId);
            }
            emf = this.dataSources.get(dbId);
            return emf.createEntityManager();
        } catch (Exception ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    public synchronized EntityManager getEntityManager() {
        return this.getEntityManager(JPADatasource.DEFAULT_DB_ID);
    }
}
