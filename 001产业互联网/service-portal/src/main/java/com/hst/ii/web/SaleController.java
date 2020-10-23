package com.hst.ii.web;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.entity.TSaleInfo;
import com.hst.ii.service.SaleService;
import org.omg.CORBA.ORB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/15 16:49
 */
@Controller
@RequestMapping("/sale")
public class SaleController {
    @Autowired
    IORDao dao;

    @Autowired
    SaleService service;

    @RequestMapping(value = "/getSalesInfo", method = RequestMethod.POST)
    public void getSalesInfo(@RequestBody String data, Model m) throws IOException, ParseException, ParseException {
        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        String saleId= (String) reqData.get("id");
        TSaleInfo tSaleInfo = dao.get(TSaleInfo.class,saleId);
        m.addAttribute("goods", JsonUtil.toString(tSaleInfo));
        m.addAttribute("props", service.getSaleProps(saleId));
        m.addAttribute("imgs", service.getSaleImgs(saleId));
        m.addAttribute("org", JsonUtil.toString(service.getOrg(tSaleInfo.getOrgId())));
        m.addAttribute("wh", JsonUtil.toString(service.getWh(tSaleInfo.getWhId())));
    }

    @RequestMapping(value = "/getSalesList", method = RequestMethod.POST)
    public void getSalesList(@RequestBody String data, Model m) throws IOException, ParseException, ParseException {
        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        List<TSaleInfo> saleList = service.getSalesList(service.getListSalesQuerys(reqData));
        m.addAttribute("goods", saleList);
        m.addAttribute("props", service.getSaleProps(saleList));
        m.addAttribute("imgs", service.getSaleImgList(saleList));
        m.addAttribute("orgs", service.getOrgs(saleList));
    }

}
