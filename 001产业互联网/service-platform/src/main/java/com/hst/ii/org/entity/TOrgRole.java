package com.hst.ii.org.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * @author Adrian
 *
 */
@Entity
@Table(name="sys_org_role")
@Comment("机构角色关系表")
@Data
@NoArgsConstructor
public class TOrgRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Comment("机构id")
	@Id
	@Column(length=10,name="id_org")
	private String orgid;
	
	@Comment("角色id")
	@Id
	@Column(length=10,name="id_role")
	private String roleid;

}
