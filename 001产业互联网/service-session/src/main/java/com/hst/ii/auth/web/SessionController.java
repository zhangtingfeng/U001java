package com.hst.ii.auth.web;

import com.hst.core.utils.HttpRequestUtils;
import com.hst.ii.auth.dao.SessionDao;
import com.hst.ii.auth.po.ChgPwdRequest;
import com.hst.ii.auth.po.LoginRequest;
import com.hst.ii.auth.po.User;
import com.hst.ii.auth.service.SessionService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class SessionController {
    public static final String SID_HEADER = "hst-ii-sid";

    @Autowired
    SessionService auth;

    @Autowired
    SessionDao dao;

    private String getCookieValue(HttpServletRequest request, String name) {
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName()))
                return cookie.getValue();
        }

        return null;
    }

    public String getSidFromRequest(HttpServletRequest request) {
        return request.getHeader(SID_HEADER);
    }

    private Map<String, Object> getUserJson(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("userid", user.getId());
        data.put("org", user.getOrg());
        data.put("name", user.getName());
        data.put("state", user.getState());
        return data;
    }


    private User getUser(HttpServletRequest request) {
        User user = null;
        String sid = getSidFromRequest(request);
        if (null != sid && !"".equals(sid)) {
            try {
                user = auth.getUserSession(sid);
            } catch (IOException e) {
            }
        }
        return user;


//        String sid = getCookieValue(request, AuthKeys.COOKIE_SID);
//        String uid = sid == null ? null : getCookieValue(request, AuthKeys.COOKIE_UID);
//        User user = null;
//        try {
//            user = auth.getUser(sid, uid);
//        } catch (IOException e) {
//        }
//        return user;
    }

    /**
     * 获取用户登录信息
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> idx(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        User user = getUser(request);
        if (user != null) {
            data.put("user", getUserJson(user));

            if ("1".equals(request.getParameter("menu"))) {
                data.put("menu", auth.getUserMenus(user));
            }
            if ("1".equals(request.getParameter("auths"))) {
                data.put("auths", auth.getUserPrivileges(user));
            }
        }

        return data;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Map<String, Object> login(@RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> data = new HashMap<>();
        User user = auth.login(login,
                HttpRequestUtils.getClientIp(request),
                HttpRequestUtils.getClientAgent(request.getHeader("User-Agent")));
        if (null != user) {
            String uid = DigestUtils.md5Hex(user.getId());
            Cookie cookie = new Cookie("uid", uid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(login.isAutoLogin() ? Integer.MAX_VALUE : -1);
            response.addCookie(cookie);
            cookie = new Cookie("sid", user.getSid());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(login.isAutoLogin() ? Integer.MAX_VALUE : -1);
            response.addCookie(cookie);
            data.put("code", 0);
            data.put("user", getUserJson(user));
        } else {
            data.put("code", 1);
        }

        return data;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void logoff(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = getUser(request);
        if (null != user) {
            auth.logout(user.getSid());

            Cookie cookie = new Cookie("uid", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            cookie = new Cookie("sid", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> chgPwd(@RequestBody ChgPwdRequest chg, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();

        if (chg == null) {
            data.put("msg", "获取请求信息为空！");
            return data;
        }
        if (chg.getPwd1() == null || chg.getPwd2() == null) {
            data.put("msg", "获取请求密码信息为空！");
            return data;
        }
        User user = getUser(request);
        if (user == null) {
            data.put("msg", "获取登录用户信息失败！");
            return data;
        }
        String passwd = user.getPasswd();
        if (passwd == null) {
            passwd = dao.getPasswdById(user.getId());
            if (passwd == null) {
                data.put("msg", "获取用户当前密码失败！");
                return data;
            }
        }

        if (!passwd.equals(chg.getPwd1())) {
            data.put("msg", "用户密码不正确！");
            return data;
        }

        dao.updateUserPasswd(user.getId(), chg.getPwd2());
        return data;
    }
}