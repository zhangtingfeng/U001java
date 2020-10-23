/**
 * 
 */
package com.hst.ii.common.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wangyh
 *
 */
@Entity
@Table(name = "sys_district")
@Comment("地区表")
@HSID(length = 10, prefix = "D")
@Data
@NoArgsConstructor
public class TDistrict {
	@Id
	@Column(length = 6)
	private String id;

	@Column(length = 30)
	private String name;
	
}
