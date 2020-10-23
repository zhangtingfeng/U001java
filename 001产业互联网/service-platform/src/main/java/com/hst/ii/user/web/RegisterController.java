package com.hst.ii.user.web;

import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.user.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    IORDao dao;

    @Autowired
    RegisterService service;

    @RequestMapping(value = "/registerUser")
    public void registerUser(@RequestBody String data, Model m) throws IOException, ServiceException {
        Map<String, String> reqData = JsonUtil.read(data, Map.class);
        String mobile = reqData.get("mobile");
        String passwd = reqData.get("passwd");
        String userid = reqData.get("userid");
        String captcha = reqData.get("captcha");
        service.register(mobile, captcha, userid, passwd);
    }

    @RequestMapping(value = "/findPasswd")
    public void findPasswd(@RequestBody String data, Model m) throws IOException, ServiceException {
        Map<String, String> reqData = JsonUtil.read(data, Map.class);
        String mobile = reqData.get("mobile");
        String passwd1 = reqData.get("passwd1");
        String captcha = reqData.get("captcha");
        service.findPasswd(mobile, passwd1, captcha);
    }

    @RequestMapping(value = "/captcha")
    public void captcha(@RequestBody String data, Model m) throws IOException, ServiceException {
        Map<String, String> reqData = JsonUtil.read(data, Map.class);
        String tel = reqData.get("mobile");
        service.captcha(tel);
    }
}
