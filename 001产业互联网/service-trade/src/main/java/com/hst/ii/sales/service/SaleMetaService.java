package com.hst.ii.sales.service;

import com.hst.core.dao.IORDao;
import com.hst.core.meta.MetaField;
import com.hst.core.meta.MetaInfo;
import com.hst.core.meta.MetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/13 15:13
 */
@Service
public class SaleMetaService {
    public static String NAME = "listing-sale";

    @Autowired
    MetaRepository repo;

    @Autowired
    IORDao dao;

    List<MetaField> lists = new ArrayList<>();
    List<MetaField> querys = new ArrayList<>();
    Map<String, MetaField> fields = new HashMap<>();

    @PostConstruct
    public void update() throws IllegalAccessException, InvocationTargetException {
        List<MetaField> mfs = new ArrayList<>();
        List<MetaField> lists = new ArrayList<>();
        List<MetaField> querys = new ArrayList<>();

        MetaInfo info = repo.getMetaInfo(NAME);
        List<MetaField> mfs1 = info.getFields();
        mfs1.addAll(mfs);
        this.lists = lists;
        this.querys = querys;
        mfs.forEach(mf->{
            this.fields.put(mf.getName(), mf);
        });
    }

    public List<MetaField> getListFields() {
        return lists;
    }

    public List<MetaField> getQueryFields() {
        return querys;
    }

    public MetaField getField(String n) {
        return fields.containsKey(n) ? fields.get(n) : null;
    }

}
