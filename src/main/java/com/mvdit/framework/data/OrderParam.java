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
public class OrderParam {
    public static final String ORDER_ASC="ASC";
    public static final String ORDER_DESC="DESC";
    
    private String field;
    private String direction;

    public OrderParam() {
        this.setDirection(ORDER_ASC);
    }

    public OrderParam(String orderField, String orderDirection) {
        this.field = orderField;
        this.setDirection(orderDirection);
    }  

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        direction = (!MvditUtils.stringEmpty(direction))?direction:ORDER_ASC;
        if(!direction.equalsIgnoreCase(ORDER_ASC) && !direction.equalsIgnoreCase(ORDER_DESC)){
            direction= ORDER_ASC;
        }
        this.direction= direction;
    }
    public boolean isValid(){
        return !MvditUtils.stringEmpty(this.field)&& !MvditUtils.stringEmpty(this.direction);
    }
}
