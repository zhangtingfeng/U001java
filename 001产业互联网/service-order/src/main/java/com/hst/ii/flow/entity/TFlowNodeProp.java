package com.hst.ii.flow.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TFlowNodeProp
 * 流程节点属性表
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="flow_node_prop")
@Data
@Comment("流程节点属性表")
public class TFlowNodeProp  implements Serializable {
    @Id
    @Column(name="id_flow", length = 30)
    @Comment("流程ID")
    private String flowId;

    @Id
    @Column(length = 10)
    @Comment("流程节点ID")
    private String nodeId;

    @Id
    @Column(length = 30)
    @Comment("流程节点属性名称")
    private String name;

    @Column(length = 100)
    @Comment("流程节点属性值")
    private String val;
}
