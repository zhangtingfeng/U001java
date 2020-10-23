package com.hst.ii.flow.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TOrgFlow
 * 机构的流程定义
 * @author WangYH
 * @date 2020/9/3
 */
@Entity
@Table(name="flow_org")
@Comment("机构的流程定义")
@Data
@NoArgsConstructor
public class TOrgFlow extends BaseEntity implements Serializable {
    @Id
    @Column(length = 30, name = "id_org")
    @Comment("机构ID")
    private String orgId;

    @Id
    @Column(length = 10, name = "type")
    @Comment("流程类型")
    private String flowType;

    @Column(length = 10, name="id_flow")
    @Comment("流程ID")
    private String flowId;
}
