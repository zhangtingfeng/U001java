package com.hst.ii.org.ui;

import com.hst.core.ServiceContext;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.org.entity.TOrgWarehouse;
import com.hst.ii.org.proxy.OrgWarehouseProxy;
import lombok.Data;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/8 11:24
 */
@Data
@EInfo(entity = TOrgWarehouse.class, name = "org-wh", title = "机构仓库信息|Warehouse info", proxy = OrgWarehouseProxy.class)
public class OrgWarehouse extends MetaData {

    @EField(name = "id", title = "ID", list = false, form = 0)
    @ESort("asc")
    private String id;

    @EField(title = "所属组织", form = 0, list = false)
    private String orgId;

    @EField(title = "仓库编码|Code", validate = "required", query = FieldQuery.LIKE)
    private String code;

    @EField(title = "仓库名称|Name", validate = "required", query = FieldQuery.LIKE)
    private String name;

    @EField(title = "发货地区域", width = 1.5f, viewer = "district", editor = "district")
    private String area;

    @EField(title = "发货地详细地址", width = 2)
    private String address;

    public OrgWarehouse() {
        String orgId = ServiceContext.getInstance().getUser().getOrgcode();
        this.orgId = orgId;
    }
}
