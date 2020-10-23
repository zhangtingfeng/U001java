/**
 *
 */
package com.hst.ii.org.ui;

import com.hst.core.annotation.Comment;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.sys.entity.TUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@Data
@NoArgsConstructor
@EInfo(name = "org-user-role", title = "机构用户-角色|User roles", entity = TUserRole.class, fk = "userid=id")
public class OrgUserRole extends MetaData {
    @EField(title = "用户id|Userid")
    private String userid;

    @Comment("角色id")
    @EField(title = "角色id|Roleid")
    private String roleid;

}
