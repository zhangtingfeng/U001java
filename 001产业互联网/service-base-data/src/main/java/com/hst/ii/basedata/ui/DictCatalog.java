/**
 * 
 */
package com.hst.ii.basedata.ui;

import com.hst.core.entity.TDictCatalog;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;

/**
 * @author wangyh
 *
 */
@EInfo(name = "dict-catalog", entity = TDictCatalog.class, title = "数据字典类型|Dictionary catalog")
public class DictCatalog extends MetaData {
	
	@EField(title = "ID", validate = "required|max:30", list = true, form = 2)
	private String id;

	@EField(title = "名称|Label", validate = "required|max:50", list = true, form = 1)
	private String name;
	
	@EField(title = "备注|Remark", form = 1,editor="textarea", width=4)
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
