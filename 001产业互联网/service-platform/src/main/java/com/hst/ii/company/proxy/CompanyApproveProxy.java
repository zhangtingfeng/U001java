package com.hst.ii.company.proxy;

import com.hst.core.Page;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.meta.proxies.DefaultDataProxy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class CompanyApproveProxy extends DefaultDataProxy {

    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    public Page<?> list(Map<String, ?> query, int pageIdx) {
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.eq, "status", "1"));
        Page<?> p = this.dao.list(this.getEntityClass(), querys, pageIdx);
        p.setData2(this.convert2MetaData(p.getData()));
        return p;
    }

    public List<?> list(Map<String, ?> query) {
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.eq, "status", "1"));
        return this.convert2MetaData(this.dao.list(this.getEntityClass(), querys));
    }

}
