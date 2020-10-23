package com.hst.ii.company.web;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.company.entity.TAccountLog;
import com.hst.ii.company.entity.TCompanyLog;
import com.hst.ii.company.entity.TMemberLog;
import com.hst.ii.company.service.CompanyService;
import com.hst.ii.sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/1 14:36
 */
@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    CompanyService companyService;

    /**
     * 获取用户信息
     */
    @RequestMapping("/userinfo")
    public void getUserInfo(Model m) throws IOException {
        String userid = ServiceContext.getInstance().getUser().getUid();
        TUser user = dao.get(TUser.class, userid);
        m.addAttribute("user", JsonUtil.toString(user));
    }

    /**
     * 临时用户申请信息保存处理
     */
    @RequestMapping("/apply")
    public void apply(@RequestBody String data, Model m) throws IOException, ServiceException {
        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        if (!reqData.containsKey("company")) {
            throw new ServiceException("1", "缺少企业申请信息！");
        }
        if (!reqData.containsKey("members")) {
            throw new ServiceException("2", "缺少企业成员申请信息！");
        }
        if (!reqData.containsKey("accounts")) {
            throw new ServiceException("3", "缺少企业账户申请信息！");
        }

        TCompanyLog tCompanyLog = JsonUtil.read(JsonUtil.toString(reqData.get("company")), TCompanyLog.class);
        List<TMemberLog> tMemberLogList = JsonUtil.readList(JsonUtil.toString(reqData.get("members")), TMemberLog.class);
        List<TAccountLog> tAccountLogList = JsonUtil.readList(JsonUtil.toString(reqData.get("accounts")), TAccountLog.class);

        companyService.apply(tCompanyLog, tMemberLogList, tAccountLogList);
    }

}
