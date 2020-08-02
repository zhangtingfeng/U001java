package com.jfc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/28.
 */
public class SeckillProduct {
    private String titleTime;
    private String type;
    private List<Seckills> list;

    public String getTitleTime() {
        return titleTime;
    }

    public void setTitleTime(String titleTime) {
        this.titleTime = titleTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Seckills> getList() {
        return list;
    }

    public void setList(List<Seckills> list) {
        this.list = list;
    }
}
