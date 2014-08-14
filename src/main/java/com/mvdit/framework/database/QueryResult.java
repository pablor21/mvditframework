/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 */
public class QueryResult {
    private List<Map<String, Object>> data;
    private int affectedRows;
    private int lastInsertId;
    
    public boolean isEmpty(){
        return this.data==null || this.data.isEmpty();
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public int getLastInsertId() {
        return lastInsertId;
    }

    public void setLastInsertId(int lastInsertId) {
        this.lastInsertId = lastInsertId;
    }
    
}
