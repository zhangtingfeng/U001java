package com.hst.ii.company.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 交易商申请审核表
 * @author: ZhaJun
 * @time: 2020/7/10 13:41
 */
@Entity
@Table(name = "comp_log_approval")
@Comment("交易商申请审核表")
@HSID(length = 10, prefix = "A")
@Data
@NoArgsConstructor
public class TLogApproval implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 10)
	@GeneratedValue(generator = "hsid")
	@GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
	@Comment("ID")
	private String id;

	@Column(name = "id_company_log", length = 10)
	@Comment("公司LogID")
	private String companyLogId;

	@Column(length = 10)
	@Comment("审批时间")
	private Date time;

	@Column(length = 10)
	@Comment("审批人员")
	private String operator;

	@Column(length = 1)
	@Comment("审批结果 1 审批通过 2 审批不通过")
	private String result;

	@Column(length = 100)
	@Comment("审批意见")
	private String opinions;

}
