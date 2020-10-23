package com.hst.ii.basedata.proxy;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.proxies.DefaultDataProxy;
import com.hst.core.meta.proxies.OREntityUtils;
import com.hst.ii.basedata.entity.TBaseGoodsProp;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;


/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/9 10:08
 */
public class BaseGoodsPropProxy extends DefaultDataProxy {

    @Autowired
    OREntityUtils orUtil;

    public Object save(MetaData data) throws Exception {
        TBaseGoodsProp data1 = (TBaseGoodsProp) this.convert2Entity(data);
        // 默认值处理
        if (data1.getWidth() == null) {
            data1.setWidth("1");
        }
        if (data1.getForm() == null) {
            data1.setForm("1");
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
