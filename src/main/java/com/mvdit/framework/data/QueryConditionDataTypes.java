/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonValue;

/**
 *
 * @author Pablo Ramírez
 */
@XmlRootElement(name = "QCDT")
public enum QueryConditionDataTypes {
    DATE(Date.class),
    DATE_TIME(Date.class),
    STRING(String.class),
    INTEGER(Integer.class),
    INT(Integer.class),
    DOUBLE(Double.class),
    LONG(Long.class),
    SHORT(Short.class),
    FLOAT(Float.class),
    OBJECT(Object.class),
    BIGINTEGER(BigInteger.class),
    BIGDECIMAL(BigDecimal.class),
    BOOL(BigDecimal.class),
    BOOLEAN(Boolean.class);

    private final Class<?> type;

    /**
     * @param text
     */
    private QueryConditionDataTypes(final Class<?> operator) {
        this.type = operator;
    }


    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.type.getCanonicalName();
    }
    @JsonValue
    public String value(){
        return this.name();
    }
    
    public Class<?> type(){
        return this.type;
    }

    /**
     *
     * @return
     */
    public List<QueryConditionDataTypes> getValues() {
        return Arrays.asList(QueryConditionDataTypes.values());
    }

    public List<String> getValuesStr() {
        List<QueryConditionDataTypes> values = this.getValues();
        List<String> ret = new ArrayList<>();
        for (QueryConditionDataTypes value : values) {
            ret.add(value.toString());
        }
        return ret;
    }
    
    public List<Class<?>> getValuesTypes() {
        List<QueryConditionDataTypes> values = this.getValues();
        List<Class<?>> ret = new ArrayList<>();
        for (QueryConditionDataTypes value : values) {
            ret.add(value.type);
        }
        return ret;
    }
}
