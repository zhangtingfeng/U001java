package com.hst.ii.entity;

import com.hst.core.annotation.Comment;

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
@Table(name="order_contract_signed")
@Comment("合同签章表")
public class TContractSigned  implements Serializable {
    @Id
    @Column(name = "id_order")
    @Comment("订单ID")
    private String orderId;


}
