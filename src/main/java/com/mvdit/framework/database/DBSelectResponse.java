/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database;

import java.util.List;

/**
 *
 * @author Pablo Ramírez
 */
public class DBSelectResponse extends DBResponse{
    private int totalAbs;
    private int total;
    private List data;
    
    public DBSelectResponse(){
        this.total=-1;
        this.totalAbs= -1;
    }

    public int getTotalAbs() {
        return totalAbs;
    }

    public void setTotalAbs(int totalAbs) {
        this.totalAbs = totalAbs;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
    
    
}
