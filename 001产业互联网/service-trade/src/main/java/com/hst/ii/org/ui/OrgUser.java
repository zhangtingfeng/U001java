package com.hst.ii.org.ui;

import com.hst.core.ServiceContext;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.sys.entity.TUser;
import com.hst.ii.org.proxy.OrgUserProxy;
import lombok.Data;

@Data
@EInfo(entity = TUser.class, name = "org-user", title = "机构用户信息|User info", proxy = OrgUserProxy.class, children = "org-user-role")
public class OrgUser extends MetaData {

    @EField(name = "id", title = "ID", list = false, form = 0)
    @ESort("asc")
    private String id;

    @EField(title = "登录账户|Userid")
    private String userid;

    @EField(title = "姓名|Name", query = FieldQuery.LIKE)
    private String name;

    @EField(title = "密码|Password", list = false, editor = "password")
    private String passwd;

    @EField(title = "Email", group = "联系信息|Link info", query = FieldQuery.LIKE)
    private String email;

    @EField(title = "手机|Mobile", query = FieldQuery.LIKE)
    private String mobile;

    @EField(title = "所属组织", form = 0, list = false)
    private String orgId;

    @EField(title = "状态", list = false, form = 0)
    private String status;

    @EField(title = "锁定状态", form = 0, viewer = "dict.locked", editor = "select.locked")
    private String locked;

    @EField(title = "openid", form = 0, list = false)
    private String openid;

    public OrgUser() {
        String orgId = ServiceContext.getInstance().getUser().getOrgcode();
        this.orgId = orgId;
        this.locked = "0";

    }

}
