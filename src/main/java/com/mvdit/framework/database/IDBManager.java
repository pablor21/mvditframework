/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 */
public interface IDBManager {
    /**
     * 
     * @param conn 
     */
    void prepare(Connection conn);
    /**
     * 
     * @return 
     */
    Connection getConnection();
    
    /**
     * 
     * @return 
     */
    String getDriverClassName();
    /**
     * Inserta un valor en la base de datos
     * @param tableName
     * @param data
     * @return 
     */
    DBInsertResponse insert(String tableName, Map<String,Object> data);
    /**
     * Inserta un valor en la base de datos
     * @param tableName
     * @param data
     * @param type
     * @return 
     */
    DBInsertResponse insert(String tableName, Map<String,Object> data, TypeOfInsert type);
    /**
     * Reemplaza un valor en la base de datos
     * @param tableName
     * @param data
     * @return 
     */
    DBInsertResponse replace(String tableName, Map<String,Object> data);
    /**
     * Elimina de la base de datos
     * @param tableName
     * @param conditions
     * @param parameters
     * @return 
     */
    DBUpdateResponse delete(String tableName, String conditions, Object... parameters);
    /**
     * Actualiza un valor de la base de datos
     * @param tableName
     * @param data
     * @param conditions
     * @param parameters
     * @return 
     */
    DBUpdateResponse update(String tableName, Map<String,Object> data, String conditions, Object... parameters);
    /**
     * Realiza una consulta a la base de datos
     * @param sql
     * @param parameters
     * @return 
     */
    QueryResult query(String sql, Object... parameters);
    /**
     * Consulta la base de datos
     * @param tableName
     * @param conditions
     * @param fields
     * @param countTotal
     * @param responseType
     * @param parameters
     * @return 
     */
    DBSelectResponse select(String tableName, String conditions, String fields, Boolean countTotal, Class responseType, Object... parameters);
    
     /**
     * Consulta la base de datos
     * @param tableName
     * @param conditions
     * @param pramsValues
     * @param fields
     * @param countTotal
     * @param responseType
     * @param parameters
     * @return 
     */
    DBSelectResponse select(String tableName, String conditions, Collection<Object> pramsValues, String fields, Boolean countTotal, Class responseType, Object... parameters);
    
    
    String getLimit(int page, int pageSize);
    /**
     * Inicia una transaccion
     */
    void beginTransaction();
    /**
     * realiza la transaccion
     */
    void commitTransaction();
    /**
     * resetea la transaccion
     */
    void rollbackTransaction();
    /**
     * quita la transaccion (En caso de existir una transaccion activa, la realiza)
     */
    void setAutoCommit();
    /**
     * Cierra la conexión
     */
    void closeConnection();

    /**
     * 
     * @return 
     */
    public SqlLogger getLogger();
    /**
     * 
     * @param logger 
     */
    public void setLogger(SqlLogger logger);
}
