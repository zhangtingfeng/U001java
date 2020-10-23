package com.hst.ii.user.po;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListingSaleInfo {

	private String id;
	private String code; // 发布编号
	private Date createTime; // 发布时间
	private String createUser; // 发布人员
	private String goodsName; // 商品名称 （最低级商品分类名称）
	private String goodsClassId; // 商品分类ID
	private String releaseTitle; // 发布标题
	private String unitPrice; // 含税单价
	private Integer releasTotal; // 发布总量
	private Integer minVolume; // 最小成交量
	private String deliveryArea; // 发货地区域
	private String deliveryAddress; // 发货地详细
	private Integer limitDays; // 挂牌时限
	private String status; // 发布状态 1 待审核 2 发布中 3 审核未通过 9 已下架
	private Date expiresTime;
	private Integer orderedQty;
	private String description;
	private String orgcode;
	private String usertype; // 0： 买家 / 1：卖家
	private List<Map<String, String>> props;

	public List<Map<String, String>> getProps() {
		return props;
	}

	public void setProps(List<Map<String, String>> props) {
		this.props = props;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsClassId() {
		return goodsClassId;
	}

	public void setGoodsClassId(String goodsClassId) {
		this.goodsClassId = goodsClassId;
	}

	public String getReleaseTitle() {
		return releaseTitle;
	}

	public void setReleaseTitle(String releaseTitle) {
		this.releaseTitle = releaseTitle;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getReleasTotal() {
		return releasTotal;
	}

	public void setReleasTotal(Integer releasTotal) {
		this.releasTotal = releasTotal;
	}

	public Integer getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(Integer minVolume) {
		this.minVolume = minVolume;
	}

	public String getDeliveryArea() {
		return deliveryArea;
	}

	public void setDeliveryArea(String deliveryArea) {
		this.deliveryArea = deliveryArea;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Integer getLimitDays() {
		return limitDays;
	}

	public void setLimitDays(Integer limitDays) {
		this.limitDays = limitDays;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(Date expiresTime) {
		this.expiresTime = expiresTime;
	}

	public Integer getOrderedQty() {
		return orderedQty;
	}

	public void setOrderedQty(Integer orderedQty) {
		this.orderedQty = orderedQty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

}
