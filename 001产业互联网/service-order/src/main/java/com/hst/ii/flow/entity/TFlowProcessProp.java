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
 * 流程实列属性表
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="flow_process_prop")
@Data
@Comment("流程实列属性表")
public class TFlowProcessProp  implements Serializable {
    @Id
    @Column(length = 10)
    @Comment("流程实例ID")
    private String id;

    @Id
    @Column(length = 30)
    @Comment("属性名称")
    private String name;

    @Column(length = 100)
    @Comment("流程节点属性值")
    private String val;
}
