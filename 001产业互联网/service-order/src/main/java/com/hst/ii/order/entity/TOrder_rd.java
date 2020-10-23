package com.hst.ii.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TPaymentRequest
 * 付款申请表
 * @author WangYH
 * @date 2020/9/6
 */
@Entity
@Table(name="order_rd")
@Comment("Order_RD(收发货表）")
@Data
@NoArgsConstructor
public class TOrder_rd extends BaseEntity  implements Serializable {
    @Id
    @Column(length = 20)
    @Comment("系统ID")
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name="id_order", length = 10)
    @Comment("订单ID")
    private String orderId;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="delivery_dt")
    @Comment("发货时间")
    private Date delivery_dt;

    @Column(name="delivery_no", length = 50)
    @Comment("送货单号")
    private String delivery_no;

    @Column(name="delivery_type", length = 50)
    @Comment("发运方式 1 自提 2 物流")
    private String delivery_type;

    @Column(name="id_carrier")
    @Comment("承运商")
    private String id_carrier;

    @Column(length = 50)
    @Comment("发货仓库")
    private String id_warehouse;

    @Column(name="info")
    @Comment("上传路径")
    private String info;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="receive_dt")
    @Comment("收货时间")
    private Date receive_dt;

    @Column(name="receiver")
    @Comment("收货人")
    private String receiver;

    @Column(name="status")
    @Comment("状态 1 发货 2 收货")
    private String status;

    @Column(name="trans_no")
    @Comment("运单号")
    private String trans_no;





}
