package com.hst.ii.org.service;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.Op;
import com.hst.ii.org.entity.TOrgInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrgService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    public void lock(List<String> idList) throws Exception {
        for (String id : idList) {
            TOrgInfo oi = dao.get(TOrgInfo.class, id);
            oi.setFreeze("1");
            dao.update(oi);
        }
    }

    public void unlock(List<String> idList) throws Exception {
        for (String id : idList) {
            TOrgInfo oi = dao.get(TOrgInfo.class, id);
            oi.setFreeze("0");
            dao.update(oi);
        }
    }
}
