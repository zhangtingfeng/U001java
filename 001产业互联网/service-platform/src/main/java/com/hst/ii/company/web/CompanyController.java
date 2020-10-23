package com.hst.ii.company.web;

import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.company.entity.TCompanyLog;
import com.hst.ii.company.entity.TLogApproval;
import com.hst.ii.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/3 11:05
 */
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    CompanyService service;

    /**
     * 企业认证申请审核
     */
    @RequestMapping("/approve")
    public void approve(@RequestBody TLogApproval tLogApproval, Model m) throws IllegalAccessException, InvocationTargetException {
        service.approve(tLogApproval);
    }

    /**
     * 企业认证信息
     *
     * @param data
     * @param m
     */
    @RequestMapping("/approveInfo")
    public void approveInfo(@RequestBody String data, Model m) throws IOException {

        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        m.addAttribute("approveInfo", service.approveInfo((String) reqData.get("companyLogId")));
    }
}
