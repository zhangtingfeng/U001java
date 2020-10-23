package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sys_dept")
@HSID(length = 10, prefix = "sd", resetByDay = true)
@Comment("平台部门表")
@Data
@NoArgsConstructor
public class TSysDept extends BaseEntity {

	@Id
	@Column(name = "id", length = 10)
	@Comment("ID")
	@GeneratedValue(generator = "hsid")
	@GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
	private String id;

	@Column(length = 50)
	@Comment("部门名称")
	private String deptname;

	@Column(length = 10)
	@Comment("父id")
	private String pid;

}
