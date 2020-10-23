package com.hst.ii.org.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="sys_orginfo")
@Comment("组织机构表")
@HSID(length = 10, prefix = "O", resetByDay = true)
@Data
@NoArgsConstructor
public class TOrgInfo extends BaseEntity{

	@Id
	@Column(name = "id", length = 10)
	@Comment("ID")
	@GeneratedValue(generator = "hsid")
	@GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
	private String id;

	@Column(length = 30)
	@Comment("组织编码")
	private String code;

	@Column(name = "name", length = 500)
	@Comment("组织名称")
	private String name;

	@Column(name = "pid", length = 10)
	@Comment("父级机构")
	private String pId;

	@Column(name = "freeze", length = 1)
	@Comment("冻结状态  0:未冻结 1:冻结")
	private String freeze;

}

