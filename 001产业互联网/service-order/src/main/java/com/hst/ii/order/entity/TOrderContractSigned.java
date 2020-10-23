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
 * 合同签章信息表
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="order_contract_signed")
@Comment("合同签章信息表")
@Data
@NoArgsConstructor
public class TOrderContractSigned extends BaseEntity implements Serializable {
    @Id
    @Column(length = 30)
    @Comment("主键")
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name = "id_order")
    @Comment("订单ID")
    private String orderId;




    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column
    @Comment("签章日期")
    private Date SignDate;



    @Column
    @Comment("签章参数")
    private String args;



}
