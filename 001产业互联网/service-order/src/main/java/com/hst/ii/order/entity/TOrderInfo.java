package com.hst.ii.order.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_info")
@HSID(length = 10, prefix = "S")
@Comment("订单信息")
@Data
@NoArgsConstructor
public class TOrderInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 10)
    @Comment("订单号")
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name = "id_buyer", length = 10)
    @Comment("买家（机构）)")
    private String buyerId;

    @Column(name = "id_seller", length = 10)
    @Comment("卖家（机构）")
    private String sellerId;

    @Column(length = 2)
    @Comment("订单状态. 1: 订单生成, 2: 订单交易, 0: 订单取消, 9:订单完成")
    private String status;

    @Column(length = 10)
    @Comment("订单生成流程id")
    private String process1;

    @Column(length = 10)
    @Comment("订单交易流程id")
    private String process2;

    @Column(length = 10)
    @Comment("订单售后服务的流程id")
    private String process3;


    @Column(length = 10)
    @Comment("买家状态")
    private String state1;

    @Column(length = 10)
    @Comment("卖家状态")
    private String state2;

    @Column(name = "id_addr", length = 10)
    @Comment("地址ID")
    private String addrId;

    @Column()
    @Comment("订单总价")
    private Double amount;

    @Column(length = 200)
    @Comment("订单备注")
    private String remark;

}
