package com.hst.ii.process.proxy;

import com.hst.core.Page;
import com.hst.core.meta.DataProxy;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.proxies.DefaultDataProxy;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/22 23:58
 */
public class NodeUserProxy  extends DefaultDataProxy {


    @Override
    public Page<?> list(Map<String, ?> query, int pageIdx) {
        return null;
    }

    @Override
    public List<?> list(Map<String, ?> query) {
        return null;
    }

    @Override
    public Object save(MetaData data) throws Exception {
        return null;
    }

    @Override
    public Object del(MetaData data) throws Exception {
        return null;
    }
}

