package com.hst.ii.web;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/4 9:43
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    @RequestMapping(value="/getClassInfo",method = RequestMethod.GET)
    public void getGoodsClass(Model m){
        List<Map<String,Object>> results1= (List<Map<String, Object>>) sql.query("home.getOneGoodsClass");
        List<Map<String,String>> results2= (List<Map<String, String>>) sql.query("home.getGoodsClass");
        for(Map<String,Object> r1:results1){
            List<Map<String,String>> chidren=new ArrayList<>();
            String id=(String)r1.get("id");
            for(Map<String,String> r2:results2){
                if(id.equals(r2.get("pid"))){
                    chidren.add(r2);
                }
            }
            r1.put("children",chidren);
        }
        m.addAttribute("class",results1);
    }

}
