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
    List<QueryCondition> getConditions();
    List<QueryCondition> getPlainConditions();
    String getFields();
    int getPageSize();
    int getPageNumber();
    void setPageSize(int pageSize);
    void setPageNumber(int pageNumber);
    void setConditions(List<QueryCondition> conditions);
    void setFields(String fields);
    void addCondition(QueryCondition condition);
    Map<String,Object> getParametersValues();
    void addOrderParam(OrderParam order);   
    List<OrderParam> getOrderParams();
    String getWhereSentence(String objectQualifier);
    List<QueryCondition> containsFieldName(String fieldName);
}
