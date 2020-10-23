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

    @Autowired
    MetaRepository mr;

    @RequestMapping("/getMetaInfo")
    public void getMetaInfo(String goodsId, Model m) throws ServiceException, InvocationTargetException, IllegalAccessException {
        MetaInfo mi =service.getGoodsMetaInfo(goodsId);
        if (null == mi)
            throw new ServiceException("1");
        m.addAttribute("meta", mi);
    }

    @RequestMapping(value="/release",method = RequestMethod.POST)
    public void releaseGoods(@RequestBody String data, Model m) throws IOException {
        Map<String,Object> reqData=JsonUtil.read(data,Map.class);
        service.saveReleaseGoods(reqData);

    }

    @RequestMapping(value="/getSalesList",method = RequestMethod.POST)
    public void getSalesList(@RequestBody String data, Model m) throws IOException, ParseException {
        User user= ServiceContext.getInstance().getUser();
        if(user==null){
            return;
        }

        Map<String,Object> reqData=JsonUtil.read(data,Map.class);
        List<TSaleInfo> saleList=service.getSalesList(reqData);
        m.addAttribute("goods",saleList) ;
        m.addAttribute("props", service.getSaleProps(saleList));
        m.addAttribute("imgs", service.getSaleImgs(saleList));
    }

    @RequestMapping(value="/delete",method = RequestMethod.POST)
    public void delete(@RequestBody String data, Model m) throws IOException, ParseException {
        Map<String,Object> reqData= JsonUtil.read(data,Map.class);
        String id= (String) reqData.get("saleId");
        service.delete(id);
    }

    @RequestMapping(value="/remove",method = RequestMethod.POST)
    public void remove(@RequestBody String data, Model m) throws IOException, ParseException {
        Map<String,Object> reqData= JsonUtil.read(data,Map.class);
        String id= (String) reqData.get("saleId");
        service.remove(id);
    }

    @RequestMapping(value="/releaseAgain",method = RequestMethod.POST)
    public void releaseAgain(@RequestBody String data, Model m) throws IOException, ParseException {
        Map<String,Object> reqData= JsonUtil.read(data,Map.class);
        String id= (String) reqData.get("saleId");
        service.releaseAgain(id);
    }
}
