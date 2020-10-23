package com.hst.ii.web;

import com.hst.ii.entity.TUserCart;
import com.hst.ii.service.SaleService;
import com.hst.ii.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.hst.ii.web
 * @ClassName: UserCartController
 * @Author: xiangwenhao
 * @CreateTime: 2020/8/5 3:21 下午
 * @Description:
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private SaleService saleService;

    @RequestMapping("/getCartList")
    public void getCartList(Model m) {

        List<TUserCart> cartList = cartService.getCartList();
        if (cartList.size() == 0) {
            return;
        }
        m.addAttribute("carts", cartList);
        List<String> goodsIdList = cartService.getGoodsIdList(cartList);
        m.addAttribute("goods", cartService.getGoodsList(goodsIdList));
        m.addAttribute("imgs", cartService.getSaleImgInfos(goodsIdList));
        m.addAttribute("props", saleService.getSalePropInfos(goodsIdList));
        List<String> sellerIdList = cartService.getSellerIdList(cartList);
        m.addAttribute("sellers", cartService.getSellerInfoList(sellerIdList));
    }

    @RequestMapping("/addToCart")
    public void addToCart(@RequestBody TUserCart tUserCart, Model m) {
        cartService.addToCart(tUserCart);
    }


    @RequestMapping("/delFromCart")
    public void delFromCart(@RequestBody TUserCart tUserCart, Model m) {
        cartService.delFromCart(tUserCart.getId());
    }

    @RequestMapping("/getRcvAddrs")
    public void getRcvAddrs(Model m) {
        m.addAttribute("addrs", cartService.getRcvAddrs());
    }

    @RequestMapping("/clearCarts")
    public void clearCarts(Model m) {
        cartService.clearCarts();
    }

    @RequestMapping("/getCartNums")
    public void getCartNums(Model m) {
        m.addAttribute("cartNums", cartService.getCartNums());
    }
}
