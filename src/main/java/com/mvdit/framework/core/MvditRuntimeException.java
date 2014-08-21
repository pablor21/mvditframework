/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.core;

import java.sql.SQLException;

/**
 *
 * @author Pablo Ramírez
 */
public class MvditRuntimeException extends RuntimeException {
    protected String message;
    
    public MvditRuntimeException(){
        super();
    }
    
    public MvditRuntimeException(Exception ex){
        super(ex);
        this.message= ex.getMessage();
    }

    public MvditRuntimeException(SQLException ex) {
        super(ex);
        this.message= ex.getMessage();
        
    }
    
    public MvditRuntimeException(String message){
        super(message);
        this.message= message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message){
        this.message= message;
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
    
    
    
}
