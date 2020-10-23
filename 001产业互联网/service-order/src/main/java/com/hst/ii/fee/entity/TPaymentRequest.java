package com.hst.ii.fee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * TPaymentRequest
 * 付款申请表
 * @author WangYH
 * @date 2020/9/6
 */
@Entity
@Table(name="payment_request")
@Comment("付款申请表")
@Data
@NoArgsConstructor
public class TPaymentRequest extends BaseEntity {
    @Id
    @Column(length = 20)
    @Comment("申请单号")
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name="id_order", length = 10)
    @Comment("订单ID")
    private String orderId;

    @Column(name="pay_phase", length = 10)
    @Comment("付款阶段")
    private String payPhase;

    @Column(name="id_process", length = 10)
    @Comment("流程ID")
    private String processId;

    @Column(name="title", length = 50)
    @Comment("申请单标题")
    private String title;

    @Column(name="payment_org")
    @Comment("付款机构")
    private String paymentOrg;

    @Column(length = 10)
    @Comment("付款方式。1 线上 2线下")
    private String type;

    @Column()
    @Comment("付款金额")
    private Double amount;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="payment_dt")
    @Comment("计划付款时间")
    private Date paymentDt;

    @Column(name="payment_no")
    @Comment("付款单号")
    private String paymentNo;

    @Column(name="receipt_org")
    @Comment("收款机构")
    private String receiptOrg;

    @Column(name="receipt_account")
    @Comment("收款账户")
    private String receiptAccount;

    @Column(name="receipt_bank")
    @Comment("收款银行")
    private String receiptBank;

    @Column(name="status")
    @Comment("申请单状态:0保存状态 1 申请 2 已审核 3 已支付")
    private String status;
}
