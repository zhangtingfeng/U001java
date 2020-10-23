package com.hst.ii.company.proxy;

import com.hst.core.Page;
import com.hst.core.ServiceContext;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.proxies.DefaultDataProxy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class CompanyLogProxy extends DefaultDataProxy {

    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    @Override
    public Page<?> list(Map<String, ?> query, int pageIdx) {
        String userid = ServiceContext.getInstance().getUser().getUid();
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.eq, "applyUserId", userid));
        Page<?> p = this.dao.list(this.getEntityClass(), querys, pageIdx);
        p.setData2(this.convert2MetaData(p.getData()));
        return p;
    }

    @Override
    public List<?> list(Map<String, ?> query) {
        String userid = ServiceContext.getInstance().getUser().getUid();
        List<ORQuery> querys = this.parseQuery(query);
        querys.add(new ORQuery(ORQuery.Op.eq, "applyUserId", userid));
        return this.convert2MetaData(this.dao.list(this.getEntityClass(), querys));
    }


}
