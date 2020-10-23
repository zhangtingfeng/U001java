package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TRouteConfig
 *
 * @author WangYH
 * @date 2020/7/6
 */
@Entity
@Table(name = "sys_route_config")
@Comment("GateWay配置表")
@ResponseModel
@Data
@NoArgsConstructor
public class TRouteConfig {
    @Id
    @Column(length = 30)
    @Comment("ID")
    private String id;

    @Column(length = 50)
    @Comment("对应后台服务URI")
    private String uri;

    @Column(length = 50)
    @Comment("对应gateway的路径")
    private String path;


    @Column(length = 100)
    @Comment("需要的权限列表，以逗号分割")
    private String auths;
}
