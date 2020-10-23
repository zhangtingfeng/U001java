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
@Table(name = "process_def")
@HSID(length = 10, prefix = "P", resetByDay = true)
@Comment("流程定义")
@Data
@NoArgsConstructor
public class TProcessDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 30)
    @Comment("ID")
    private String id;

    @Column(length = 30)
    @Comment("名称")
    private String name;

    @Column(length = 30)
    @Comment("类型")
    private String type;

}
