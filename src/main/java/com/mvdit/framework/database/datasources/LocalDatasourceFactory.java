/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database.datasources;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.PropertiesRepository;
import javax.sql.DataSource;
import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

/**
 *
 * @author Pablo Ramírez
 */
public class LocalDatasourceFactory implements IDatasourceFactory{

    @Override
    public DataSource initDatasource(String dbId, PropertiesRepository props) {
        DriverAdapterCPDS cpds = new DriverAdapterCPDS();
        try {
            cpds.setDriver(props.getProperty(dbId + ".driver", JDBCDatasource.DATABASES_FILE));
        } catch (ClassNotFoundException e) {
            throw new MvditRuntimeException(e);
        }
        String url = props.getProperty(dbId + ".url", JDBCDatasource.DATABASES_FILE);
        cpds.setUrl(url);
        cpds.setUser(props.getProperty(dbId + ".username", JDBCDatasource.DATABASES_FILE));
        cpds.setPassword(props.getProperty(dbId + ".password", JDBCDatasource.DATABASES_FILE));

        SharedPoolDataSource tds = new SharedPoolDataSource();
        tds.setConnectionPoolDataSource(cpds);
        tds.setMaxActive(Integer.valueOf(props.getProperty(dbId + ".pool.maxActive", JDBCDatasource.DATABASES_FILE)));
        tds.setMaxWait(Integer.valueOf(props.getProperty(dbId + ".pool.maxWait", JDBCDatasource.DATABASES_FILE)));

        DataSource ds = tds;
        return ds;
    }
    
}
