/**
 * 
 */
package com.hst.ii.sys.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.sys.entity.TRoleMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "role-menu", title = "菜单权限|Menus", entity = TRoleMenu.class, fk = "roleId = id")
@Data
@NoArgsConstructor
public class RoleMenu extends MetaData {
	@EField(title="ID")
	private String roleId;

	@EField(title="Menu")
	private String menuId;

}
