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
    
    public MvditRuntimeException(Exception ex){
        super(ex);
    }

    public MvditRuntimeException(SQLException ex) {
        super(ex);
    }
    
    public MvditRuntimeException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        /*try{
            MvditApp.getInstance().getLogger().error(this);
        }catch(Throwable ex){
            
        }*/
        return super.getMessage();
    }
    
    
    
}
