/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.database;

import com.mvdit.framework.database.datasources.JDBCDatasource;
import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.PropertiesRepository;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 */
public class DBManager implements IDBManager {

    private IDBDriver driver = null;
    private String driverClassName = null;
    private String dbId;
    private SqlLogger logger;

    public DBManager(String dbId) {
        this(dbId, null);
    }

    public DBManager(SqlLogger logger) {
        this(JDBCDatasource.DEFAULT_DB_ID, logger);
    }

    public DBManager() {
        this(JDBCDatasource.DEFAULT_DB_ID, null);
    }

    public DBManager(String dbId, SqlLogger logger) {
        this.logger = logger;
        this.dbId = dbId;
    }

    @Override
    public void prepare(Connection conn) {
        try {
            PropertiesRepository props = PropertiesRepository.getInstance();
            this.driverClassName = props.getProperty(dbId + ".className", JDBCDatasource.DATABASES_FILE);
            driver = (IDBDriver) Class.forName(this.driverClassName).newInstance();
            driver.setConnection(conn);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    @Override
    public Connection getConnection() {
        return driver.getConnection();
    }

    @Override
    public String getDriverClassName() {
        return this.driver.getClass().getName();
    }

    @Override
    public SqlLogger getLogger() {
        return this.driver.getLogger();
    }

    @Override
    public void setLogger(SqlLogger logger) {
        this.driver.setLogger(logger);
    }

    @Override
    public DBInsertResponse insert(String tableName, Map<String, Object> data) {
        return this.driver.insert(tableName, data);
    }

    @Override
    public DBInsertResponse insert(String tableName, Map<String, Object> data, TypeOfInsert type) {
        return this.driver.insert(tableName, data, type);
    }

    @Override
    public DBInsertResponse replace(String tableName, Map<String, Object> data) {
        return this.driver.replace(tableName, data);
    }

    @Override
    public DBUpdateResponse delete(String tableName, String conditions, Object... parameters) {
        if(parameters==null || parameters.length==0){
            throw new MvditRuntimeException("No puede eliminar un objeto sin parámetros.");
        }
        return this.driver.delete(tableName, conditions, parameters);
    }

    @Override
    public DBUpdateResponse update(String tableName, Map<String, Object> data, String conditions, Object... parameters) {
        if(parameters==null || parameters.length==0){
            throw new MvditRuntimeException("No puede actualizar un objeto sin parámetros.");
        }
        return this.driver.update(tableName, data, conditions, parameters);
    }

    @Override
    public QueryResult query(String sql, Object... parameters) {
        return this.driver.query(sql, parameters);
    }

    @Override
    public DBSelectResponse select(String tableName, String conditions, String fields, Boolean countTotal, Class responseType, Object... parameters) {
        return this.driver.select(tableName, conditions, fields, countTotal, responseType, parameters);
    }

    @Override
    public void beginTransaction() {
        this.driver.beginTransaction();
    }

    @Override
    public void commitTransaction() {
        this.driver.commitTransaction();
    }

    @Override
    public void rollbackTransaction() {
        this.driver.rollbackTransaction();
    }

    @Override
    public void setAutoCommit() {
        this.driver.setAutoCommit();
    }

    @Override
    public void closeConnection() {
        this.driver.closeConnection();
    }

    @Override
    public String getLimit(int page, int pageSize) {
        return this.driver.getLimit(page, pageSize);
    }

    @Override
    public DBSelectResponse select(String tableName, String conditions, Collection<Object> pramsValues, String fields, Boolean countTotal, Class responseType, Object... parameters) {
        return this.driver.select(tableName, conditions, pramsValues, fields, countTotal, responseType, parameters);
    }


}
