package com.hst.ii.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hst.core.annotation.Comment;

/**
 * 
 * @author Adrian
 *
 */
@Entity
@Table(name="sys_user_role")
@Comment("用户角色关系表")
public class TUserRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Comment("用户id")
	@Id
	@Column(length=10,name="id_user")
	private String userid;
	
	@Comment("角色id")
	@Id
	@Column(length=10,name="id_role")
	private String roleid;

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the roleid
	 */
	public String getRoleid() {
		return roleid;
	}

	/**
	 * @param roleid the roleid to set
	 */
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
}
