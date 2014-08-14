/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.database.drivers;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.database.DBInsertResponse;
import com.mvdit.framework.database.DBSelectResponse;
import com.mvdit.framework.database.DBUpdateResponse;
import com.mvdit.framework.database.DefaultResponseType;
import com.mvdit.framework.database.IDBDriver;
import com.mvdit.framework.database.IDBEntity;
import com.mvdit.framework.database.QueryResult;
import com.mvdit.framework.database.SqlLogger;
import com.mvdit.framework.database.TypeOfInsert;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 */
public class MySqlDriver implements IDBDriver {

    protected Connection connection;
    protected SqlLogger logger;
    protected String dbId;

    public MySqlDriver() {
    }

    public MySqlDriver(String dbId, SqlLogger logger) {
        //this.connect(dbId, logger);
    }

    @Override
    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    @Override
    public Connection getConnection() {
        //return JDBCDatasource.getInstance().getConnection(dbId);
        return this.connection;
    }

    @Override
    public SqlLogger getLogger() {
        return logger;
    }

    @Override
    public void setLogger(SqlLogger logger) {
        this.logger = logger;
    }

    @Override
    public String getDriverClassName() {
        return this.getClass().getName();
    }

    private void addToLog(String sql) {
        if (this.logger != null) {
            this.logger.addToLog(sql);
        }
    }

    public String toSql(String sqlStr) {
        return sqlStr;
    }

    private QueryResult _query(String sql, Collection<Object> parameters) throws SQLException {
        this.addToLog(sql);
        QueryResult result = new QueryResult();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = this.getConnection();
        try {
            if (sql.matches(".*(UPDATE|INSERT|DELETE|REPLACE).*")) {
                stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                for (Object param : parameters) {
                    this.setParameterOfQuery(stmt, index, param);
                    index++;
                }
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    result.setLastInsertId(rs.getInt(1));
                }
                //System.out.println(stmt.toString());
                result.setAffectedRows(stmt.getUpdateCount());
                result.setData(this.getResultOfResultSet(rs));

                //stmt.close();
            } else {
                stmt = conn.prepareStatement(sql);
                int index = 1;
                for (Object param : parameters) {
                    this.setParameterOfQuery(stmt, index, param);
                    index++;
                }
                rs = stmt.executeQuery();
                result.setData(this.getResultOfResultSet(rs));
                //stmt.close();
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {

            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {

            }

        }
        return result;
    }

    private void setParameterOfQuery(PreparedStatement stmt, int index, Object obj) throws SQLException {
        if (stmt != null) {
            if (obj instanceof String) {
                stmt.setString(index, obj.toString());
            } else if (obj instanceof Integer) {
                stmt.setInt(index, (int) obj);
            } else if (obj instanceof BigDecimal) {
                stmt.setBigDecimal(index, (BigDecimal) obj);
            } else if (obj instanceof Short) {
                stmt.setShort(index, (short) obj);
            } else if (obj instanceof Double) {
                stmt.setDouble(index, (double) obj);
            } else if (obj instanceof Long) {
                stmt.setLong(index, (long) obj);
            } else if (obj instanceof Double) {
                stmt.setDouble(index, (double) obj);
            } else if (obj instanceof Date) {
                stmt.setDate(index, new java.sql.Date(((Date) obj).getTime()));
            } else if (obj instanceof Array) {
                stmt.setArray(index, (Array) obj);
            } else if (obj instanceof Boolean) {
                stmt.setBoolean(index, (boolean) obj);
            } else {
                stmt.setObject(index, obj);
            }
        }
    }

    private List<Map<String, Object>> getResultOfResultSet(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        if (rs != null) {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            while (rs.next()) {
                HashMap row = new HashMap(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(row);
            }
        }
        return list;
    }

    @Override
    public QueryResult query(String sql, Object... parameters) {
        QueryResult result = null;
        try {
            List<Object> params = new ArrayList<>(Arrays.asList(parameters));
            return this._query(sql, params);
        } catch (SQLException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    @Override
    public DBSelectResponse select(String tableName, String conditions, Collection<Object> pramsValues, String fields, Boolean countTotal, Class responseType, Object... parameters) {
        DBSelectResponse resp = new DBSelectResponse();
        if (!MvditUtils.stringEmpty(tableName)) {
            fields = (!MvditUtils.stringEmpty(fields)) ? fields : "*";
            conditions = (!MvditUtils.stringEmpty(conditions)) ? conditions : " ";
            String sql = "SELECT " + fields + " FROM " + tableName + " " + conditions;
            //List<Object> params = new ArrayList<>(Arrays.asList(parameters));
            QueryResult qr = null;
            try {
                qr = this._query(sql, pramsValues);
                //List list = this.getResultOfResultSet(rs);
                int total = 0;
                int totalAbs = -1;

                //verifico si se quiere saber el total de registros
                if (countTotal && conditions.matches("(?i)(.+)?LIMIT +[0-9]+( *, *[0-9]+)?.+$")) {
                    QueryResult qr2 = null;
                    try {
                        if (fields.toUpperCase().matches(".*DISTINCT.*")) {
                            qr = this._query("SELECT SQL_CALC_FOUND_ROWS  " + fields + " FROM  " + tableName + " " + conditions, pramsValues);
                            //obtengo el total de registros
                            qr2 = this._query("SELECT FOUND_ROWS() as totalAbs", null);
                        } else {
                            conditions = conditions.replaceAll("(?i)(ORDER +BY[a-z, \\._]+)?LIMIT *([0-9]+ *, *)?[0-9]+", "");
                            qr2 = this._query("SELECT count(*) as totalAbs FROM " + tableName + " " + conditions, pramsValues);
                        }
                        if (!qr2.isEmpty()) {
                            totalAbs = new Long((long) qr2.getData().get(0).get("totalAbs")).intValue();
                        }
                    } finally {
                    }
                }
                List<Map<String, Object>> data = qr.getData();
                total = data.size();

                resp.setCommandStr(sql);
                resp.setTotal(total);
                resp.setTotalAbs(totalAbs);

                if (responseType == DefaultResponseType.class) {
                    resp.setData(data);
                } else {

                    List finalData = new ArrayList();
                    for (Map<String, Object> obj : data) {
                        if (responseType != null) {
                            Object raw = responseType.newInstance();
                            if (raw instanceof IDBEntity) {
                                IDBEntity entity = (IDBEntity) raw;
                                entity.fillFromSqlArray(obj);
                                finalData.add(entity);
                            }
                        } else {
                            finalData.add(obj);
                        }

                        //System.out.println(obj);
                    }
                    resp.setData(finalData);
                }

            } catch (SQLException ex) {
                resp.setError(ex.getMessage());
                resp.setErrorCode(ex.getErrorCode());
                return resp;
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new MvditRuntimeException(ex);
            } finally {

            }

        } else {
            resp.setError("No se ha indicado una tabla para hacer la consulta");
            resp.setErrorCode(-1);
        }
        return resp;
    }

    @Override
    public DBSelectResponse select(String tableName, String conditions, String fields, Boolean countTotal, Class responseType, Object... parameters) {
        return this.select(tableName, conditions, new ArrayList<>(), fields, countTotal, responseType, parameters);
    }

    @Override
    public DBInsertResponse insert(String tableName, Map<String, Object> data) {
        return this.insert(tableName, data, TypeOfInsert.INSERT);
    }

    @Override
    public DBInsertResponse insert(String tableName, Map<String, Object> data, TypeOfInsert type) {
        DBInsertResponse resp = new DBInsertResponse();
        if (!MvditUtils.stringEmpty(tableName) && data != null) {
            StringBuilder sql = new StringBuilder(type.name() + " INTO " + tableName + " (`");
            List<String> fields = new ArrayList<>(data.keySet());
            List<Object> values = new ArrayList<>();
            for (String key : fields) {
                Object obj = data.get(key);
                if (obj == null) {
                    values.add(null);
                } else {
                    values.add(obj);
                }
            }
            sql.append(String.join("`,`", fields));
            sql.append("`) VALUES (");
            boolean isFirst = true;
            for (Object obj : values) {
                if (!isFirst) {
                    sql.append(",");
                }
                sql.append("?");
                isFirst = false;
            }
            sql.append(")");
            QueryResult qr = null;
            try {
                qr = this._query(sql.toString(), values);
                resp.setAffectedRows(qr.getAffectedRows());
                resp.setLastInsertId(qr.getLastInsertId());

            } catch (SQLException ex) {
                throw new MvditRuntimeException(ex);
            } finally {

            }
            resp.setCommandStr(sql.toString());

        }
        return resp;
    }

    @Override
    public DBInsertResponse replace(String tableName, Map<String, Object> data) {
        return this.insert(tableName, data, TypeOfInsert.REPLACE);
    }

    @Override
    public DBUpdateResponse delete(String tableName, String conditions, Object... parameters) {
        DBUpdateResponse resp = new DBUpdateResponse();
        if (!MvditUtils.stringEmpty(tableName) && !MvditUtils.stringEmpty(conditions)) {
            List<Object> params = new ArrayList<>(Arrays.asList(parameters));
            StringBuilder sql = new StringBuilder("DELETE FROM " + tableName + " " + conditions);
            QueryResult qr = null;
            try {

                qr = this._query(sql.toString(), params);
                resp.setAffectedRows(qr.getAffectedRows());

            } catch (SQLException ex) {
                throw new MvditRuntimeException(ex);
            } finally {

            }
            resp.setCommandStr(sql.toString());
        }
        return resp;
    }

    @Override
    public DBUpdateResponse update(String tableName, Map<String, Object> data, String conditions, Object... parameters) {
        DBUpdateResponse resp = new DBUpdateResponse();
        if (!MvditUtils.stringEmpty(tableName) && !MvditUtils.stringEmpty(conditions) && data != null) {
            StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
            List<String> fields = new ArrayList<>(data.keySet());
            List<String> fieldsNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (String key : fields) {
                Object obj = data.get(key);
                if (obj == null) {
                    values.add(null);
                } else {
                    values.add(obj);
                }
            }

            for (Object obj : parameters) {
                if (obj == null) {
                    values.add(null);
                } else {
                    values.add(obj);
                }
            }

            for (String key : fields) {
                fieldsNames.add("`" + key + "`=?");
            }

            sql.append(String.join(", ", fieldsNames));
            sql.append(" ");
            sql.append(conditions);
            QueryResult qr = null;
            try {
                qr = this._query(sql.toString(), values);
                resp.setAffectedRows(qr.getAffectedRows());

            } catch (SQLException ex) {
                throw new MvditRuntimeException(ex);
            } finally {

            }
            resp.setCommandStr(sql.toString());
        }
        return resp;
    }

    @Override
    public void beginTransaction() {
        try {
            this.getConnection().setAutoCommit(false);
        } catch (SQLException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    @Override
    public void setAutoCommit() {
        try {
            this.getConnection().commit();
        } catch (SQLException ex) {
            //throw new MvditRuntimeException(ex);
        }
        try {
            this.getConnection().setAutoCommit(true);
        } catch (SQLException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    @Override
    public void commitTransaction() {
        try {
            this.getConnection().commit();
        } catch (SQLException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            this.getConnection().rollback();
        } catch (SQLException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    @Override
    public void closeConnection() {
        try {

            this.getConnection().close();
        } catch (SQLException ex) {
            //throw new MvditRuntimeException(ex);
        }
    }

    @Override
    public String getLimit(int page, int pageSize) {
        String limit = "";
        if (page > 0 && pageSize > 0) {
            limit = " LIMIT " + ((page - 1) * pageSize) + ", " + pageSize + " ";
        } else if (pageSize > 0) {
            limit = " LIMIT " + pageSize + " ";
        }
        return limit;
    }

}
