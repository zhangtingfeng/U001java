package com.hst.ii.order.entity;

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
 * TOrderContract
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="order_dealer")
@Comment("订单交易商信息表")
@Data
@NoArgsConstructor
public class TOrderDealer extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id_order")
    @Comment("订单ID")
    private String orderId;

    @Id
    @Column
    @Comment("类型，1 买方/ 2 卖方")
    private String type;

    @Column(name = "id_org")
    @Comment("买家/卖家对应的机构ID")
    private String orgId;

    @Column
    @Comment("企业名称")
    private String title;

    @Column
    @Comment("企业地址")
    private String address;

    @Column
    @Comment("企业电话")
    private String phone;

    @Column
    @Comment("企业签字人")
    private String signer;

    @Column
    @Comment("企业开户行")
    private String bank;

    @Column(name="bank_title")
    @Comment("银行账户名称")
    private String bankTitle;

    @Column(name="bank_account")
    @Comment("银行账号")
    private String bankAccount;

    @Column
    @Comment("联系人")
    private String contactor;

    @Column(name="contact_phone")
    @Comment("联系电话")
    private String contactPhone;

    @Column(name="contact_address")
    @Comment("联系地址")
    private String contactAddress;

}
