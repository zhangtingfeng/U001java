/**
 * 
 */
package com.hst.ii.common.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author luwenjie
 *
 */
@Entity
@Table(name = "sys_user", indexes = { @Index(unique = true, columnList = "userid")})
@HSID(length = 10, prefix = "U", resetByDay = true)
@Comment("系统用户表")
@Data
@NoArgsConstructor
public class TUser extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 10)
	@GeneratedValue(generator = "hsid")
	@Comment("ID")
	private String id;

	@Column(length = 20)
	@Comment("用户id")
	private String userid;

	@Column(length = 20)
	@Comment("姓名")
	private String name;

	@Column(length = 20)
	@Comment("密码")
	private String passwd;

	@Column(length = 50)
	@Comment("Email")
	private String email;

	@Column(length = 15)
	@Comment("mobile")
	private String mobile;
	
	@Column(name="id_org",length = 10)
	@Comment("所属机构")
	private String orgId;
	
	@Column(length = 32)
	@Comment("状态")
	private String status;
	
	@Column(length = 32)
	@Comment("账号类型：0：临时 1：正式")
	private String type;
	
	@Column(length = 32)
	@Comment("openid")
	private String openid;

	@Column(name="is_locked",length = 1)
	@Comment("锁定(0:未锁定状态/ 1：锁定状态)")
	private String isLocked;

}