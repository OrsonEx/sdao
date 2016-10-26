package com.rambo.sdao.test;

import com.rambo.sdao.annotation.TableName;
import com.rambo.sdao.exception.DataOptException;
import com.rambo.sdao.pojo.EjbService;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

/**
 * Create by rambo on 2016/5/10
 **/
public class Selecter {
    public <T> List<T> find(Class<T> clazz, Map param, Connection connection) throws DataOptException {
        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName == null) {
            throw new DataOptException("A1-308", "没有找到类[" + clazz.getName() + "]所映射的表！");
        } else {
            String sql = "SELECT * FROM " + tableName.value() + " WHERE 1=1 ";
            ArrayList paras = new ArrayList();

            if (param != null) {
                Iterator<Map.Entry<String, Object>> iterator = param.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    sql = sql + " AND " + entry.getKey() + " = ?";
                    paras.add(entry.getValue());
                }
            }
            return this.select(connection, sql, paras, clazz);
        }
    }

    public <T> List<T> select(Connection connection, String sql, List<Object> paras, Class<T> clazz) throws DataOptException {
        ArrayList retList = new ArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Object bean;
            Iterator iterator;
            if (paras != null) {
                int count = 1;
                iterator = paras.iterator();

                while (iterator.hasNext()) {
                    bean = iterator.next();
                    preparedStatement.setObject(count++, bean);
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            String fname;
            Object value;
            List fields = getCanWriteField(clazz);
            Iterator filedIter = fields.iterator();

            Field field;
            while (filedIter.hasNext()) {
                field = (Field) filedIter.next();
                field.setAccessible(true); //抑制java 对修饰符的检查
            }

            HashSet hashSet = new HashSet();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int i;
            for (i = 1; i <= metaData.getColumnCount(); ++i) {
                hashSet.add(metaData.getColumnLabel(i).toLowerCase());
            }

            for (i = fields.size() - 1; i >= 0; --i) {
                if (!hashSet.contains(((Field) fields.get(i)).getName().toLowerCase())) {
                    fields.remove(i);
                }
            }

            for (; resultSet.next(); retList.add(bean)) {
                bean = clazz.newInstance();
                filedIter = fields.iterator();

                while (filedIter.hasNext()) {
                    field = (Field) filedIter.next();
                    fname = field.getName();
                  /*  if (ClassUtils.isAssignable(field.getType(), Date.class)) {
                        Timestamp timestamp = resultSet.getTimestamp(fname);
                        if (timestamp != null) {
                            field.set(bean, timestamp);
                        }
                        continue;
                    }*/

                    value = resultSet.getObject(fname);
                    if (value != null) {
                        try {
                            field.set(bean, changeCast(field.getType(), value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Throwable e) {
            throw new DataOptException("A1-001", e.getMessage(), e);
        } finally {
            this.closeConnection(connection);
        }
        return retList;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static List<Field> getCanWriteField(Class clazz) {
        ArrayList fields = new ArrayList();
        getCanWriteField(clazz, new HashSet(), fields);
        return fields;
    }

    public static void getCanWriteField(Class clazz, HashSet<String> set, List<Field> fields) {
        Field[] fieldsArray;
        int length = (fieldsArray = clazz.getDeclaredFields()).length;

        for (int i = 0; i < length; ++i) {
            Field superc = fieldsArray[i];
            if (!Modifier.isFinal(superc.getModifiers()) && !Modifier.isStatic(superc.getModifiers()) && !set.contains(superc.getName())) {
                superc.setAccessible(true);
                fields.add(superc);
            }
        }

        Class superclass = clazz.getSuperclass();
        if (!superclass.equals(Object.class)) {
            getCanWriteField(superclass, set, fields);
        }
    }

    public static <T> T changeCast(Class<T> propType, Object tmpobj) throws ParseException {
        return propType.cast(castType(propType, tmpobj));
    }

    public static Object castType(Class propType, Object tmpobj) throws ParseException {
        if (propType.isInstance(tmpobj)) {
            return tmpobj;
        }
        String value = toString(tmpobj, "").trim();
        PropertyEditor pe = PropertyEditorManager.findEditor(propType);
        pe.setAsText(value);
        return pe.getValue();

       /* if (propType.equals(String.class)) {
            return value;
        }

        if (propType.equals(Date.class)) {
            return value.length() == 0 ? null : (value.length() == 8 ? Keys.df3.parse(value) : (NumberUtils.isNumber(value) ? new Date(Long.parseLong(value) * 86400000L + Keys.df4.parse("1900-01-01").getTime()) : (value.length() == 10 ? Keys.df4.parse(value) : (value.length() <= 16 ? Keys.df5.parse(value) : Keys.df7.parse(value)))));
        }

        if (propType.equals(java.sql.Date.class)) {
            return value.length() == 0 ? null : (value.length() == 8 ? new java.sql.Date(Keys.df3.parse(value).getTime()) : (NumberUtils.isNumber(value) ? new java.sql.Date(Long.parseLong(value) * 86400000L + Keys.df4.parse("1900-01-01").getTime()) : (value.length() <= 10 ? new java.sql.Date(Keys.df4.parse(value).getTime()) : new java.sql.Date(Keys.df4.parse(value.substring(0, 10)).getTime()))));
        }

        if (propType.equals(Timestamp.class)) {
            return value.length() == 0 ? null : (value.length() == 8 ? new Timestamp(Keys.df3.parse(value).getTime()) : (NumberUtils.isNumber(value) ? new Timestamp(Long.parseLong(value) * 86400000L + Keys.df4.parse("1900-01-01").getTime()) : (value.length() <= 10 ? new Timestamp(Keys.df4.parse(value).getTime()) : (value.length() <= 16 ? new Timestamp(Keys.df5.parse(value).getTime()) : new Timestamp(Keys.df7.parse(value).getTime())))));
        }

        if (propType.equals(Calendar.class)) {
            Date pd = changeCast(Date.class, tmpobj);
            if (pd != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(pd);
                return cal;
            } else {
                return null;
            }
        }

        if (propType.equals(Character.class) && propType.equals(Character.TYPE)) {
            return value.length() > 0 ? value.charAt(0) : '\u0000';
        }

        if (propType.equals(Byte.class) && propType.equals(Byte.TYPE)) {
            return new Byte(value);
        }

        if (propType.equals(Short.class) && propType.equals(Short.TYPE)) {
            return value.length() > 0 ? (new Short(value)) : 0;
        }

        if (propType.equals(Integer.class) && propType.equals(Integer.TYPE)) {
            return value.length() > 0 ? Integer.valueOf("NaN".equals(value) ? 0 : (value.contains(".") ? new Integer(value.split("\\.")[0]) : new Integer(value))) : Integer.valueOf(0);
        }

        if (propType.equals(Long.class) && propType.equals(Long.TYPE)) {
            return value.length() == 0 ? Long.valueOf(0L) : Long.valueOf("NaN".equals(value) ? 0L : (value.contains(".") ? new Long(value.split("\\.")[0]) : new Long(value)));
        }


        if (propType.equals(Float.class) && propType.equals(Float.TYPE)) {
            return "NaN".equals(value) ? 0.0F : (value.trim().length() == 0 ? 0.0F : (new Float(value)));
        }

        if (propType.equals(Double.class) && propType.equals(Double.TYPE)) {
            return "NaN".equals(value) ? 0.0D : (value.trim().length() == 0 ? 0.0D : new Double(value));
        }

        if (propType.equals(Boolean.class) && propType.equals(Boolean.TYPE)) {
            return value.trim().length() != 0 && Boolean.parseBoolean(value);
        }
        return tmpobj;*/
    }

    public static String toString(Object obj, String defsv) {
        return obj == null ? defsv : (obj instanceof Date ? Keys.df5.format((Date) obj) : (obj instanceof String ? (String) obj : (obj instanceof Number ? obj.toString() : obj.toString())));
    }

    public static void main(String[] args) {
        Selecter selecter = new Selecter();
        Connection conn = DB.getConn();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            List<EjbService> ejbServiceList = selecter.find(EjbService.class, null, conn);

            System.out.println("花费时间：" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
/*
            for (EjbService ejbService : ejbServiceList) {
                System.out.println(ejbService.getName() + "," + ejbService.getTheme());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}