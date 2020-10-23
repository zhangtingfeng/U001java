package com.hst.ii.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 合同属性信息
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="order_contract")
@Comment("合同信息表")
@Data
@NoArgsConstructor
public class TOrderContract  extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id_order")
    @Comment("订单ID")
    private String orderId;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name = "pay_day")
    @Comment("付款日期")
    private Date payDay;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column
    @Comment("交付时间开始")
    private Date delivery_Start_dt;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column
    @Comment("交付时间结束")
    private Date delivery_End_dt;


    @Column
    @Comment("是否需要加工包装服务")
    private String needpack;


    @Column
    @Comment("是否提供包装服务")
    private String pack;

    @Column
    @Comment("包装描述")
    private String pack_remark;

    @Column
    @Comment("物流公司")
    private String delivery_vendor;

    @Column
    @Comment("收货地址")
    private String delivery_address;

    @Column
    @Comment("支付方式")
    private String pay_type;

    @Column
    @Comment("第一次支付金额")
    private String pay1Amount;

    @Column
    @Comment("第一次支付期限")
    private String pay1Days;

    @Column
    @Comment("第二次支付金额")
    private String pay2Amount;

    @Column
    @Comment("第二次支付期限")
    private String pay2Days;

    @Column
    @Comment("商业机密保密义务的有效期限(年)")
    private String validityperiod;

    @Column
    @Comment("未按照合同付款的违约滞纳金（%/日）")
    private String overduefine;


    @Column
    @Comment("任何一方的地址或联系人发生变更,需提前多少日通知对方(日)")
    private String changesneedadvance;

    @Column
    @Comment("产品名称、产地合同需要的其他")
    private String productnameproductionarea;


}
