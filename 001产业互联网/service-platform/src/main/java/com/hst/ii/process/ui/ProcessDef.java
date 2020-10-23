package com.hst.ii.process.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.process.entity.TNodeDef;
import com.hst.ii.process.entity.TProcessDef;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/13 14:51
 */
@Data
@NoArgsConstructor
@EInfo(name="process-def",title="流程定义",entity = TProcessDef.class,children = "node-def")
public class ProcessDef extends MetaData {

    @EField(title = "流程ID",query= FieldQuery.LIKE)
    private String id;

    @EField(title = "名称",query= FieldQuery.LIKE)
    private String name;

    @EField(title = "类型",viewer="dict.processType",editor = "select.processType",query= FieldQuery.EQ)
    private String type;

}
