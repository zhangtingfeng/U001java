package com.hst.ii.flow.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.flow.entity.TFlow;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Flow
 *
 * @author WangYH
 * @date 2020/9/3
 */
@EInfo(name = "flow", title = "流程配置|Flow", entity = TFlow.class)
@Data
@NoArgsConstructor
public class Flow extends MetaData {
    @EField(title = "ID", form = 2)
    private String id;

    @EField(title = "名称|Name", query = FieldQuery.LIKE, width=2)
    private String name;

    @EField(title = "版本|Version")
    private String ver;

    @EField(title = "流程类型|Flow catalog", form=0, viewer="dict.flowType", editor = "select.flowType", query = FieldQuery.EQ)
    private String type;
}
