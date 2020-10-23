package com.hst.ii.fee.service;

import com.hst.core.Page;
import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.dao.ISQLDao;
import com.hst.core.meta.DataProxy;
import com.hst.core.meta.MetaData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * FeeProcessProxy
 *
 * @author WangYH
 * @date 2020/9/5
 */
public class FeeProcessProxy extends DataProxy {
    @Autowired
    ISQLDao sql;

    @Override
    public Page<?> list(Map<String, ?> query, int pageIdx) {
        ((Map<String, Object>) query).put("uid", ServiceContext.getInstance().getUid());
        ((Map<String, Object>) query).put("org", ServiceContext.getInstance().getOrg());

        Page<?> list = sql.list("fee.list-processes", query, pageIdx, Page.getPageSize());
        return list;
    }

    @Override
    public List<?> list(Map<String, ?> query) {
        ((Map<String, Object>) query).put("uid", ServiceContext.getInstance().getUid());
        ((Map<String, Object>) query).put("org", ServiceContext.getInstance().getOrg());
        return sql.query("fee.list-processes", query);
    }

    @Override
    public Object save(MetaData data) throws Exception {
        throw new ServiceException(ServiceException.INTERNAL);
    }

    @Override
    public Object del(MetaData data) throws Exception {
        throw new ServiceException(ServiceException.INTERNAL);
    }
}
