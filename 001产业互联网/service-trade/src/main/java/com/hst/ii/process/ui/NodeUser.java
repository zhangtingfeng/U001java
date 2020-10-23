package com.hst.ii.process.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.process.entity.TNodeUser;
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
@EInfo(name="node-user",title="节点用户",entity = TNodeUser.class)
public class NodeUser extends MetaData {

    @EField(title = "节点",viewer="dict.dict-nodes",editor = "select.dict-nodes",query= FieldQuery.EQ)
    private String nodeId;

    @EField(title = "用户",viewer="dict.dict-users",editor = "select.dict-users",query= FieldQuery.EQ)
    private String userId;

}
