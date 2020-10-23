package com.hst.ii.flow.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * TFlow
 * 流程表
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="flow_info")
@Data
@Comment("流程定义表")
public class TFlow  implements Serializable {
    @Id
    @Column(length = 30)
//    @GeneratedValue(generator = "hsid")
//    @GenericGenerator(name="hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("流程ID")
    private String id;

    @Column(length = 50)
    @Comment("流程名称")
    private String name;

    @Column(length = 10)
    @Comment("流程版本")
    private String ver;

    @Column(length = 10)
    @Comment("流程类型")
    private String type;
}
