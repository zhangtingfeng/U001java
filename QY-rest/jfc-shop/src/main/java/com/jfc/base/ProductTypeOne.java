package com.jfc.base;

import java.util.List;

public class ProductTypeOne {
    private String id;
    private String name;
    private List<ProductTypeTwo> list;

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

    public List<ProductTypeTwo> getList() {
        return list;
    }

    public void setList(List<ProductTypeTwo> list) {
        this.list = list;
    }
}
