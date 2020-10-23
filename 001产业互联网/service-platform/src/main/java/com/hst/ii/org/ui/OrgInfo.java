package com.hst.ii.org.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.org.entity.TOrgInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@EInfo(entity = TOrgInfo.class, name = "org-info", title = "交易商管理", children = "org-role")
@Data
@NoArgsConstructor
public class OrgInfo extends MetaData {

    @EField(title = "交易商代码", form = 3)
    @ESort("asc")
    private String id;

    @EField(title = "交易商编码", list = false, form = 0)
    private String code;

    @EField(title = "交易商名称", query = FieldQuery.LIKE)
    private String name;

    @EField(title = "父级机构", list = false, form = 0)
    private String pid;

    @EField(title = "冻结状态", viewer = "dict.freeze", editor = "select.freeze", form = 3)
    private String freeze;

}
