package com.hst.ii.sales.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 挂牌销售审核流程表
 * @author: ZhaJun
 * @time: 2020/7/10 13:41
 */
@Entity
@Table(name = "sale_approval")
@Comment("挂牌销售审核流程表")
@HSID(length = 10, prefix = "A")
@Data
@NoArgsConstructor
public class TSaleApproval implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_sales", length = 10)
    @Comment("挂牌销售ID")
    private String salesId;

    @Column(length = 10)
    @Comment("审批时间")
    private Date time;

    @Column(length = 10)
    @Comment("审批人员")
    private String operator;

    @Column(length = 1)
    @Comment("审批结果")
    private String result;

    @Column(length = 100)
    @Comment("审批意见")
    private String opinions;


}
