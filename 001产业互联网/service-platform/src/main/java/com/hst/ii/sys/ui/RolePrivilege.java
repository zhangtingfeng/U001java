/**
 * 
 */
package com.hst.ii.sys.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.sys.entity.TRolePrivilege;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "role-privilege", title = "系统权限|Privileges", entity = TRolePrivilege.class, fk = "roleId = id")
@Data
@NoArgsConstructor
public class RolePrivilege extends MetaData {
	@EField(title="ID")
	private String roleId;

	@EField(title="Privilege")
	private String privilegeId;
}
