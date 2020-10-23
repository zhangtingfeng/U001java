package com.hst.ii.user.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.user.entity.TUser;
import com.hst.ii.user.proxy.UserRegisteredProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

@EInfo(entity = TUser.class, name = "userRegistered", title = "注册用户管理|User Registered", proxy = UserRegisteredProxy.class)
@Data
@NoArgsConstructor
public class UserRegistered extends MetaData {

    @EField(name = "id", title = "ID", list = false, form = 0)
    @ESort("asc")
    private String id;

    @EField(title = "登录账户|Userid", query = FieldQuery.LIKE)
    @ESort("asc")
    private String userid;

    @EField(title = "姓名|Name", query = FieldQuery.LIKE)
    private String name;

    @EField(title = "密码|Password", list = false, editor = "password")
    private String passwd;

    @EField(title = "联系邮箱|Email")
    private String email;

    @EField(title = "手机|mobile")
    private String mobile;

    @EField(title = "openid", list = false, form = 0)
    private String openid;

    @EField(title = "状态", list = false, form = 0)
    private String status;

    @EField(title = "是否锁定", editor = "select.locked", viewer = "dict.locked")
    private String locked;

}
