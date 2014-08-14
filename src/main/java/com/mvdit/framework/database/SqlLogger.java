/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo Ramírez
 */
public class SqlLogger {
    private List<String> log;

    public SqlLogger() {
        this.log=new ArrayList<>();
    }    
    

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }
    
    
    
    public void addToLog(String msg){
        log.add(msg);
    }
    
    public void clearLog(){
        log=new ArrayList<>();
    }
    
    
}
