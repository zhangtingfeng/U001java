package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sys_module")
@Comment("系统模块")
@ResponseModel
@Data
@NoArgsConstructor
public class TModule {
    @Id
    @Column(length = 30)
    @Comment("模块id")
    private String id;

    @Column(length = 50)
    @Comment("模块名称")
    private String name;

    @Column(length = 50)
    @Comment("模块base url")
    private String url;

    @Column(length = 10)
    @Comment("版本")
    private String version;

    @Column(length = 255)
    @Comment("说明")
    private String remark;
}
