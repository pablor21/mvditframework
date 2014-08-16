/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.data;

import com.mvdit.framework.core.MvditRuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 */
public class GenericFilter implements IFilter {

    protected int pageSize;
    protected int pageNumber;
    protected String fields;
    protected List<QueryCondition> conditions;
    protected List<OrderParam> orderParams;

    public GenericFilter(int pageNumber, int pageSize, String fields) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.fields = fields;
    }

    public GenericFilter(int pageNumber, int pageSize) {
        this();
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public GenericFilter() {
        this.conditions = new ArrayList<>();
        this.orderParams = new ArrayList<>();
        this.pageNumber = 1;
        this.pageSize = 0;
        this.fields = "*";
    }
    
    @Override
    public List getPlainConditions(){
        List<QueryCondition> ret= new ArrayList<>();
        for(QueryCondition qc:this.conditions){
            ret.addAll(qc.getPlainConditions(""));
        }
        return ret;
    }

    @Override
    public List getConditions() {
        return this.conditions;
    }

    @Override
    public void setConditions(List conditions) {
        this.conditions = conditions;
    }

    @Override
    public String getFields() {
        return this.fields;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public void setPageSize(int pageSize) {
        if (pageSize < 0) {
            throw new MvditRuntimeException(new IndexOutOfBoundsException("The page size is invalid"));
        }
        this.pageSize = pageSize;
    }

    @Override
    public void setPageNumber(int pageNumber) {
        if (this.pageNumber < 1) {
            throw new MvditRuntimeException(new IndexOutOfBoundsException("The current page number is invalid"));
        }
        this.pageNumber = pageNumber;
    }

    @Override
    public void setFields(String fields) {
        this.fields = fields;
    }

    @Override
    public void addCondition(QueryCondition condition) {
        this.conditions.add(condition);
    }

    @Override
    public void addOrderParam(OrderParam order) {
        this.orderParams.add(order);
    }

    @Override
    public List<OrderParam> getOrderParams() {
        return this.orderParams;
    }

    @Override
    public Map<String, Object> getParametersValues() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        List<QueryCondition> conds= this.getConditions();
        for (QueryCondition condition : conds) {
            if (condition.isValid()) {
                if (condition.isValid()) {
                    parameters.putAll(condition.getParametersValues(""));
                }
            }
        }
        return parameters;
    }

    @Override
    public String getWhereSentence(String objectQualifier) {
        StringBuilder sb = new StringBuilder();
        boolean incOp = false;
        List<QueryCondition> conds= this.getConditions();
        for (QueryCondition condition : conds) {
            sb.append(condition.getSentenceStr(objectQualifier, incOp));
            sb.append(" ");
            incOp = true;
        }
        return sb.toString();
    }

    @Override
    public List containsFieldName(String fieldName) {
        List<QueryCondition> ret= new ArrayList<>();
        List<QueryCondition> conds= this.getConditions();
        for(QueryCondition qc:conds){
            if(qc.getField().equalsIgnoreCase(fieldName)){
                ret.add(qc);
            }
        }
        return ret;
    }
}
