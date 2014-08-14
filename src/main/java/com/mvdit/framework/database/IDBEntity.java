/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database;

import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 * @param <K>
 */
public interface IDBEntity<K> {
    public String getTableName();
    public String getPrimaryKeyName();
    public K getId();
    public void setId(K id);
    public Map<String, Object> toSqlArray();
    public void fillFromSqlArray(Map<String, Object> data);
}
