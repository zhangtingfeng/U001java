/**
 * 
 */
package com.hst.ii.user.ui;

import com.hst.core.annotation.Comment;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.user.entity.TUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "user-role", title = "用户角色|User roles", entity = TUserRole.class, fk = "userid=id")
@Data
@NoArgsConstructor
public class UserRole extends MetaData {
	@EField(title = "用户id|Userid")
	private String userid;

	@Comment("角色id")
	@EField(title = "角色id|Roleid")
	private String roleid;

}
