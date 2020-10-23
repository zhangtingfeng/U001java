/**
 *
 */
package com.hst.ii.sys.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.sys.entity.TMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name = "menu", title = "系统菜单|System menus", entity = TMenu.class)
@Data
@NoArgsConstructor
public class Menu extends MetaData {
    @EField(title = "ID", form = 2, query = FieldQuery.LIKE)
    private String id;

    @EField(title = "上级ID|Parent ID")
    private String pid;

    @EField(title = "名称|Name", query = FieldQuery.LIKE)
    private String name;

    @EField(title = "图标|Icon")
    private String icon;

    @EField(title = "参数|Arguments")
    private String args;
}
