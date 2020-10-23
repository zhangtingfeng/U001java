package com.hst.ii.flow.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * TFlowNode
 * 流程节点表
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name = "flow_node")
@Data
@Comment("流程节点表")
public class TFlowNode  implements Serializable {
    public static final  String TYPE_START = "0";
    public static final  String TYPE_END = "1";
    public static final  String TYPE_ACTIVITY = "2";
    @Id
    @Column(length = 30)
//    @GeneratedValue(generator = "hsid")
//    @GenericGenerator(name="hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("节点I,(id_flow_id_node)")
    private String id;

    @Id
    @Column(name="id_flow", length = 30)
    @Comment("流程ID")
    private String flowId;

    @Column(length = 50)
    @Comment("节点名称")
    private String name;

    @Column(length = 1)
    @Comment("节点类型.0:开始节点, 1: 结束节点, 2: 处理节点")
    private String type;

    @Column
    @Comment("X")
    private Integer x;

    @Column
    @Comment("Y")
    private Integer y;
}
