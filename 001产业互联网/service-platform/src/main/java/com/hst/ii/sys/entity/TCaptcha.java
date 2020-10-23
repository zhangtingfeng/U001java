package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sys_captcha")
@Comment("验证码信息表")
@Data
@NoArgsConstructor
public class TCaptcha extends BaseEntity{
	
	@Id
	@Column(name = "id", length = 32)
	@Comment("ID")
	@GeneratedValue(generator = "hsid")
	@GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
	private String id;
	
	@Column(name = "captcha", length = 6)
	@Comment("6位验证码")
	private String captcha;
	
	@Column(name = "tel", length = 20)
	@Comment("手机号")
	private String tel;

}
