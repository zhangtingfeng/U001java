package com.hst.ii.org.service;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.ii.sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrgService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    public void lock(List<TUser> tUserList) throws Exception {
        for (TUser tUser : tUserList) {
            tUser.setLocked("1");
            dao.update(tUser);
        }
    }

    public void unlock(List<TUser> tUserList) throws Exception {
        for (TUser tUser : tUserList) {
            tUser.setLocked("0");
            dao.update(tUser);
        }
    }
}
