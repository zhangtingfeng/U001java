package com.hst.ii.process.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/21 17:44
 */
@Entity
@Table(name = "node_def")
@HSID(length = 10, prefix = "N", resetByDay = true)
@Comment("节点定义")
@Data
@NoArgsConstructor
public class TNodeDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 30)
    @Comment("ID")
    private String id;

    @Id
    @Column(name="id_process",length = 30)
    @Comment("流程ID")
    private String processId;

    @Column(length = 30)
    @Comment("名称")
    private String name;

    @Comment("序列")
    private Integer idx;
}
