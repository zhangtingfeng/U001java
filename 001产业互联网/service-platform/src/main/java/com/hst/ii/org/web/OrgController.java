package com.hst.ii.org.web;

import com.hst.core.annotation.WebAuth;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.org.service.OrgService;
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
     * 企业管理-锁定
     *
     * @param data
     * @param m
     * @throws Exception
     */
    @RequestMapping("/lock")
    public void lock(@RequestBody String data, Model m) throws Exception {
        Map<String, Object> lists = JsonUtil.read(data, Map.class);
        List<String> idList = (List<String>) lists.get("idList");
        service.lock(idList);
    }

    /**
     * 企业管理-解锁
     *
     * @param data
     * @param m
     * @throws Exception
     */
    @RequestMapping("/unlock")
    public void unlock(@RequestBody String data, Model m) throws Exception {
        Map<String, Object> lists = JsonUtil.read(data, Map.class);
        List<String> idList = (List<String>) lists.get("idList");
        service.unlock(idList);
    }
}
