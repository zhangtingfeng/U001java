package com.hst.ii.cart.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_cart")
@Comment("购物车")
@HSID(length = 10, prefix = "C")
@Data
@NoArgsConstructor
public class TUserCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Comment("商品id")
    @Column(name="id_goods",length = 10)
    private String goodsId;

    @Comment("买家id")
    @Column(name="id_buyer",length = 10)
    private String buyerId;

    @Comment("卖家id")
    @Column(name="id_seller",length = 10)
    private String sellerId;

    @Comment("数量")
    private Integer num;

    @Column(name = "create_time")
    @Comment("创建时间")
    private Date createTime;

    @Column(name = "update_time")
    @Comment("修改时间")
    private Date updateTime;

}
