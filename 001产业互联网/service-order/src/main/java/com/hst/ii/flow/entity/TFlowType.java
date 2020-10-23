package com.hst.ii.flow.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hst.core.annotation.Comment;
import com.hst.core.meta.annotation.ESort;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TFlowType
 * 流程类型表
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name = "flow_type")
@Data
@Comment("流程类型表")
public class TFlowType implements Serializable {
    @Id
    @Column(name = "id", length = 10)
    @Comment("id")
    private String id;

    @Column(length = 50)
    @Comment("流程类型")
    @JsonProperty("text")
    private String name;

    @Column(length = 30)
    @Comment("link类型对应的dict名称")
    private String linkType;

    @ESort("asc")
    @Column
    @Comment("排序")
    private int seq;
}
