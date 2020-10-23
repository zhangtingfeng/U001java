package com.hst.ii.process.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.process.entity.TProcessDef;
import com.hst.ii.process.entity.TProcessOrg;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/13 14:51
 */
@Data
@NoArgsConstructor
@EInfo(name="process-org",title="流程机构",entity = TProcessOrg.class)
public class ProcessOrg extends MetaData {

    @EField(title = "流程",viewer="dict.dict-process",editor = "select.dict-process",query= FieldQuery.EQ)
    private String processId;

    @EField(title = "机构",viewer="dict.dict-orgs",editor = "select.dict-orgs",query= FieldQuery.EQ)
    private String orgId;

}
