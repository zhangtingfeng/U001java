/**
 * 
 */
package com.hst.ii.org.ui;

import com.hst.core.annotation.Comment;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.org.entity.TOrgRole;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "org-role", title = "机构角色|org roles", entity = TOrgRole.class, fk = "orgid=id")
@Data
@NoArgsConstructor
public class OrgRole extends MetaData {
	@EField(title = "机构id|orgid")
	private String orgid;

	@Comment("角色id")
	@EField(title = "角色id|Roleid")
	private String roleid;

}
