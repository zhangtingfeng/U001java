package com.hst.ii.org.proxy;

import com.hst.core.MapBean;
import com.hst.core.Page;
import com.hst.core.ServiceContext;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.proxies.DefaultDataProxy;
import com.hst.core.meta.proxies.OREntityUtils;
import com.hst.ii.org.entity.TOrgReceiveAddress;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OrgReceiveAddrProxy extends DefaultDataProxy {

    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    @Autowired
    OREntityUtils orUtil;

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

    public Object save(MetaData data) throws Exception {
        TOrgReceiveAddress data1 = (TOrgReceiveAddress) this.convert2Entity(data);
        if (data1.getType() == null) {
            data1.setType("2");
        }
        if ("1".equals(data1.getType())) {
            MapBean<String, Object> params = new MapBean<>();
            params.put("orgId", data1.getOrgId());
            sql.update("orgs.updateDefaultAddr", params);
        }
        if (1 == data.getOp()) {
            this.dao.save(data1);
        } else {
            Object data2 = null;
            Serializable pk = (Serializable) this.orUtil.getEntityPk(data1);
            if (null != pk) {
                data2 = this.dao.get(this.getEntityClass(), pk);
            }

            if (null == data2) {
                this.dao.save(data1);
            } else {
                BeanUtils.copyProperties(data2, data1);
                this.dao.update(data2);
            }
        }

        return this.convert2MetaData(data1);
    }


}
