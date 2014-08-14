/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.data;

import com.mvdit.framework.core.MvditRuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    protected Map<String,QueryCondition> conditions;
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
        this.conditions = new HashMap<>();
        this.orderParams = new ArrayList<>();
        this.pageNumber = 1;
        this.pageSize = 0;
        this.fields = "*";
    }

    @Override
    public Map<String, QueryCondition> getConditions() {
        return this.conditions;
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
    public void addCondition(String key, QueryCondition condition) {
        this.conditions.put(key, condition);
    }

    @Override
    public void addCondition(QueryCondition condition) {
        this.addCondition(condition.getField(), condition);
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
        Iterator<String> iterator = this.conditions.keySet().iterator();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            String key= iterator.next();
            QueryCondition condition = this.conditions.get(key);
            if (condition.isValid()) {
                switch(condition.getComparator()){
                    case ENDS_WITH:
                    case EW:
                        parameters.put(key, "%" + condition.getValue());
                        break;
                    case CONTAINS:
                    case CN:
                        parameters.put(key,"%" +condition.getValue() + "%");
                        break;
                    case STARTS_WITH:
                    case SW:
                        parameters.put(key,condition.getValue()+ "%");
                        break;
                    default:
                        parameters.put(key,condition.getValue());
                }
                
            }
        }
        return parameters;
    }

    @Override
    public void setConditions(List conditions) {
        if(conditions!=null){
            this.conditions= new HashMap<>();
            for(Object c: conditions){
                QueryCondition qc= (QueryCondition)c;
                this.addCondition(qc);
            }
        }
    }

    @Override
    public void setConditions(Map conditions) {
        this.conditions= new HashMap<>();
        if(conditions!=null){
            this.conditions= conditions;
        }
    }

   

}
