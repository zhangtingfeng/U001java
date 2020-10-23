package com.hst.ii.user.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.user.entity.TUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@EInfo(entity = TUser.class, name = "user", title = "用户信息|User info",children = "user-role")
@Data
@NoArgsConstructor
public class User extends MetaData{
	
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
	
	@EField(title = "所属组织",form=0,list=false)
	private String orgId;
	
	@EField(title = "状态",form=0,list=false)
	private String status;
	
	@EField(title = "openid",form=0,list=false)
	private String openid;

	@EField(title = "锁定", editor = "select.locked", viewer = "dict.locked")
	private String locked;

}
