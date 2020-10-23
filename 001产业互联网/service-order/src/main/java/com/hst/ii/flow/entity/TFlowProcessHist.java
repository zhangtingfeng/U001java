package com.hst.ii.flow.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TFlowProcess 流程实例处理纪录表
 * 
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name = "flow_process_hist")
@Data
@Comment("流程流转历史表")
public class TFlowProcessHist extends BaseEntity implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_process", length = 10)
    @Comment("流程实例ID")
    private String processId;

    @Column(name = "prev_node", length = 10)
    @Comment("上一个节点")
    private String prevNodeId;

    @Column(name = "prev_userid", length = 30)
    @Comment("上一个节点处理人")
    private String prevNodeUserid;

    @Column(name = "prev_submit_dt")
    @Comment("上一个节点提交时间")
    private Date prevNodeDt;

    @Column(name = "node", length = 10)
    @Comment("当前节点")
    private String nodeId;

    @Column(name = "userid", length = 30)
    @Comment("当前节点处理人")
    private String nodeUserid;

    @Column(name = "dt")
    @Comment("当前节点处理时间")
    private Date nodeDt;

    @Column(name = "remark", length = 200)
    @Comment("当前节点处理说明")
    private String remark;

    @Column(name = "next_node", length = 10)
    @Comment("下一个节点")
    private String nextNode;

    @Column(name = "next_userid", length = 30)
    @Comment("下一个节点用户")
    private String nextNodeUserid;
}
