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
public enum QueryConditionComparators {

    LIKE("LIKE"),
    LK("LIKE"),//short LIKE
    STARTS_WITH("LIKE"),
    SW("LIKE"),//short STARTS_WITH
    ENDS_WITH("LIKE"),
    EW("LIKE"),//short ENDS_WITH
    CONTAINS("LIKE"),
    CN("LIKE"),//short CONTAINS
    EQ("="),
    NOT_EQ("!="),
    NE("!="),//short NOT_EQ
    IS("IS"),
    NOT_IS("NOT IS"),
    NI("NOT IS"),//short NOT_IS
    IN("IN"),
    NOT_IN("NOT IN"),
    NN("NOT IN"),//short NOT_IN
    LESS_THAN("<"),
    LT("<"),//short LESS_THAN
    LESS_OR_EQ_THAN("<="),
    LE("<="),//short LESS_OR_EQ_THAN
    GREATER_THAN(">"),
    GT(">"),//short GREATER_THAN
    GREATER_OR_EQ_THAN(">="),
    GE(">="),//short GREATER_OR_EQ_THAN
    MATCH("MATCH"),
    MA("MATCH"),//short MATCH
    AGAINST("AGAINST"),
    AG("AGAINST");//short AGAINST

    private final String comparator;

    /**
     * @param text
     */
    private QueryConditionComparators(final String comparator) {
        this.comparator = comparator;
    }


    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.comparator;
    }

    /**
     *
     * @return
     */
    public List<QueryConditionComparators> getValues() {       
        return Arrays.asList(QueryConditionComparators.values());
    }
    
    public List<String> getValuesStr(){
        List<QueryConditionComparators> values = this.getValues();
        List<String> ret= new ArrayList<>();
        for(QueryConditionComparators value:values){
            ret.add(value.toString());
        }
        return ret;
    }
}
