/**
 * 
 */
package com.hst.ii.sys.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.sys.entity.TRoleAction;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "role-action", title = "动作权限|Actions", entity = TRoleAction.class, fk = "roleId = id")
@Data
@NoArgsConstructor
public class RoleAction extends MetaData {
	
	@EField(title="ID")
	private String roleId;

	@EField(title="Action")
 	private String actionId;

}
