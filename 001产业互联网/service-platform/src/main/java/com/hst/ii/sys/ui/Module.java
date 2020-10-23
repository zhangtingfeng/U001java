/**
 * 
 */
package com.hst.ii.sys.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.sys.entity.TModule;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@EInfo(name="sys-module", title="系统模块|System modules", entity= TModule.class)
@Data
@NoArgsConstructor
public class Module extends MetaData {
	@EField(title="ID", form=2, query=FieldQuery.LIKE)
	@ESort
	private String id;
	
	@EField(title="名称|Name", query=FieldQuery.LIKE)
	private String name;

	@EField(title="版本|Version")
	private String version;

	@EField(title="URL", width = 4)
	private String url;

	@EField(title="说明|Remark", width = 4)
	private String remark;
}
