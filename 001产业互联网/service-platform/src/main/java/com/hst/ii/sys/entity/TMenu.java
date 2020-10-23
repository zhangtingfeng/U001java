/**
 * 
 */
package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author wangyh
 *
 */
@Entity
@Table(name="sys_menu")
@Comment("菜单表")
@ResponseModel
@Data
@NoArgsConstructor
public class TMenu implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length=10)
	@Comment("菜单ID")
	private String id;
	
	@Column(length=10)
	@Comment("上级菜单ID,为0表示一级菜单")
	private String pid;
	
	@Column(length=30)
	@Comment("菜单图标")
	private String icon;
	
	@Column(length=30)
	@Comment("菜单名称")
	private String name;
	
	@Column(length=300)
	@Comment("菜单参数")	
	private String args;

}
