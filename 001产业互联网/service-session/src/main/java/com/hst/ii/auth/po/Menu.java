/**
 * 
 */
package com.hst.ii.auth.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@Data
@NoArgsConstructor
public class Menu {
	private String id;
	private String pid;
	private String icon;
	private String name;
	private String args;
}
