/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database;

/**
 *
 * @author Pablo Ramírez
 */
public class DBResponse {
    private String commandStr;
    private Integer errorCode;
    private String error;
    
    public DBResponse(){
        this.commandStr="";
        this.errorCode=0;
        this.error="";
    }

    public String getCommandStr() {
        return commandStr;
    }

    public void setCommandStr(String commandStr) {
        this.commandStr = commandStr;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
}
