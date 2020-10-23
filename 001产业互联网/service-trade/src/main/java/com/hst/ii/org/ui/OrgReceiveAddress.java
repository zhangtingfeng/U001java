package com.hst.ii.org.ui;

import com.hst.core.ServiceContext;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.org.entity.TOrgReceiveAddress;
import com.hst.ii.org.entity.TOrgWarehouse;
import com.hst.ii.org.proxy.OrgReceiveAddrProxy;
import com.hst.ii.org.proxy.OrgWarehouseProxy;
import lombok.Data;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/8 11:24
 */
@Data
@EInfo(entity = TOrgReceiveAddress.class, name = "org-rcv-addr", title = "机构收货地址信息|Receive info", proxy = OrgReceiveAddrProxy.class)
public class OrgReceiveAddress extends MetaData {

    @EField(name = "id", title = "ID", list = false, form = 0)
    @ESort("asc")
    private String id;

    @EField(title = "所属组织", form = 0, list = false)
    private String orgId;

    @EField(title = "收件人|Name", validate = "required", query = FieldQuery.LIKE)
    private String name;

    @EField(title = "联系方式|Tel", validate = "required", query = FieldQuery.LIKE)
    private String tel;

    @EField(title = "类型", viewer = "dict.default", editor = "select.default")
    private String type;

    @EField(title = "收货地区域", width = 1.5f, viewer = "district", editor = "district")
    private String area;

    @EField(title = "收货地详细地址", width = 2)
    private String address;


    public OrgReceiveAddress() {
        String orgId = ServiceContext.getInstance().getUser().getOrgcode();
        this.orgId = orgId;
    }
}
