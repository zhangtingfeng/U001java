package com.hst.ii.user.proxy;

import java.util.List;
import java.util.Map;

import com.hst.core.dao.ORQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.hst.core.Page;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.meta.proxies.DefaultDataProxy;

public class UserOrgProxy extends DefaultDataProxy {

    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    public Page<?> list(Map<String, ?> query, int pageIdx) {
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.notnull, "orgId", ""));
        Page<?> p = this.dao.list(this.getEntityClass(), querys, pageIdx);
        p.setData2(this.convert2MetaData(p.getData()));
        return p;
    }

    public List<?> list(Map<String, ?> query) {
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.notnull, "orgId", ""));
        return this.convert2MetaData(this.dao.list(this.getEntityClass(), querys));
    }

}
