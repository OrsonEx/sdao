package com.rambo.sdao.util;

import com.rambo.sdao.test.DB;
import oracle.sql.BLOB;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobUtil {

    private static BlobUtil bu;
    private String env;
    public static BlobUtil getInstance(String env) {
        bu = new BlobUtil();
        bu.env = env;
        return bu;
    }

    public boolean write(String projectId, String taskId, String fileName, String file) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        BLOB blob = null;
        boolean flag = false;
        try {
            conn = DB.getConn();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO BLOB_TEMP VALUES(? ,empty_blob())";
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            ps.setString(2, taskId);
            ps.setString(3, fileName);
            ps.executeUpdate();


            sql = "SELECT ZZ_IMAGE_BLOB FROM PS_ZP_PRJ_WBS_BLOB WHERE ZP_PRJ_ID = ? AND ZZ_SEQ_NUM = ? AND ZZ_FILE_NAME = ? FOR UPDATE";
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            ps.setString(2, taskId);
            ps.setString(3, fileName);
            rs = ps.executeQuery();
            if(rs.next()) {
                blob = (BLOB) rs.getBlob(1);
            }
            InputStream in = new FileInputStream(file);
            OutputStream out = blob.setBinaryStream(1L);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = in.read(buffer)) != -1){
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            conn.commit();
            conn.setAutoCommit(true);
            flag = true;
        }
        catch(Exception e) {
            if(conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * <p>根据项目Id，任务Id，文件名读取数据库blob字段文件，写入指定的文件路径</p>
     * @param projectId
     * @param taskId
     * @param fileName
     * @param file
     * @return 返回是否成功
     */
    public boolean read(String projectId, String taskId, String fileName, String file) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        BLOB blob = null;
        boolean flag = false;
        try {
            conn = DB.getConn();
            String sql = "SELECT ZZ_IMAGE_BLOB FROM PS_ZP_PRJ_WBS_BLOB WHERE ZP_PRJ_ID = ? AND ZZ_SEQ_NUM = ? AND ZZ_FILE_NAME = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            ps.setString(2, taskId);
            ps.setString(3, fileName);
            rs = ps.executeQuery();
            if(rs.next()) {
                blob = (BLOB) rs.getBlob(1);
            }
            InputStream in = blob.getBinaryStream();
            byte[] buf = new byte[1024];
            int bytesIn = 0;
            FileOutputStream out = new FileOutputStream(file);
            while ((bytesIn = in.read(buf, 0, 1024)) != -1) {
                out.write(buf, 0, bytesIn);
            }
            in.close();
            out.close();
            flag = true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        BlobUtil bu = BlobUtil.getInstance("MSDEV");
        System.out.println(bu.write("CB", "001", "image1", "D:\\61e44b02jw1dw4xbp2zo6j.jpg"));
        System.out.println(bu.read("CB", "001", "image1", "D:\\2.jpg"));
    }

}