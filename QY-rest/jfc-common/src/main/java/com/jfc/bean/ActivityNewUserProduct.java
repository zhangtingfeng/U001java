package com.jfc.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by MJ on 2018-10-08.
 */
public class ActivityNewUserProduct implements Serializable{

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
     * 所需金额可能为0
     */
    private BigDecimal money;

    /**
     * 每人可兑换次数
     */
    private Integer numbers;

    /**
     *  开始时间
     */
    private Date start;

    /**
     *  结束时间
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
        return "ActivityNewUserProduct{" +
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
                ", numbers=" + numbers +
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSecId() {
        return secId;
    }

    public void setSecId(Long secId) {
        this.secId = secId;
    }

    public Long getFullId() {
        return fullId;
    }

    public void setFullId(Long fullId) {
        this.fullId = fullId;
    }

    public Long getNewuserId() {
        return newuserId;
    }

    public void setNewuserId(Long newuserId) {
        this.newuserId = newuserId;
    }

    public Long getSpecialpId() {
        return specialpId;
    }

    public void setSpecialpId(Long specialpId) {
        this.specialpId = specialpId;
    }

    public String getProNme() {
        return proNme;
    }

    public void setProNme(String proNme) {
        this.proNme = proNme;
    }

    public String getPnme() {
        return pnme;
    }

    public void setPnme(String pnme) {
        this.pnme = pnme;
    }

    public String getPintroduction() {
        return pintroduction;
    }

    public void setPintroduction(String pintroduction) {
        this.pintroduction = pintroduction;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getSmallpic() {
        return smallpic;
    }

    public void setSmallpic(String smallpic) {
        this.smallpic = smallpic;
    }

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public String getSpic() {
        return spic;
    }

    public void setSpic(String spic) {
        this.spic = spic;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getProShopId() {
        return proShopId;
    }

    public void setProShopId(Long proShopId) {
        this.proShopId = proShopId;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getProStatus() {
        return proStatus;
    }

    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdator() {
        return updator;
    }

    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
