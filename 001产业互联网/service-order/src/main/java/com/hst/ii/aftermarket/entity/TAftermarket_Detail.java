package com.hst.ii.aftermarket.entity;

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
 * 售后服务详情表
 * @author WangYH
 * @date 2020/9/6
 */
@Entity
@Table(name="aftermarket_detail")
@Comment("Aftermarket_Detail(售后服务详情表）")
@Data
@NoArgsConstructor
public class TAftermarket_Detail extends BaseEntity implements Serializable {
    @Id
    @Column(length = 10)
    @Comment("系统ID")
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name="id_process", length = 10)
    @Comment("订单processID")
    private String processid;

    @Column(name="id_order", length = 10)
    @Comment("订单ID")
    private String orderId;

    @Column(name = "id_buyer", length = 10)
    @Comment("买家（机构）)")
    private String buyerId;

    @Column(name = "id_seller", length = 10)
    @Comment("卖家（机构）")
    private String sellerId;


    @Column(length = 10)
    @Comment("买家状态")
    private String state1;

    @Column(length = 10)
    @Comment("卖家状态")
    private String state2;

    @Column(name = "id_org", length = 10)
    @Comment("机构ID")
    private String orgId;

    @Column(name="buyeraftermarkettype", length = 20)
    @Comment("buyer售后类型")
    private String buyeraftermarkettype;

    @Column(name = "buyeraftermarketdesc", length = 400)
    @Comment("buyer描述")
    private String buyeraftermarketdesc;


    @Column(name="selleraftermarkettype", length = 20)
    @Comment("seller解决类型")
    private String selleraftermarkettype;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name = "sellerservice_day")
    @Comment("seller解决日期")
    private Date sellerserviceDay;

    @Column(name = "selleraftermarketdesc", length = 400)
    @Comment("seller描述")
    private String selleraftermarketdesc;


}
