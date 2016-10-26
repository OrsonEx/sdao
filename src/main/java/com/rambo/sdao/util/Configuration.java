package com.rambo.sdao.util;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Create by rambo on 2016/3/10
 **/
public class Configuration {
    private static String getConfigFileParam(String key, String filePath) {
        String theValue = "";
        Properties prop = new Properties();
        String url = Configuration.class.getClassLoader().getResource("/").getPath().replaceAll("%20", " ");
        String path = url.substring(0, url.indexOf("classes")) + filePath;
        InputStream in = null;
        BufferedReader bf = null;
        try {
            in = new FileInputStream(path);
            bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            prop.load(bf);
            if (prop.containsKey(key))
                theValue = prop.getProperty(key).trim();
            prop.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return theValue;
    }

    //方式2：
    public static String getPropertyParam(String key) {
        String value = "";
        try {
            //配置文件需要存在与src目录下面
            URL resource = Thread.currentThread().getContextClassLoader().getResource("dataSources.properties");
            Properties properties = new Properties();
            if (resource != null) {
                properties.load(resource.openStream());
                value = properties.get(key).toString();
            } else
                System.out.println("无法读取该" + key + ",配置文件中不存在！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
