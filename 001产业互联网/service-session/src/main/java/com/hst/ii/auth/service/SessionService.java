package com.hst.ii.auth.service;

import com.hst.core.utils.JsonUtil;
import com.hst.ii.auth.dao.SessionDao;
import com.hst.ii.auth.po.LoginRequest;
import com.hst.ii.auth.po.Menu;
import com.hst.ii.auth.po.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SessionService {
    @Autowired
    RedisTemplate<String, String> redis;
    @Autowired
    SessionDao dao;

    private String getUserSessionKey(String sid) {
        return StringUtils.join(new String[]{"sid.", sid});
    }

    public User getUserSession(String sid) throws IOException {
        User user = null;
        String json = redis.opsForValue().get(getUserSessionKey(sid));
        if (StringUtils.isNotEmpty(json)) {
            user = JsonUtil.read(json, User.class);
            user.setSid(sid);
        }
        return user;
    }

    public void setUserSession(User user) throws IOException {
        String json = JsonUtil.toString(user);
        redis.opsForValue().set(getUserSessionKey(user.getSid()), json, Duration.ofDays(1));
    }

    public User login(LoginRequest request, String ip, String agent) throws IOException {
        // userid或者手机号登录皆可
        User user = dao.getByUserid(request.getUserid());
        if (null != user && StringUtils.equals(user.getPasswd(), request.getPasswd())) {
            // 非机构冻结、非用户锁定方可登录或者未注册用户可登录
            if ("1".equals(user.getState()) || user.getOrg() == null) {
                // 记录到数据库登录日志
                String sid = UUID.randomUUID().toString();
                dao.saveAuthLogin(sid, user.getId(), ip, agent);
                user.setSid(sid);

                // 记录session到redis
                setUserSession(user);

                return user;
            }

        }

        return null;
    }

    public User getUser(String sid, String uid) throws IOException {
        User u = getUserSession(sid);
        if (u != null && DigestUtils.md5Hex(u.getId()).equalsIgnoreCase(uid)) {
            u.setSid(sid);
            return u;
        } else {
            return null;
        }
    }

    public void logout(String sid) {
        dao.updateAuthLogof(sid);
        redis.delete(getUserSessionKey(sid));
    }

    public List<Menu> getUserMenus(User user) {
        return dao.getUserMenus(user.getId());
    }

    public List<String> getUserPrivileges(User user) {
        return dao.getUserPrivileges(user.getId());
    }
}
