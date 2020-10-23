package com.hst.ii.basedata.ui;

import com.hst.core.annotation.ResponseModel;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.basedata.entity.TBaseGoodsProp;
import com.hst.ii.basedata.proxy.BaseGoodsPropProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

@ResponseModel
@Data
@NoArgsConstructor
@EInfo(name = "base-goods-prop", title = "商品属性", entity = TBaseGoodsProp.class, proxy = BaseGoodsPropProxy.class)
public class BaseGoodsProp extends MetaData {
    @EField(title = "id", list = false, form = 0)
    @ESort
    private String id;

    @EField(title = "属性编码", form = 2, group = "基础配置", validate = "required")
    private String code;

    @EField(title = "属性名称", validate = "required")
    private String name;

    @EField(title = "属性分组", viewer = "dict.propGroup", editor = "select.propGroup", validate = "required", query = FieldQuery.EQ)
    private String propGroup;

    @EField(title = "单位", viewer = "dict.unit", editor = "select.unit")
    private String unit;

    @EField(title = "编辑类型|Editor", group = "其他配置", viewer = "dict.editorType", editor = "select.editorType", validate = "required")
    private String editor;

    @EField(title = "编辑参数|Editor args", list = false, form = 10)
    private String editorArgs;

    @EField(title = "是否必填", viewer = "dict.required", editor = "select.required", validate = "required")
    private String required;

    @EField(title = "表单编辑|Form", viewer = "dict.evisible", editor = "select.evisible", list = false, form = 0)
    private String form;

    @EField(title = "宽度|Width")
    private String width;

    @EField(title = "排序|Order", viewer = "dict.sort", editor = "select.sort")
    private String sort;

    @EField(title = "顺序|Sort", validate = "required")
    @ESort
    private String idx;

//    @EField(title = "控件类型", group = "其他配置", viewer = "dict.componentType", editor = "select.componentType", validate = "required")
//    private String componentType;
//
//    @EField(title = "控件参数", list = false, form = 0)
//    private String componentArgs;

//    @EField(title="属性分类",viewer = "dict.propCategory",editor = "select.propCategory",query = FieldQuery.EQ)
//    private String propCategory;

//    @EField(title="符号",viewer = "dict.symbol",editor = "select.symbol")
//    private String symbol;

    /**
     * 查询方式
     */
//    @EField(title = "查询|Query", viewer="dict.query", editor="select.query")
//    private String query;

//    /**
//     * 表单分组
//     */
//    @EField(title = "分组|Group")
//    private String groupTitle;

//    /**
//     * 校验规则
//     */
//    @EField(title = "校验规则|Validator")
//    private String validate;

//    @EField(title="数据类型", list = false, form = 0, viewer = "dict.dataType",editor = "select.dataType")
//    private String dataType;

//    /**
//     * 标题
//     */
//    @EField(title = "标题|Title", query=FieldQuery.LIKE, list = false, form = 0)
//    private String title;

//    /**
//     * 显示view
//     */
//    @EField(title = "显示|Viewer", list = false, form = 0)
//    private String view;

//    /**
//     * 显示view的参数
//     */
//    @EField(title = "显示参数|Viewer args", list = false, form = 0)
//    private String viewArgs;


//    /**
//     * 是否显示在列表
//     */
//    @EField(title = "列表显示|List", viewer="dict.visible", editor="select.visible", list = false, form = 0)
//    private String list;


//    /**
//     * 是否分行显示
//     */
//    @EField(title = "分行显示|Is Line Show", list = false, form = 0)
//    private String isLineShow;


}
