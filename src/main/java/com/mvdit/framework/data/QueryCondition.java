/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.data;

import com.mvdit.framework.core.MvditUtils;

/**
 *
 * @author Pablo Ramírez
 */
public class QueryCondition {
   private QueryConditionComparators comparator;
   private QueryConditionOperators operator;
   private String field;
   private Object value;

    public QueryCondition(QueryConditionComparators comparator, QueryConditionOperators operator, String field, String value) {
        this.comparator = comparator;
        this.operator = operator;
        this.field = field;
        this.value = value;
    }

    public QueryCondition() {
        this(QueryConditionComparators.EQ, QueryConditionOperators.AND, "", "");
    }  
   

    public QueryConditionComparators getComparator() {
        return comparator;
    }

    public void setComparator(QueryConditionComparators comparator) {
        this.comparator = comparator;
    }

    public QueryConditionOperators getOperator() {
        return operator;
    }

    public void setOperator(QueryConditionOperators operator) {
        this.operator = operator;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
   
    public boolean isValid(){
        return !MvditUtils.stringEmpty(field) && value!=null;
    }
   
   @Override
    public String toString(){
       return this.operator + " " + this.toStringNotOperator();
        
    }
    
    public String toStringNotOperator(){
        return this.field + " " + this.comparator + " " + this.value;
    }
}
