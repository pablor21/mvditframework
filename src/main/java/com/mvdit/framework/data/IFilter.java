/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.data;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 */
public interface IFilter<T> {
    Map<String, QueryCondition> getConditions();
    String getFields();
    int getPageSize();
    int getPageNumber();
    void setPageSize(int pageSize);
    void setPageNumber(int pageNumber);
    void setConditions(List<QueryCondition> conditions);
    void setConditions(Map<String, QueryCondition> conditions);
    void setFields(String fields);
    void addCondition(QueryCondition condition);
    void addCondition(String key, QueryCondition condition);
    Map<String,Object> getParametersValues();
    void addOrderParam(OrderParam order);   
    List<OrderParam> getOrderParams();
    //int getLastPageNumber();
}
