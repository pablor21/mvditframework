/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.dao;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.data.GenericFilter;
import com.mvdit.framework.data.GenericPageResult;
import com.mvdit.framework.data.IFilter;
import com.mvdit.framework.data.IPageResult;
import com.mvdit.framework.data.OrderParam;
import com.mvdit.framework.data.QueryCondition;
import com.mvdit.framework.data.QueryConditionGroup;
import com.mvdit.framework.database.TransactionException;
import com.mvdit.framework.database.datasources.JPADatasource;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Pablo Ramírez
 */
public abstract class GenericDAOJPAImpl<T, K> implements IGenericDAO<T, K> {

    protected String dbId;
    protected String defaultQueryObjectName;
    protected OrderParam defaultOrderParam;
    protected Class<T> entityClass;

    public GenericDAOJPAImpl() {
        dbId = "";
        defaultQueryObjectName = "o";
        defaultOrderParam = new OrderParam("id", OrderParam.ORDER_ASC);
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected EntityManager getEntityManager() {
        return JPADatasource.getInstance().getEntityManager();
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

    public OrderParam getDefaultOrderParam() {
        return defaultOrderParam;
    }

    public void setDefaultOrderParam(OrderParam defaultOrderParam) {
        this.defaultOrderParam = defaultOrderParam;
    }

    public Class<T> getEntityClass() {
        return entityClass;
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
            String str= filter.getWhereSentence(getDefaultQueryObjectName());
            if(!MvditUtils.stringEmpty(str)){
                sb.append(" WHERE ");
            }
            sb.append(str);
            /*List<QueryCondition> conditions = filter.getConditions();
            boolean incOp=false;
            for(QueryCondition cond:conditions){
                if (sb.toString().equalsIgnoreCase("")) {
                    sb.append(" WHERE ");
                }
                sb.append(cond.getSentenceStr(getDefaultQueryObjectName(), incOp));
                sb.append(" ");
                incOp= true;
            }*/
            /*Map<String, QueryConditionGroup> groups = filter.getConditions();
            Iterator<String> groupIterator = groups.keySet().iterator();
            boolean isFirstGroup = true;
            int index = 1;
            while (groupIterator.hasNext()) {
                String key = groupIterator.next();
                QueryConditionGroup group = groups.get(key);
                Iterator<String> condIterator = group.getConditions().keySet().iterator();

                if (sb.toString().equalsIgnoreCase("")) {
                    sb.append(" WHERE ");
                }

                if (!isFirstGroup) {
                    sb.append(group.getOperator()).append(" ");
                }
                sb.append("(");
                boolean isFirstCond = true;
                while (condIterator.hasNext()) {
                    String condKey = condIterator.next();
                    QueryCondition condition = group.getCondition(condKey);
                    if (condition.isValid()) {
                        if (!isFirstCond) {
                            sb.append(condition.getOperator()).append(" ");
                        }
                        sb.append(getDefaultQueryObjectName());//nombre del obj
                        sb.append(".");//separador de field
                        sb.append(condition.getField());//nombre del campo
                        sb.append(" ");//espacio para separar la definicion
                        sb.append(condition.getComparator());//operador de comparacion
                        sb.append(" :").append(key).append("_").append(condKey);//nombre del parámetro
                        sb.append(" ");//espacio para separar
                        isFirstCond = false;
                        index++;
                    }
                }
                sb.append(")");
                isFirstGroup = false;
            }*/
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

    public void setParametersOfQuery(Query query, IFilter filter) {
        if (filter == null) {
            return;
        }
        //mapeo de los parametros
        Map<String, Object> parametersValues = filter.getParametersValues();
        Iterator<String> iterator = parametersValues.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = parametersValues.get(key);
            query.setParameter(key, value);            
        }
    }

    protected void rollbackTransactionAndCloseEntityManager(EntityTransaction tx, EntityManager em) {
        try {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            if (em != null && em.isOpen()) {
                //em.close();
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public T create(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception ex) {
            rollbackTransactionAndCloseEntityManager(tx, em);
            throw new MvditRuntimeException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(entity);
            tx.commit();
            return entity;
        } catch (Exception ex) {
            rollbackTransactionAndCloseEntityManager(tx, em);
            throw new MvditRuntimeException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public int delete(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.remove(em.merge(entity));
            tx.commit();
            return 1;
        } catch (Exception ex) {
            rollbackTransactionAndCloseEntityManager(tx, em);
            throw new MvditRuntimeException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public IPageResult list(IFilter filter) {

        EntityManager em = getEntityManager();

        try {
            if (filter == null) {
                filter = new GenericFilter();
            }

            if (filter.getOrderParams().isEmpty()) {
                filter.addOrderParam(getDefaultOrderParam());
            }
            String conditionsStr = getConditionsStr(filter) + " " + getOrderParamsStr(filter);

            if (filter.getFields().equalsIgnoreCase("*")) {
                filter.setFields(getDefaultQueryObjectName());
            }
            TypedQuery<T> query = em.createQuery("SELECT " + filter.getFields() + " FROM " + entityClass.getSimpleName()
                    + " " + getDefaultQueryObjectName() + " " + conditionsStr, entityClass);

            //mapeo de los parametros
            setParametersOfQuery(query, filter);

            if (filter.getPageNumber() > 0) {
                query.setFirstResult((filter.getPageNumber() - 1) * filter.getPageSize());
            }
            if (filter.getPageSize() > 0) {
                query.setMaxResults(filter.getPageSize());
            }
            List<T> resultList = query.getResultList();
            IPageResult result = new GenericPageResult(resultList, filter, filter.getPlainConditions(), filter.getOrderParams(), count(filter));
            //si obtengo una pagina vacía y estoy filtrando, muestro la primera página
            if (result.getElements().isEmpty() && filter.getPageNumber() > 1) {
                filter.setPageNumber(result.getPageCount());
                return list(filter);

            }
            return result;
        } catch (Exception ex) {
            throw new MvditRuntimeException(ex);
        } finally {
            em.close();
        }

    }

    @Override
    public int count(IFilter filter) {
        EntityManager em = getEntityManager();

        try {

            if (filter.getOrderParams().isEmpty()) {
                filter.addOrderParam(getDefaultOrderParam());
            }
            String conditions = getConditionsStr(filter) + " " + getOrderParamsStr(filter);

            if (filter.getFields().equalsIgnoreCase("*")) {
                filter.setFields(getDefaultQueryObjectName());
            }

            Query query = em.createQuery("SELECT count(" + getDefaultQueryObjectName() + ") FROM " + entityClass.getSimpleName()
                    + " " + getDefaultQueryObjectName() + " " + conditions, entityClass);

            //mapeo de los parametros
            setParametersOfQuery(query, filter);

            Long count = (Long) query.getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            throw new MvditRuntimeException(ex);
        } finally {
            em.close();
        }

    }

    @Override
    public T getById(K id) {
        EntityManager em = getEntityManager();
        T entity = null;
        try {
            entity = em.find(entityClass, id);
            return entity;
        } catch (Exception ex) {
            throw new MvditRuntimeException(ex);
        } finally {
            em.close();
        }
    }

    @Override
    public void initTransaction() throws TransactionException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
    }

    @Override
    public void commitTransaction() throws TransactionException {
        EntityManager em = getEntityManager();
        em.getTransaction().commit();
    }

    @Override
    public void rollbackTransaction() throws TransactionException {
        EntityManager em = getEntityManager();
        em.getTransaction().rollback();
    }

    @Override
    public boolean isTransactionActive() {
        EntityManager em = getEntityManager();
        return em.getTransaction().isActive();
    }

}
