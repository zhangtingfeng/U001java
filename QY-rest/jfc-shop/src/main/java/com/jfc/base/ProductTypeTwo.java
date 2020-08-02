package com.jfc.base;

import java.util.List;

public class ProductTypeTwo {
    private String id;
    private String name;
    private String pid;
    private List<ProductTypeThree> list;

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

    public List<ProductTypeThree> getList() {
        return list;
    }

    public void setList(List<ProductTypeThree> list) {
        this.list = list;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
