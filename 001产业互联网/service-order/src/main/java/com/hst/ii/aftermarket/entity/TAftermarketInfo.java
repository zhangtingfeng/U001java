package com.hst.ii.aftermarket.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aftermarket_info")
@HSID(length = 10, prefix = "S")
@Comment("售后服务信息")
@Data
@NoArgsConstructor
public class TAftermarketInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 10)
    @Comment("售后编号")
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name="id_order", length = 10)
    @Comment("订单ID")
    private String orderId;

    @Column(name = "id_org", length = 10)
    @Comment("机构ID")
    private String orgId;

    @Column(name = "id_buyer", length = 10)
    @Comment("买家（机构）)")
    private String buyerId;

    @Column(name = "id_seller", length = 10)
    @Comment("卖家（机构）")
    private String sellerId;

    @Column(length = 2)
    @Comment("售后服务状态. 1: 售后服务生成, 2: 售后服务交易处理中, 0: 售后服务取消, 9:售后服务完成")
    private String status;

    @Column(length = 10)
    @Comment("售后生成流程id")
    private String process1;

    @Column(length = 10)
    @Comment("售后交易流程id")
    private String process2;

    @Column(length = 10)
    @Comment("买家状态")
    private String state1;

    @Column(length = 10)
    @Comment("卖家状态")
    private String state2;


    @Column(length = 200)
    @Comment("售后服务备注")
    private String remark;

}
