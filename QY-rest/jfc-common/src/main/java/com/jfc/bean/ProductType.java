package com.jfc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/28.
 */
public class ProductType {
    private String id;
    private String parent_id;
    private String text;
    private String pic;
    private List<ProductType> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ProductType> getList() {
        return list;
    }

    public void setList(List<ProductType> list) {
        this.list = list;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
