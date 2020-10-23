package com.hst.ii.company.service;

import com.hst.core.meta.MetaConvert;
import com.hst.core.meta.MetaRepository;
import com.hst.core.meta.proxies.DefaultDataProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/1 14:45
 */
@Service
public class ProxyService extends DefaultDataProxy {

    @Autowired
    private MetaRepository mr;

    public Object convert2Entity(Object o,String name) {
        MetaConvert mc = mr.getMetaConvert(name);
        if (null == mc)
            return o;
        return mc.toEntity(o);
    }

    public Object convert2MetaData(Object o,String name) {
        MetaConvert mc = mr.getMetaConvert(name);
        if (null == mc)
            return o;
        return mc.toMetaData(o);
    }


}
