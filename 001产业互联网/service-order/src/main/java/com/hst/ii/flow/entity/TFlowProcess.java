package com.hst.ii.flow.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TFlowProcess 流程实例表
 * 
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name = "flow_process")
@Data
@Comment("流程实例表")
public class TFlowProcess extends BaseEntity implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("流程实例ID")
    private String id;

    @Column(name = "id_flow", length = 10)
    @Comment("流程")
    private String flowId;

    @Column(name = "id_service", length = 30)
    @Comment("业务表单ID")
    private String serviceId;

    @Column(name = "id_org", length = 30)
    @Comment("机构ID")
    private String orgId;

    @Column(name = "id_hist", length = 10)
    @Comment("历史表ID")
    private String histId;

    @Column(name = "prev_node", length = 10)
    @Comment("上一个节点")
    private String prevNodeId;

    @Column(name = "prev_userid", length = 30)
    @Comment("上一个节点处理人")
    private String prevNodeUserid;

    @Column(name = "prev_dt")
    @Comment("上一个节点提交时间")
    private Date prevNodeDt;

    @Column(name = "prev_remark", length = 200)
    @Comment("上一个节点提交说明")
    private String prevNodeRemark;

    @Column(name = "node", length = 10)
    @Comment("当前节点")
    private String nodeId;

    @Column(name = "userid", length = 30)
    @Comment("当前节点处理人")
    private String nodeUserid;
}
