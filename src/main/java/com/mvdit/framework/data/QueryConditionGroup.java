/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 */
public class QueryConditionGroup {
    protected String id;
    protected Map<String,QueryCondition> conditions;
    protected QueryConditionOperators operator;

    public QueryConditionGroup(String id, Map<String, QueryCondition> conditions, QueryConditionOperators operator) {
        this.id = id;
        this.conditions = conditions;
        this.operator = operator;
    }

    public QueryConditionGroup(String id, QueryConditionOperators operator) {
        this.id = id;
        this.operator = operator;
        this.conditions= new HashMap<>();
    }

    public QueryConditionGroup(String id) {
        this.id=id;
        this.operator= QueryConditionOperators.AND;
        this.conditions= new HashMap<>();
    }  

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public Map<String, QueryCondition> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, QueryCondition> conditions) {
        this.conditions = conditions;
    }
    
    public void addCondition(String key, QueryCondition condition){
        this.conditions.put(key, condition);
    }
    
    public QueryCondition getCondition(String key){
        return this.conditions.get(key);
    }

    public QueryConditionOperators getOperator() {
        return operator;
    }

    public void setOperator(QueryConditionOperators operator) {
        this.operator = operator;
    }   
}
