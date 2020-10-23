/**
 *
 */
package com.hst.ii.org.ui;

import com.hst.core.annotation.Comment;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.org.proxy.OrgRoleInfoProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "org-role-info", title = "机构角色信息|org roles", proxy = OrgRoleInfoProxy.class)
@Data
@NoArgsConstructor
public class OrgRoleInfo extends MetaData {
    @EField(title = "机构id|orgid")
    private String orgid;

    @EField(title = "角色id|Roleid")
    private String roleid;

    @Comment("角色名称")
    @EField(title = "角色名称|Role Name")
    private String rolename;
}
