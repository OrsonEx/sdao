package com.rambo.sdao;


import com.rambo.sdao.util.Configuration;

import java.sql.*;

public class DB {
    static {
        try {
            String dateSourceType = Configuration.getPropertyParam("DateSourceType");
            Class.forName(Configuration.getPropertyParam(dateSourceType + ".driverClassName"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        Connection conn = null;
        try {
            String url = Configuration.getPropertyParam(Configuration.getPropertyParam("DateSourceType") + ".url");
            String userName = Configuration.getPropertyParam(Configuration.getPropertyParam("DateSourceType") + ".username");
            String pwd = Configuration.getPropertyParam(Configuration.getPropertyParam("DateSourceType") + ".password");
            conn = DriverManager.getConnection(url, userName, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Statement createStmt(Connection conn) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stmt;
    }

    public static PreparedStatement createPreparedStmt(Connection conn, String sql) {
        PreparedStatement pStmt = null;
        try {
            pStmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pStmt;
    }

    public static PreparedStatement createPreparedStmt(Connection conn, String sql, boolean generatedKey) {
        PreparedStatement pStmt = null;
        try {
            pStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pStmt;
    }

    public static ResultSet getRs(Statement stmt, String sql) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet getRs(Connection conn, String sql) {
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void executeUpdate(Connection conn, String sql) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.close(stmt);
        }
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(PreparedStatement pStmt) {
        try {
            if (pStmt != null) {
                pStmt.close();
                pStmt = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            //getConnection
            String url = "jdbc:mysql://localhost/db_nxwb?useUnicode=true&characterEncoding=UTF-8";
            String userName = "root";
            String pwd = "123456";
            Connection conn = DriverManager.getConnection(url, userName, pwd);

            //创建 prepareStatement/createStatement
            String sqlStatement = "select * from tmp_services where uuid ='c9ea709cb30d4954a33dfec01d3ef142'";
            Statement statement = conn.createStatement();

            String sqlPrepared = "select * from tmp_services where uuid = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlPrepared);
            preparedStatement.setString(1, "c9ea709cb30d4954a33dfec01d3ef142");

            //executeQuery
            ResultSet resultSetStatement = statement.executeQuery(sqlStatement);
            ResultSet resultSetPrepared = preparedStatement.executeQuery();

            //遍历结果
            while (resultSetPrepared.next()) {
                System.out.println("on：" + resultSetPrepared.getRow());
                System.out.println("uuid:" + resultSetPrepared.getString("uuid") + ",name:" + resultSetPrepared.getString("name"));
            }

            /*long statrTime = System.currentTimeMillis();
            Connection conn = getConn();
            PreparedStatement preparedStmt = DB.createPreparedStmt(conn, "select * from tmp_services where uuid =?");
            preparedStmt.setString(1, "c9ea709cb30d4954a33dfec01d3ef142");
            ResultSet resultSet = preparedStmt.executeQuery();


//            ResultSet resultSet1 = DB.getRs(conn, "select * from cms_news");
            while (resultSet.next()) {
                System.out.println("on：" + resultSet.getRow());
                System.out.println("uuid:" + resultSet.getString("uuid") + ",name:" + resultSet.getString("name"));
            }
            DB.close(conn);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
