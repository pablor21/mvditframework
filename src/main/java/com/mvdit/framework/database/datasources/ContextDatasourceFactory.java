/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database.datasources;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.PropertiesRepository;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Pablo Ramírez
 */
public class ContextDatasourceFactory implements IDatasourceFactory{

    @Override
    public DataSource initDatasource(String dbId, PropertiesRepository props) {
       DataSource ds = null;
        try {
            InitialContext cxt = new InitialContext();
            if (cxt == null) {
                throw new MvditRuntimeException("Cannot find context!");
            }
            ds = (DataSource) cxt.lookup(props.getProperty(dbId + ".url", JDBCDatasource.DATABASES_FILE));
        } catch (NamingException ex) {
            throw new MvditRuntimeException(ex);
        }

        return ds;
    }
    
}
