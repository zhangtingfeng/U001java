package com.hst.ii.order.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * TPaymentRequest
 * 付款申请表
 * @author WangYH
 * @date 2020/9/6
 */
@Entity
@Table(name="order_evaluate")
@Comment("Order_Evaluate(订单评价表）")
@Data
@NoArgsConstructor
public class TOrder_evaluate extends BaseEntity implements Serializable {
    @Id
    @Column(length = 20)
    @Comment("系统ID")
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name="id_order", length = 10)
    @Comment("订单ID")
    private String orderId;

    @Column(name="evacontent", length = 500)
    @Comment("评价内容")
    private String evacontent;

    @Column(name="evacontentscore", length = 19)
    @Comment("评价分数")
    private Double evacontentscore;


}
