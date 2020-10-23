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
 * 合同属性信息
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Entity
@Table(name="Order_Specifications")
@Comment("规格、质量")
@Data
@NoArgsConstructor
public class TOrderSpecifications extends BaseEntity implements Serializable {

    @Column
    @Comment("订单ID")
    private String id_order;

    @Column
    @Comment("类型")
    private String CheckType;

    @Column(length = 10)
    @Comment("是否选中")
    private boolean ifcheck;


    @Id
    @Column(length = 10)
    @Comment("自增长ID 用于页面排序")
    private Long orderbyid;


}
