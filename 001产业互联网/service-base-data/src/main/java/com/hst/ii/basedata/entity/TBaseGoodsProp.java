package com.hst.ii.basedata.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "base_goods_prop")
@HSID(length = 10, prefix = "P", resetByDay = true)
@Comment("商品属性表")
@ResponseModel
@Data
@NoArgsConstructor
public class TBaseGoodsProp {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("id")
    private String id;

    @Column(name = "prop_group", length = 10)
    @Comment("属性分组")
    private String propGroup;

    @Column(length = 200)
    @Comment("属性编码")
    private String code;

    @Column(length = 200)
    @Comment("属性名称")
    private String name;

    @Column(length = 20)
    @Comment("单位")
    private String unit;

    @Column(length = 10)
    @Comment("是否必填")
    private String required;

    @Column(length = 50)
    @Comment("编辑器")
    private String editor;

    @Column(name = "editor_args", length = 1000)
    @Comment("编辑器的参数")
    private String editorArgs;

    @Column
    @Comment("是否支持表单编辑")
    private String form;

    @Column(length = 5)
    @Comment("排序")
    private String sort;

    @Comment("显示或者编辑的宽度（1，2，3，4）")
    @Column
    private String width;

    @Column
    @Comment("顺序")
    private String idx;

//    @Column(name = "prop_category", length = 10)
//    @Comment("属性分类")
//    private String propCategory;

//    @Column(name = "component_type", length = 10)
//    @Comment("控件类型")
//    private String componentType;
//
//    @Column(name = "component_args", length = 2000)
//    @Comment("控件参数")
//    private String componentArgs;

//    @Column(name = "data_type", length = 10)
//    @Comment("数据类型")
//    private String dataType;

//    @Column(length = 20)
//    @Comment("符号")
//    private String symbol;

//    @Column(length=30)
//    @Comment("标题")
//    private String title;

//    @Column(length=10)
//    @Comment("显示view")
//    private String view;
//
//    @Column(name="view_args",length=20)
//    @Comment("显示view的参数")
//    private String viewArgs;
//


//    @Column
//    @Comment("查询方式")
//    private String query;

//    @Column
//    @Comment("是否显示在列表")
//    private String list;
//
//    @Column
//    @Comment("是否支持表单编辑")
//    private String form;

//    @Column(name="group_title",length=30)
//    @Comment("表单分组")
//    private String groupTitle;

//    @Column(length=50)
//    @Comment("校验规则")
//    private String validate;
//
//    @Column(name="is_line_show",length=1)
//    @Comment("是否分行 0 非分行显示 1 分行显示")
//    private String isLineShow;

}

