package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.ResponseModel;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * @author wangyh
 *
 */
@Entity
@Table(name = "sys_role")
@Comment("角色表")
@ResponseModel
@Data
@NoArgsConstructor
public class TRole extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Comment("角色id")
	@Id
	@Column(length = 10, name = "id")
	private String id;

	@Comment("角色名称")
	@Column(length = 30, name = "name")
	private String name;
	
}
