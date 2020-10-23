package com.hst.ii.company.proxy;

import com.hst.core.Page;
import com.hst.core.ServiceContext;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.meta.proxies.DefaultDataProxy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class CompanyProxy extends DefaultDataProxy {

    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    @Override
    public Page<?> list(Map<String, ?> query, int pageIdx) {
        String orgid = ServiceContext.getInstance().getUser().getOrgcode();
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.eq, "orgId", orgid));
        Page<?> p = this.dao.list(this.getEntityClass(), querys, pageIdx);
        p.setData2(this.convert2MetaData(p.getData()));
        return p;
    }

    @Override
    public List<?> list(Map<String, ?> query) {
        String orgid = ServiceContext.getInstance().getUser().getOrgcode();
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.eq, "orgId", orgid));
        return this.convert2MetaData(this.dao.list(this.getEntityClass(), querys));
    }

}
