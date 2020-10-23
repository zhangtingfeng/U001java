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
@Table(name="flow_node_prop_config")
@Data
@Comment("流程类型节点属性表")
public class TFlowNodePropConfig implements Serializable {
    @Id
    @Column(name="flow_type", length = 10)
    @Comment("流程类型")
    private String flowType;

    @Id
    @Column(length = 30)
    @Comment("属性ID")
    private String Id;

    @Column(length = 50)
    @Comment("流程属性名称")
    private String name;

    @Column(nullable = false)
    @Comment("排序")
    private int seq;

    @Column(length = 30)
    @Comment("属性编辑器")
    private String editor;

    @Column(length = 1)
    @Comment("是否有额外参数.1: 有")
    private String extArgs;
}
