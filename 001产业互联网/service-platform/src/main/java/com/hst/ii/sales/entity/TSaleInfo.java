package com.hst.ii.sales.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 挂牌销售信息
 * @author: ZhaJun
 * @time: 2020/7/10 13:41
 */
@Entity
@Table(name = "sale_info", indexes = {@Index(name = "idx_code", unique = true, columnList = "code")})
@Comment("挂牌销售信息表")
@HSID(length = 10, prefix = "S")
@Data
@NoArgsConstructor
public class TSaleInfo implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_org", length = 10)
    @Comment("机构ID")
    private String orgId;

    @Column(name = "id_wh", length = 10)
    @Comment("仓库ID")
    private String whId;

    @Column(length = 18)
    @Comment("发布编号")
    private String code;

    @Column(name = "create_time")
    @Comment("发布时间")
    private Date createTime;

    @Column(name = "create_user", length = 10)
    @Comment("发布人员")
    private String createUser;

    @Column(name = "goods_name", length = 200)
    @Comment("商品名称 （最低级商品分类名称）")
    private String goodsName;

    @Column(name = "id_goods_class", length = 10)
    @Comment("商品分类ID")
    private String goodsClassId;

    @Column(name = "title", length = 100)
    @Comment("发布标题")
    private String title;

    @Column(name = "unit_price")
    @Comment("含税单价")
    private BigDecimal unitPrice;

    @Column(name = "total_qty")
    @Comment("发布总量")
    private Integer totalQty;

    @Column(length = 10)
    @Comment("单位")
    private String unit1;

    @Column(length = 20)
    @Comment("单位名称")
    private String unit1Name;

    @Column(name = "min_qty")
    @Comment("最小成交量")
    private Integer minQty;

    @Column(length = 10)
    @Comment("单位")
    private String unit2;

    @Column(length = 20)
    @Comment("单位名称")
    private String unit2Name;

    @Column(name = "frozen_qty")
    @Comment("冻结数量")
    private Integer frozenQty;

    @Column(name = "remain_qty")
    @Comment("剩余数量")
    private Integer remainQty;

    @Column(name = "limit_days")
    @Comment("挂牌时限")
    private Integer limitDays;

    @Column(length = 2)
    @Comment("发布状态 1 待审核 2 发布中 3 审核未通过 9 已下架")
    private String status;

    private Date expiresTime;

}
