package com.hst.ii.process.entity;

import com.hst.core.annotation.Comment;
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
 * @time: 2020/8/22 21:54
 */
@Entity
@Table(name = "node_user")
@Comment("节点人员表")
@Data
@NoArgsConstructor
public class TNodeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id_node",length = 30)
    @Comment("节点ID")
    private String nodeId;

    @Id
    @Column(name="id_user",length = 30)
    @Comment("人员ID")
    private String userId;
}
