package com.gsc.arcgis;

import java.io.Serializable;

public class DataBean implements Serializable {
    private int id;
    private String name;
    private int type;
    private String image;
    private String creat_date;
    private String update_date;
    private String point;
    private String remark;

    public DataBean(int id, String name, int type, String image, String creat_date, String update_date, String point, String remark) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
        this.creat_date = creat_date;
        this.update_date = update_date;
        this.point = point;
        this.remark = remark;
    }

    public DataBean(int type, String point) {
        this.type = type;
        this.point = point;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreat_date() {
        return creat_date;
    }

    public void setCreat_date(String creat_date) {
        this.creat_date = creat_date;
    }
}
