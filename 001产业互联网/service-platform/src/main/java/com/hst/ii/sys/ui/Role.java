/**
 * 
 */
package com.hst.ii.sys.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.sys.entity.TRole;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "role", entity = TRole.class, title = "角色管理|Roles", children = {"role-menu","role-privilege" ,"role-action"})
@Data
@NoArgsConstructor
public class Role extends MetaData {
	
	@EField(title="ID",form = 2, validate = "required", query = FieldQuery.LIKE)
	private String id;

	@EField(title = "名称|Name", validate = "required", query = FieldQuery.LIKE)
	private String name;

}
