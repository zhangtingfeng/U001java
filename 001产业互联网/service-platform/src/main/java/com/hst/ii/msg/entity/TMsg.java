package com.hst.ii.msg.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.ResponseModel;
import com.hst.core.dao.BaseEntity;
import com.hst.core.meta.annotation.EField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * @author wangyh
 *
 */
@Entity
@Table(name = "sys_templte_smg")
@Comment("系统消息模板表")
@ResponseModel
@Data
@NoArgsConstructor
public class TMsg extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	@Id
	@Column(name = "id", length = 10)
	@Comment("消息模板ID")
	@GeneratedValue(generator = "hsid")
	@GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
	private String id;


	@Comment("业务节点")
	@Column(length = 30, name = "msgSendBusinessNode")
	private String msgSendBusinessNode;


	@Comment("发送对象")
	@Column(length = 30, name = "msgSendOBJ")
	private String msgSendOBJ;

	@Comment("发送方式")
	@Column(length = 30, name = "msgSendWay")
	private String msgSendWay;

	@Comment("消息内容")
	@Column(length =330, name = "sendcontent")
	private String sendcontent;



}
