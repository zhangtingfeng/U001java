package com.hst.ii.user.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hst.ii.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hst.core.annotation.WebAuth;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.utils.JsonUtil;

@Controller
@WebAuth
@RequestMapping("/user")
public class UserController {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    UserService service;

    @RequestMapping("/lock")
    public void lock(@RequestBody String data, Model m) throws IOException {
        Map<String, Object> dataMap = JsonUtil.read(data, Map.class);
        List<String> userIdlist = (List<String>) dataMap.get("userIds");
        service.lock(userIdlist);
    }

    @RequestMapping("/unlock")
    public void unlock(@RequestBody String data, Model m) throws IOException {
        Map<String, Object> dataMap = JsonUtil.read(data, Map.class);
        List<String> userIdlist = (List<String>) dataMap.get("userIds");
        service.unlock(userIdlist);
    }
}
