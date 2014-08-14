/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.dao;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.data.GenericPageResult;
import com.mvdit.framework.data.IFilter;
import com.mvdit.framework.data.IPageResult;
import com.mvdit.framework.data.OrderParam;
import com.mvdit.framework.data.QueryCondition;
import com.mvdit.framework.database.DBInsertResponse;
import com.mvdit.framework.database.DBSelectResponse;
import com.mvdit.framework.database.DBUpdateResponse;
import com.mvdit.framework.database.IDBManager;
import com.mvdit.framework.database.IDBEntity;
import com.mvdit.framework.database.TransactionException;
import com.mvdit.framework.database.datasources.JDBCDatasource;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 * @param <K>
 */
public abstract class GenericDAOJDBCImpl<T extends IDBEntity, K> implements IGenericDAO<T, K> {

    protected String dbId;
    protected String defaultQueryObjectName;
    protected Class<T> entityClass;

    public GenericDAOJDBCImpl() {
        dbId = "";
        defaultQueryObjectName = "o";
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public String getDbId() {
        return dbId;
    }

    public String getDefaultQueryObjectName() {
        return defaultQueryObjectName;
    }

    public void setDefaultQueryObjectName(String defaultQueryObjectName) {
        this.defaultQueryObjectName = defaultQueryObjectName;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Metodo para obtener el dbmanager
     *
     * @return
     */
    protected abstract IDBManager getDbManager();

    /**
     * Obtiene la conexión a la base de datos
     *
     * @see getDbConnection(String dbId)
     * @return
     */
    protected Connection getDbConnection() {
        return this.getDbConnection(this.dbId);
    }

    /**
     * Obtiene la conexión a la base de datos con una id determinada
     *
     * @param dbId el identificador de la bd
     * @return
     */
    protected Connection getDbConnection(String dbId) {
        return JDBCDatasource.getInstance().getConnection(dbId);
    }

    /**
     * Prepara el DbManager para aceptar peticiones
     *
     * @param conn
     */
    protected void prepareDbManager(Connection conn) {
        getDbManager().prepare(conn);
    }

    /**
     * Obtiene un DBManager preparado con la bd por default
     *
     * @return
     */
    protected IDBManager getPreparedDbManager() {
        return this.getPreparedDbManager(this.dbId);
    }

    /**
     * Obtiene un DBManager preparado con la bd con identificador dbId
     *
     * @param dbId
     * @return
     */
    protected IDBManager getPreparedDbManager(String dbId) {
        IDBManager dbManager = getDbManager();
        Connection conn = getDbConnection(dbId);
        dbManager.prepare(conn);
        return dbManager;
    }

    /**
     * Obtiene el string de condiciones sql del filtro
     *
     * @param filter
     * @return
     */
    public String getConditionsStr(IFilter filter) {
        StringBuilder sb = new StringBuilder();
        if (filter != null) {
            Map<String, QueryCondition> conditions= filter.getConditions();
            Iterator<String> iterator = conditions.keySet().iterator();
            boolean isFirst = true;
            int index = 1;
            while (iterator.hasNext()) {
                String key = iterator.next();
                QueryCondition condition = conditions.get(key);
                if (condition.isValid()) {
                    if (sb.toString().equalsIgnoreCase("")) {
                        sb.append(" WHERE ");
                    }
                    if (isFirst) {
                        sb.append(getDefaultQueryObjectName()).append(".").append(condition.getField()).append(" ").append(condition.getComparator()).append(" ?");
                    } else {
                        sb.append(condition.getOperator()).append(" ").append(getDefaultQueryObjectName()).append(".").append(condition.getField())
                                .append(" ").append(condition.getComparator()).append(" ?");
                    }
                    sb.append(" ");
                    isFirst = false;

                }
            }
        }
        return sb.toString();
    }

    /**
     * Obtiene el string de orden del filtro
     *
     * @param filter
     * @return
     */
    protected String getOrderParamsStr(IFilter filter) {

        StringBuilder sb = new StringBuilder();
        if (filter != null) {
            Iterator<OrderParam> iterator = filter.getOrderParams().iterator();
            boolean isFirst = true;
            while (iterator.hasNext()) {
                OrderParam param = iterator.next();
                if (param.isValid()) {
                    if (sb.toString().equalsIgnoreCase("")) {
                        sb.append("ORDER BY ");
                    }
                    if (!isFirst) {
                        sb.append(", ");
                    }
                    isFirst = false;
                    sb.append(getDefaultQueryObjectName()).append(".").append(param.getField()).append(" ").append(param.getDirection());
                }

            }
        }
        return sb.toString();
    }

    @Override
    public T create(T entity) {
        IDBManager dbManager = getPreparedDbManager();
        try {
            DBInsertResponse insertResponse = dbManager.insert(entity.getTableName(), entity.toSqlArray());
            //DBSelectResponse select = dbManager.select(entity.getTableName(), "WHERE " + entity.getPrimaryKeyName() + "='" + insertResponse.getLastInsertId() + "'", "*", false, entityClass);
            if (MvditUtils.isNumeric(String.valueOf(insertResponse.getLastInsertId()))) {
                return getById((K) new Integer(insertResponse.getLastInsertId()));
            } else {
                return getById((K) entity.getId());
            }
        } finally {
            dbManager.closeConnection();
        }
    }

    @Override
    public T update(T entity) {
        IDBManager dbManager = getPreparedDbManager();
        try {
            DBUpdateResponse updateResponse = dbManager.update(entity.getTableName(), entity.toSqlArray(), "WHERE " + entity.getPrimaryKeyName() + "=?", entity.getId());
            return getById((K) entity.getId());
        } finally {
            dbManager.closeConnection();
        }
    }

    @Override
    public int delete(T entity) {
        IDBManager dbManager = getPreparedDbManager();
        try {

            //parameters.add(entity.getId());
            DBUpdateResponse updateResponse = dbManager.delete(entity.getTableName(), "WHERE " + entity.getPrimaryKeyName() + "=?", entity.getId());
            return updateResponse.getAffectedRows();
        } finally {
            dbManager.closeConnection();
        }
    }

    @Override
    public IPageResult list(IFilter filter) {

        IDBManager dbManager = getPreparedDbManager();
        try {
            T entity = null;
            try {
                entity = entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new MvditRuntimeException(ex);
            }

            if (filter.getOrderParams().isEmpty()) {
                OrderParam param = new OrderParam();
                param.setDirection(OrderParam.ORDER_ASC);
                param.setField(entity.getPrimaryKeyName());
                filter.addOrderParam(param);
            }

            String conditionsStr= getConditionsStr(filter) + " " + getOrderParamsStr(filter) + " " + dbManager.getLimit(filter.getPageNumber(), filter.getPageSize());
            DBSelectResponse resp = dbManager.select(entity.getTableName() + " AS " + getDefaultQueryObjectName(), conditionsStr, filter.getParametersValues().values(), filter.getFields(), true, entityClass);
            
            //List<QueryCondition> conditions= (List<QueryCondition>) filter.getConditions().values();
            IPageResult result = new GenericPageResult(resp.getData(), filter, filter.getConditions(), filter.getOrderParams(), resp.getTotalAbs());
            //si obtengo una pagina vacía y estoy filtrando, muestro la primera página
            if (result.getElements().isEmpty() && filter.getPageNumber() > 1) {
                filter.setPageNumber(result.getPageCount());
                return list(filter);
            }
            return result;
        } finally {
            dbManager.closeConnection();
        }
    }

    @Override
    public int count(IFilter filter) {
        IDBManager dbManager = getPreparedDbManager();
        try {
            T entity = null;
            try {
                entity = entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new MvditRuntimeException(ex);
            }
            DBSelectResponse resp = dbManager.select(entity.getTableName(), getConditionsStr(filter), "count(*) as TOTAL", false, null);
            Map<String, Object> res = (Map<String, Object>) resp.getData().get(0);
            Integer count = (Integer) res.get("TOTAL");
            return count;
        } finally {
            dbManager.closeConnection();
        }
    }

    @Override
    public T getById(K id) {
        IDBManager dbManager = getPreparedDbManager();
        try {
            T entity = null;
            try {
                entity = entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new MvditRuntimeException(ex);
            }
            DBSelectResponse response = dbManager.select(entity.getTableName(), "WHERE " + entity.getPrimaryKeyName() + "='" + id + "'", "*", false, entityClass);
            if (response.getData() != null && !response.getData().isEmpty()) {
                return (T) response.getData().get(0);
            }
            return null;
        } finally {
            dbManager.closeConnection();
        }
    }
    
     @Override
    public void initTransaction() throws TransactionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commitTransaction() throws TransactionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rollbackTransaction() throws TransactionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isTransactionActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
