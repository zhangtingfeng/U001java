package com.hst.ii.service;

import com.hst.core.MapBean;
import com.hst.core.ServiceContext;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.*;
import com.hst.ii.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

/**
 * @Package: com.hst.ii.service
 * @ClassName: UserCartService
 * @Author: xiangwenhao
 * @CreateTime: 2020/8/5 3:23 下午
 * @Description:
 */
@Service
public class CartService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    /**
     * 获取购物车数据列表
     *
     * @return
     */
    public List<TUserCart> getCartList() {
        User user = ServiceContext.getInstance().getUser();

        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "buyerId", user.getUid()));
        querys.add(new ORQuery(Op.order, "sellerId", "asc"));
        return dao.list(TUserCart.class, querys);
    }

    public List<TSaleImg> getSaleImgInfos(List<String> saleIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", saleIdList));
        querys.add(new ORQuery(Op.eq, "type", "1"));
        querys.add(new ORQuery(Op.eq, "idx", "1"));
        querys.add(new ORQuery(Op.order, "salesId", "asc"));

        return dao.list(TSaleImg.class, querys);
    }

    /**
     * 获取商品ID列表
     *
     * @param cartList
     * @return
     */
    public List<String> getGoodsIdList(List<TUserCart> cartList) {
        List<String> goodsIdList = new ArrayList<>();
        for (TUserCart cart : cartList) {
            if (!goodsIdList.contains(cart.getGoodsId())) {
                goodsIdList.add(cart.getGoodsId());
            }
        }

        return goodsIdList;
    }

    /**
     * 获取商品列表
     *
     * @param goodsIdList
     * @return
     */
    public List<TSaleInfo> getGoodsList(List<String> goodsIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "id", goodsIdList));
        return dao.list(TSaleInfo.class, querys);
    }

    /**
     * 获取卖家ID列表
     *
     * @param cartList
     * @return
     */
    public List<String> getSellerIdList(List<TUserCart> cartList) {
        List<String> sellerIdList = new ArrayList<>();
        for (TUserCart cart : cartList) {
            if (!sellerIdList.contains(cart.getSellerId())) {
                sellerIdList.add(cart.getSellerId());
            }
        }

        return sellerIdList;
    }

    public List<Map<String, String>> getSellerInfoList(List<String> sellerIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("sellerIds", sellerIdList);
        return (List<Map<String, String>>) sql.query("cart.getUsers", params);
    }

    /**
     * 加入购物车
     *
     * @param cart
     */
    public void addToCart(TUserCart cart) {
        //获取登录用户
        User user = ServiceContext.getInstance().getUser();
        if (user == null) {
            return;
        }
        //查询购物车数据
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "buyerId", user.getUid()));
        querys.add(new ORQuery(Op.eq, "sellerId", cart.getSellerId()));
        querys.add(new ORQuery(Op.eq, "goodsId", cart.getGoodsId()));
        List<TUserCart> cartList = dao.list(TUserCart.class, querys);

        if (cartList.size() > 0) {
            TUserCart userCart = cartList.get(0);
            userCart.setNum(userCart.getNum() + cart.getNum());
            userCart.setUpdateTime(new Date());
            dao.update(userCart);
        } else {
            cart.setCreateTime(new Date());
            cart.setBuyerId(user.getUid());
            cart.setNum(cart.getNum());
            dao.save(cart);
        }
    }

    /**
     * 删除购物车商品
     *
     * @param id
     */
    public void delFromCart(String id) {

        TUserCart userCart = dao.get(TUserCart.class, id);
        dao.delete(userCart);
    }

    /**
     * 删除购物车商品
     */
    public void clearCarts() {

        User user = ServiceContext.getInstance().getUser();
        MapBean<String, Object> params = new MapBean<>();
        params.put("buyerId", user.getUid());
        sql.delete("cart.deleteCarts", params);
    }

    /**
     * 获取收货地址
     */
    public List<TOrgReceiveAddress> getRcvAddrs() {
        String orgId = ServiceContext.getInstance().getUser().getOrgcode();
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orgId", orgId));
        querys.add(new ORQuery(Op.order, "type", "asc"));
        return dao.list(TOrgReceiveAddress.class, querys);
    }

    /**
     * 购物车数量
     */
    public int getCartNums() {

        User user = ServiceContext.getInstance().getUser();
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "buyerId", user.getUid()));
        List<TUserCart> cartList = dao.list(TUserCart.class, querys);
        return cartList.size();
    }

}
