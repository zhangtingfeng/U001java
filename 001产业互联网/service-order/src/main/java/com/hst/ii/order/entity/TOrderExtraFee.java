package com.hst.ii.order.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_extra_fee")
@Comment("订单扩展费用")
@HSID(length = 10, prefix = "E")
@Data
@NoArgsConstructor
public class TOrderExtraFee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 30)
	@Comment("主键")
	@GeneratedValue(generator = "hsid")
	@GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
	private String id;

	@Column(name="id_order",length = 10)
	@Comment("订单id")
	private String orderId;

	@Comment("扩展信息ID(包装，运输等费用)")
	@Column(name = "id_extra")
	private String extraId;

	@Comment("费用")
	@Column
	private Double amount;

	@Comment("说明")
	@Column
	private String remark;
}
