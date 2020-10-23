package com.hst.ii.user.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.ii.user.entity.TUser;

@Service
@Transactional
public class UserService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    /**
     * 锁定
     *
     * @param userIdList
     * @throws Exception
     */
    public void lock(List<String> userIdList) {
        for (String userid : userIdList) {
            TUser u = dao.get(TUser.class, userid);
            u.setLocked("1");
            dao.update(u);
        }
    }

    /**
     * 解锁
     *
     * @param userIdList
     * @throws Exception
     */
    public void unlock(List<String> userIdList) {
        for (String userid : userIdList) {
            TUser u = dao.get(TUser.class, userid);
            u.setLocked("0");
            dao.update(u);
        }
    }
}
