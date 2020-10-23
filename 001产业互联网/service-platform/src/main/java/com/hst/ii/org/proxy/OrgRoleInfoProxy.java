package com.hst.ii.org.proxy;

import com.hst.core.Page;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.meta.proxies.DefaultDataProxy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class OrgRoleInfoProxy extends DefaultDataProxy {

    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    @Override
    public List<?> list(Map<String, ?> query) {
        List<?> lists = sql.query("org.getOrgRoleInfo", query);
        return lists;
    }

    @Override
    public Page<?> list(Map<String, ?> query, int pageIdx) {
        Page<?> page = sql.list("org.getOrgRoleInfo", query, pageIdx, Page.getPageSize());
        return page;
    }

}
