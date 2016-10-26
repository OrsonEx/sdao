package com.rambo.sdao.test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by rambo on 2016/3/10
 **/
public class DBWrapper {
    public static Map<String, Object> sqlQueryOneRow(Connection conn, String sql, Object[] params) {
        return sqlQuery(conn, sql, params).get(0);
    }

    public static List<Map<String, Object>> sqlQuery(Connection conn, String sql, Object[] params) {
        List<Map<String, Object>> retList = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement(sql);
            if (params != null) {
                int i = 0;
                for (Object o : params) {
                    stm.setObject(++i, o);
                }
            }
            rs = stm.executeQuery();
            retList = new ArrayList<>();
            Map<String, Object> row;
            ResultSetMetaData rsmd = rs.getMetaData();
            String label;
            while (rs.next()) {
                row = new HashMap<>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    label = rsmd.getColumnLabel(i + 1).toLowerCase();
                    row.put(label, rs.getObject(label));
                }
                retList.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closePreparedStatement(stm);
            closeConnection(conn);
        }
        return retList;
    }

    public static void sqlUpdate(Connection conn, String sql, Object[] params) {
        PreparedStatement stm = null;
        try {
            stm = conn.prepareCall(sql);
            if (params != null) {
                int i = 0;
                for (Object o : params) {
                    stm.setObject(++i, o);
                }
            }
            stm.executeUpdate();
        } catch (Exception e) {
             e.printStackTrace();
        } finally {
            closePreparedStatement(stm);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null)
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
