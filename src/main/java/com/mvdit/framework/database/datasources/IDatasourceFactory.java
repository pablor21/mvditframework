/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database.datasources;

import com.mvdit.framework.core.PropertiesRepository;
import javax.sql.DataSource;

/**
 *
 * @author Pablo Ramírez
 */
public interface IDatasourceFactory {
    DataSource initDatasource(String dbId, PropertiesRepository props);
}
