package com.hst.ii.sys.ui;

import com.hst.core.annotation.ResponseModel;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.sys.entity.TRouteConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TRouteConfig
 *
 * @author WangYH
 * @date 2020/7/6
 */
@ResponseModel
@Data
@NoArgsConstructor
@EInfo(name="route-config", title="Gateway配置|Gateway config", entity = TRouteConfig.class)
public class RouteConfig extends MetaData {
    @EField(title="ID", form=2, query= FieldQuery.LIKE)
    @ESort
    private String id;

    @EField(title="后端服务URI|Backend URI")
    private String uri;

    @EField(title="Gateway路径|Gateway path")
    private String path;

    @EField(title="权限列表|Priviliges")
    private String auths;
}
