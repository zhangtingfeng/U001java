package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_dept_user")
@HSID(length = 10, prefix = "sd", resetByDay = true)
@Comment("平台部门表")
@Data
@NoArgsConstructor
public class TSysDeptUser extends BaseEntity {

	@Id
	@Column(length = 10)
	@Comment("部门id")
	private String deptid;

	@Column(length = 10)
	@Comment("用户id")
	private String userid;

}
