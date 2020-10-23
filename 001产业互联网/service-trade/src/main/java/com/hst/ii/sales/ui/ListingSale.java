package com.hst.ii.sales.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.sales.entity.TSaleInfo;
import com.hst.ii.sales.proxy.SaleProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/13 14:51
 */
@Data
@NoArgsConstructor
@EInfo(name = "listing-sale", title = "挂牌销售信息-新增", entity = TSaleInfo.class, proxy = SaleProxy.class)
public class ListingSale extends MetaData {

    @EField(title = "流水号", list = false, form = 0)
    private String id;

    @EField(title = "机构ID", list = false, form = 0)
    private String orgId;

    @EField(title = "发布编号", list = false, form = 0)
    private String code;

    @EField(title = "发布时间", list = false, form = 0)
    private Date createTime;

    @EField(title = "发布人员", list = false, form = 0)
    private String createUser;

    @EField(title = "商品名称", list = false, form = 0)
    private String goodsName;

    @EField(title = "商品分类ID", list = false, form = 0)
    private String goodsClassId;

    @EField(group = "资源信息", title = "发布标题", validate = "required", width = 4, idx = 101)
    private String title;

    @EField(title = "含税单价（元）", validate = "required", idx = 102)
    private BigDecimal unitPrice;

    @EField(title = "发布总量", validate = "required", idx = 103)
    private Integer totalQty;

    @EField(title = "单位", viewer = "dict.goodsUnit", editor = "select.goodsUnit", validate = "required", idx = 104)
    private String unit1;

    @EField(title = "最小成交量", validate = "required", idx = 105)
    private Integer minQty;

    @EField(title = "单位", viewer = "dict.goodsUnit", editor = "select.goodsUnit", validate = "required", idx = 106)
    private String unit2;

    @EField(title = "冻结数量", list = false, form = 0)
    private Integer frozenQty;

    @EField(title = "剩余数量", list = false, form = 0)
    private Integer remainQty;

    @EField(title = "发货仓库", viewer = "dict.org-warehouse", editor = "select.org-warehouse", validate = "required", idx = 107)
    private String whId;

    @EField(group = "挂牌时限", title = "挂牌时限", viewer = "dict.timeLimit", editor = "radiobox", list = false, form = 0, idx = 108)
    private Integer limitDays;

    @EField(title = "发布状态", list = false, form = 0)
    private String status;

}
