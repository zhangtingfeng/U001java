package com.hst.ii.order.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "order_goods")
@Comment("订单商品信息")
@HSID(length = 10, prefix = "G")
@Data
@NoArgsConstructor
public class TOrderGoods extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 30)
	@Comment("主键")
	@GeneratedValue(generator = "hsid")
	@GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
	private String id;

	@Column(name="id_goods",length = 10)
	@Comment("商品id")
	private String goodsId;

	@Column(name="id_order",length = 10)
	@Comment("订单id")
	private String orderId;

	@Column(length = 10)
	@Comment("商品数量")
	private Integer num;

	@Column(name="goods_price")
	@Comment("商品成交单价")
	private BigDecimal goodsPrice;

	@Column(name="amount_price")
	@Comment("商品成交总价")
	private BigDecimal amountPrice;
}
