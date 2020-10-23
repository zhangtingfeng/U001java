/**
 * 
 */
package com.hst.ii.basedata.ui;

import com.hst.core.entity.TDict;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;

/**
 * @author wangyh
 *
 */
@EInfo(name="dict", entity=TDict.class, title="数据字典|Dictionary")
public class Dict extends MetaData{
	
	@EField(title="字典类别", query=FieldQuery.EQ, viewer="dict.dict-catalog", editor="select.dict-catalog", form=2)
	@ESort("asc")
	private String name;
	
	@EField(title="ID", validate="required|max:30",form=2)
	private String id;
	
	@EField(title="名称|Label", validate="required|max:50",query=FieldQuery.LIKE,width=4)
	private String text;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
