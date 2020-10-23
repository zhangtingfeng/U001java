package com.hst.ii.order.web;

import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.common.entity.TUserCart;
import com.hst.ii.flow.service.FlowService;
import com.hst.ii.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    OrderService service;

    @Autowired
    FlowService flowService;

    /**
     * 购物车订单生成
     */
    @RequestMapping("/createOrders")
    public void createOrders(@RequestBody String data, Model m) throws IOException, ServiceException {

        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        String addrId = (String) reqData.get("addrId");
        List<TUserCart> userCarts = JsonUtil.readList(JsonUtil.toString(reqData.get("carts")), TUserCart.class);
        String orderId = service.createOrders(userCarts, addrId);
        m.addAttribute("orderId", orderId);
    }


    @RequestMapping(value = "/as/{id}", method = RequestMethod.GET)
    public void getOrderProcees3(@PathVariable String id, Model m) throws IOException, ServiceException {

        m.addAttribute("order",  service.createfatermarket(id));

    }

}
