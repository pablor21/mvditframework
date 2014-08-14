/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pablo Ramírez
 */
@XmlRootElement
public enum QueryConditionOperators {
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    XOR("XOR");

    private final String operator;

    /**
     * @param text
     */
    private QueryConditionOperators(final String operator) {
        this.operator = operator;
    }


    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.operator;
    }
    
    /**
     *
     * @return
     */
    public List<QueryConditionOperators> getValues() {       
        return Arrays.asList(QueryConditionOperators.values());
    }
    
    public List<String> getValuesStr(){
        List<QueryConditionOperators> values = this.getValues();
        List<String> ret= new ArrayList<>();
        for(QueryConditionOperators value:values){
            ret.add(value.toString());
        }
        return ret;
    }
}
