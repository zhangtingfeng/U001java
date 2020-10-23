package com.hst.ii.flow.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TFlow
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="flow_link")
@Data
@Comment("流程节点关系表")
public class TFlowLink implements Serializable {
    @Id
    @Column(length = 10)
    private String node1;

    @Id
    @Column(length = 10)
    private String node2;

    @Id
    @Column(name="id_flow", length = 30)
    @Comment("流程ID")
    private String flowId;

    @Column(length = 50)
    @Comment("操作名称")
    private String name;

    @Column(name="exec_condition", length = 200)
    @Comment("执行条件")
    private String condition;

    @Column(length = 1)
    @Comment("流转类型. 0: 手工流转, 1: 自动流转")
    private String type;
}
