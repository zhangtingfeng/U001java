package com.hst.ii.sales.web;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.meta.MetaInfo;
import com.hst.core.meta.MetaRepository;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.sales.entity.TSaleInfo;
import com.hst.ii.sales.service.SaleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/15 16:49
 */
@Controller
@RequestMapping("/listing-sale")
public class SaleController {
    @Autowired
    IORDao dao;

    @Autowired
    SaleService service;

    @RequestMapping(value = "/getSalesList", method = RequestMethod.POST)
    public void getSalesList(@RequestBody String data, Model m) throws IOException, ParseException {
        User user = ServiceContext.getInstance().getUser();
        if (user == null) {
            return;
        }

        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        List<TSaleInfo> saleList = service.getSalesList(reqData);
        m.addAttribute("goods", saleList);
        m.addAttribute("props", service.getSaleProps(saleList));
        m.addAttribute("imgs", service.getSaleImgs(saleList));
        m.addAttribute("orgs", service.getOrgs(saleList));
        m.addAttribute("whs", service.getWhs(saleList));
    }

    /**
     * 商品审批
     *
     * @param data
     * @param m
     * @throws Exception
     */
    @RequestMapping("/approve")
    public void approve(@RequestBody String data, Model m) throws Exception {
        Map<String, Object> params = JsonUtil.read(data, Map.class);
        String result = (String) params.get("result");
        String remark = (String) params.get("remark");
        String salesId = (String) params.get("salesId");
        service.approve(salesId, result, remark);
        m.addAttribute("success", 1);
    }

    /**
     * 强制下架
     *
     * @param data
     * @param m
     * @throws Exception
     */
    @RequestMapping("/offShelf")
    private void offShelf(@RequestBody String data, Model m) throws Exception {
        String salesId = StringUtils.isNotBlank(data) ? data.substring(1, data.length() - 1) : null;
        service.offShelf(salesId);
    }
}
