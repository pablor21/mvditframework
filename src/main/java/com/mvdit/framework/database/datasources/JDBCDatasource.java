/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.database.datasources;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.core.PropertiesRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author Pablo Ramírez
 */
public class JDBCDatasource {

    public static final String DEFAULT_DB_ID = "default";
    public static final String DATABASES_FILE = "mvdit_databases";
    private Map<String, DataSource> dataSources;
    /**
     * Singleton reference of JDBCDatasource.
     */
    private static JDBCDatasource instance;

    /**
     * Constructs singleton instance of JDBCDatasource.
     */
    private JDBCDatasource() {
        this.dataSources = new HashMap<>();
    }

    /**
     * Provides reference to singleton object of JDBCDatasource.
     *
     * @return Singleton instance of JDBCDatasource.
     */
    public static final JDBCDatasource getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (JDBCDatasource.class) {
                if (instance == null) {
                    instance = new JDBCDatasource();
                }
            }
        }
        return instance;
    }

    private DataSource initDataSource(String dbId) {
        if (this.dataSources.containsKey(dbId)) {
            this.dataSources.remove(dbId);
        }
        DataSource ds = null;
        PropertiesRepository props = PropertiesRepository.getInstance();
        String type = props.getProperty(dbId + ".type", JDBCDatasource.DATABASES_FILE);

        try {
            IDatasourceFactory factory = (IDatasourceFactory) Class.forName(type).newInstance();
            ds = factory.initDatasource(dbId, props);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new MvditRuntimeException(ex);
        }

        if (ds == null) {
            throw new MvditRuntimeException("Datasource " + dbId + "not found");
        }
        this.dataSources.put(dbId, ds);
        return ds;
    }

    public synchronized Connection getConnection(String dbId) {
        try {
            if (MvditUtils.stringEmpty(dbId)) {
                dbId = JDBCDatasource.DEFAULT_DB_ID;
            }
            DataSource ds= null;
            if (!this.dataSources.containsKey(dbId)) {
                this.initDataSource(dbId);
            }
            ds= this.dataSources.get(dbId);
            return ds.getConnection();
        } catch (SQLException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    public synchronized Connection getConnection() {
        return this.getConnection(JDBCDatasource.DEFAULT_DB_ID);
    }

}
