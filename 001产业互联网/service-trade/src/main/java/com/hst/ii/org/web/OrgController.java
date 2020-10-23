package com.hst.ii.org.web;

import com.hst.core.annotation.WebAuth;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.org.service.OrgService;
import com.hst.ii.sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@WebAuth
@RequestMapping("/org")
public class OrgController {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    OrgService service;

    /**
     * 机构管理-用户锁定
     *
     * @param data
     * @param m
     * @throws Exception
     */
    @RequestMapping("/lock")
    public void lock(@RequestBody String data, Model m) throws Exception {
        List<TUser> tUserList = JsonUtil.readList(data, TUser.class);
        service.lock(tUserList);
    }

    /**
     * 机构管理-用户解锁
     *
     * @param data
     * @param m
     * @throws Exception
     */
    @RequestMapping("/unlock")
    public void unlock(@RequestBody String data, Model m) throws Exception {
        List<TUser> tUserList = JsonUtil.readList(data, TUser.class);
        service.unlock(tUserList);
    }
}
