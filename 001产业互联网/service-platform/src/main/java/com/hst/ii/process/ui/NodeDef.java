package com.hst.ii.process.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.process.entity.TNodeDef;
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
@EInfo(name="node-def",title="节点定义",entity = TNodeDef.class,fk="processId = id")
public class NodeDef extends MetaData {

    @EField(title = "节点ID")
    private String id;

    @EField(title = "流程ID",list = false,form = 0)
    private String processId;

    @EField(title = "名称")
    private String name;

    @EField(title = "序列")
    private Integer idx;


}
