package com.rambo.sdao.pojo;

import com.rambo.sdao.annotation.TableName;

import java.util.UUID;

/**
 * Create by rambo on 2016/3/9
 **/
@TableName("ejb_service")
public class EjbService {
    private String id;
    private String name;
    private String theme;
    private String type;
    private String descrition;
    private String XSD;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public String getXSD() {
        return XSD;
    }

    public void setXSD(String XSD) {
        this.XSD = XSD;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInsertStr() {
        return "('" + getUUID() + "','" + id.trim() + "','" + name.trim() + "','" + theme.trim() + "','" + type.trim()
                + "','" + descrition.trim() + "','" + XSD.trim() + "','" + remark.trim() + "')";
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
