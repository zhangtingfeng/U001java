package com.jfc.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by MJ on 2018-10-08.
 */
public class ActivitySeckillProduct implements Serializable{

    /**
     *  主键ID
     */
    private Long id;

    /**
     *  商品id
     */
    private Long productId;

    /**
     *  商铺ID
     */
    private Long shopId;

    /**
     *  秒杀id
     */
    private Long secId;

    /**
     *  满减表id
     */
    private Long fullId;

    /**
     *  新注册用户活动id
     */
    private Long newuserId;

    /**
     *  特殊商品兑换id
     */
    private Long specialpId;

    /**
     * 商品名称
     */
    private String proNme;

    /**
     * 商品名称
     */
    private String pnme;

    /**
     * 商品简介
     */
    private String pintroduction;

    /**
     * 商品首图
     */
    private String pics;

    /**
     * 商品编号
     */
    private String proNumber;

    /**
     * 商品价格
     */

    private BigDecimal price;

    /**
     * 二级价格
     */
    private  BigDecimal price2;

    /**
     * 联盟价格
     */

    private BigDecimal price3;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 手机略缩图
     */
    private String smallpic;

    /**
     * pc缩略图
     */
    private String bigpic;

    /**
     * 详情页展示图
     */
    private String spic;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 所属类别
     */
    private Long typeId;

    /**
     * 产品所属店铺
     */
    private Long proShopId;

    /**
     * 所属价格类别
     */
    private Long priceId;

    /**
     * 所属品牌
     */
    private Long brandId;

    /**
     * 产品状态
     */
    private String proStatus;

    /**
     * 秒杀价格
     */
    private BigDecimal money;

    /**
     * 规则编号
     */
    private String number;

    /**
     * 是否循环 0 否  1 是
     */
    private String loopFlag;

    /**
     * 秒杀时间
     */
    private Date killTime;

    /**
     * 间隔时间小时
     */
    private Integer space;

    /**
     *  循环开始时间
     */
    private Date start;

    /**
     *  循环结束时间
     */
    private Date end;

    /**
     *  状态
     */
    private Long status;

    /**
     *  创建时间
     */
    private Date createTime;

    /**
     *  创建人
     */
    private Long creator;

    /**
     *  修改时间
     */
    private Date updateTime;

    /**
     *  修改人
     */
    private Long updator;

    /**
     *  是否删除
     */
    private String isDelete;

    @Override
    public String toString() {
        return "ActivitySeckillProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", shopId=" + shopId +
                ", secId=" + secId +
                ", fullId=" + fullId +
                ", newuserId=" + newuserId +
                ", specialpId=" + specialpId +
                ", proNme='" + proNme + '\'' +
                ", pnme='" + pnme + '\'' +
                ", pintroduction='" + pintroduction + '\'' +
                ", pics='" + pics + '\'' +
                ", proNumber='" + proNumber + '\'' +
                ", price=" + price +
                ", price2=" + price2 +
                ", price3=" + price3 +
                ", stock=" + stock +
                ", introduction='" + introduction + '\'' +
                ", discount=" + discount +
                ", smallpic='" + smallpic + '\'' +
                ", bigpic='" + bigpic + '\'' +
                ", spic='" + spic + '\'' +
                ", tagId=" + tagId +
                ", typeId=" + typeId +
                ", proShopId=" + proShopId +
                ", priceId=" + priceId +
                ", brandId=" + brandId +
                ", proStatus='" + proStatus + '\'' +
                ", money=" + money +
                ", number='" + number + '\'' +
                ", loopFlag='" + loopFlag + '\'' +
                ", killTime=" + killTime +
                ", space=" + space +
                ", start=" + start +
                ", end=" + end +
                ", status=" + status +
                ", createTime=" + createTime +
                ", creator=" + creator +
                ", updateTime=" + updateTime +
                ", updator=" + updator +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
