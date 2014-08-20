/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.data;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Pablo Ramírez
 */
public class QueryCondition {

    protected QueryConditionComparators comparator;
    protected QueryConditionOperators operator;
    protected String id;
    protected String field;
    protected List<QueryCondition> valuesList;
    protected Object singleValue;
    protected QueryConditionTypes type;
    protected QueryConditionDataTypes dataType;
    protected boolean ignoreOnPlain;

    public QueryCondition(String id, String field, QueryConditionComparators comparator, QueryConditionOperators operator) {
        this.comparator = comparator;
        this.operator = operator;
        this.id = id;
        this.field = field;
        this.valuesList = new ArrayList<>();
        this.singleValue = null;
        this.type = QueryConditionTypes.SINGLE;
        this.dataType = QueryConditionDataTypes.OBJECT;
        this.ignoreOnPlain = false;
    }

    public QueryCondition() {
        this("", "", QueryConditionComparators.NONE, QueryConditionOperators.NONE);
    }

    public QueryCondition(QueryCondition src) {
        this.comparator = src.comparator;
        this.operator = src.operator;
        this.id = src.id;
        this.field = src.field;
        this.valuesList = src.valuesList;
        this.singleValue = src.singleValue;
        this.type = src.type;
        this.dataType = src.dataType;
        this.ignoreOnPlain = src.ignoreOnPlain;
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

    public String getId() {
        if (MvditUtils.stringEmpty(this.id)) {
            this.id = this.field;
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIgnoreOnPlain(boolean ignoreOnPlain) {
        this.ignoreOnPlain = ignoreOnPlain;
    }

    public QueryConditionDataTypes getDataType() {
        return dataType;
    }

    public void setDataType(QueryConditionDataTypes dataType) {
        this.dataType = dataType;
    }

    public Class<?> getTypeOfData() {
        if (this.type != QueryConditionTypes.SINGLE) {
            return null;
        }
        return this.dataType.getClass();
    }

    public List<QueryCondition> getValuesList() {
        if (this.type != QueryConditionTypes.MULTIPLE) {
            return null;
        }
        return valuesList;
    }

    public void setValuesList(List<QueryCondition> values) {
        this.type = QueryConditionTypes.MULTIPLE;
        this.singleValue = null;
        this.valuesList = values;
    }

    public void addValueList(QueryCondition... values) {
        this.type = QueryConditionTypes.MULTIPLE;
        this.singleValue = null;
        this.valuesList.addAll(Arrays.asList(values));
    }

    public void addCondition(QueryCondition cond) {
        if (this.type != QueryConditionTypes.MULTIPLE) {
            this.valuesList = new ArrayList<>();
        }
        this.valuesList.add(cond);
    }

    public void addCondition(String id, String fieldName, QueryConditionComparators comparator, Object value, QueryConditionOperators operator) {
        QueryCondition cond = new QueryCondition(id, field, comparator, operator);
        cond.setSingleValue(value);
        this.addCondition(cond);
    }

    public void addCondition(String id, String fieldName, QueryConditionComparators comparator, List<QueryCondition> values, QueryConditionOperators operator) {
        QueryCondition cond = new QueryCondition(id, field, comparator, operator);
        cond.setValuesList(values);
        this.addCondition(cond);
    }

    public Object getSingleValue() {
        if (this.type != QueryConditionTypes.SINGLE) {
            return null;
        }

        return formatSingleValue();
    }

    protected Object formatSingleValue() {
        switch (this.dataType) {
            case OBJECT:
                return this.singleValue;
            case BIGDECIMAL:
                return MvditUtils.parseStringToBigDecimal(this.singleValue.toString());
            case BIGINTEGER:
               return MvditUtils.parseStringToBigInteger(this.singleValue.toString());
            case DATE:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return sdf.parse(this.singleValue.toString());
                } catch (ParseException pex) {
                    throw new MvditRuntimeException(pex);
                }
            case DATE_TIME:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    return sdf.parse(this.singleValue.toString());
                } catch (ParseException pex) {
                    throw new MvditRuntimeException(pex);
                }
            case STRING:
                String val = this.singleValue.toString();
                switch (this.comparator) {
                    case ENDS_WITH:
                    case EW:
                        val = "%" + val;
                        break;
                    case CONTAINS:
                    case CN:
                        val = "%" + val + "%";
                        break;
                    case STARTS_WITH:
                    case SW:
                        val = val + "%";
                        break;
                }
                return val;
            default:
                return this.dataType.type().cast(this.singleValue);
        }
    }

    public void setSingleValue(Object singleValue) {
        this.type = QueryConditionTypes.SINGLE;
        this.valuesList = new ArrayList<>();
        this.singleValue = singleValue;
    }

    public QueryConditionTypes getType() {
        return type;
    }

    public void setType(QueryConditionTypes type) {
        this.type = type;
    }

    @JsonIgnore
    public boolean isValid() {
        //return !MvditUtils.stringEmpty(field);
        return true;
    }

    public String getSentenceStr(String entityQualifier, boolean includeOperator) {
        return getSentenceStr(entityQualifier, "", includeOperator);
    }

    @JsonIgnore
    public String getSentenceStr(String entityQualifier, String keyPrepend, boolean includeOperator) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        if (includeOperator) {
            sb.append((!MvditUtils.stringEmpty(this.operator.toString())) ? this.operator : QueryConditionOperators.AND);
            sb.append(" ");
        }
        if (this.type == QueryConditionTypes.SINGLE) {
            if (!MvditUtils.stringEmpty(entityQualifier)) {
                sb.append(entityQualifier)
                        .append(".");
            }
            sb.append(this.field);
            sb.append(" ");
            sb.append(this.comparator);
            sb.append(" ");
            if (this.comparator == QueryConditionComparators.NOT_NULL
                    || this.comparator == QueryConditionComparators.IS_NULL) {

            } else {
                sb.append(":");
                if (!MvditUtils.stringEmpty(keyPrepend)) {
                    sb.append(keyPrepend);
                    sb.append("_");
                }
                sb.append(this.getId());
            }

        } else if (this.type == QueryConditionTypes.MULTIPLE) {
            sb.append("(");
            boolean incOp = false;
            for (QueryCondition qc : this.valuesList) {
                sb.append(qc.getSentenceStr(entityQualifier, this.getId(), incOp));
                incOp = true;
            }
            sb.append(")");
        }
        sb.append(" ");
        return sb.toString();
    }

    @JsonIgnore
    public List<QueryCondition> getPlainConditions(String prependKey) {
        List<QueryCondition> conditions = new ArrayList<>();
        if (!this.ignoreOnPlain) {
            QueryCondition copy = new QueryCondition(this);
            if (!MvditUtils.stringEmpty(prependKey)) {
                copy.id = prependKey + "_" + copy.id;
            }
            if (this.type == QueryConditionTypes.SINGLE) {
                conditions.add(copy);
            } else if (this.type == QueryConditionTypes.MULTIPLE) {
                for (QueryCondition qc : this.valuesList) {
                    conditions.addAll(qc.getPlainConditions(copy.id));
                }
            }

        }
        return conditions;
    }

    @JsonIgnore
    public Map<String, Object> getParametersValues(String keyPrepend) {
        Map<String, Object> ret = new HashMap<>();
        String key = this.getId();
        if (!MvditUtils.stringEmpty(keyPrepend)) {
            key = keyPrepend + "_" + this.getId();
        }
        if (this.type == QueryConditionTypes.SINGLE) {
            if (this.comparator == QueryConditionComparators.NOT_NULL
                    || this.comparator == QueryConditionComparators.IS_NULL) {

            } else {
                Object value = formatSingleValue();
                ret.put(key, value);
            }
        } else if (this.type == QueryConditionTypes.MULTIPLE) {
            for (QueryCondition qc : this.valuesList) {
                ret.putAll(qc.getParametersValues(key));
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return this.operator + " " + this.toStringNotOperator();

    }

    public String toStringNotOperator() {
        return "";
        //return this.field + " " + this.comparator + " " + this.singleValue;
    }
}
