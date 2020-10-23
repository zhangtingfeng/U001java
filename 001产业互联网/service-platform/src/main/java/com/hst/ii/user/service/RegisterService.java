package com.hst.ii.user.service;

import com.hst.core.MapBean;
import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.Op;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.sys.entity.TCaptcha;
import com.hst.ii.user.entity.TUser;
import com.hst.ii.user.entity.TUserRole;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class RegisterService {

    @Value("${ii.register.isCaptcha}")
    private Boolean isCaptcha;

    @Value("${ii.sms.regeister.template}")
    private String template;

    @Value("${ii.sms.regeister.signname}")
    private String signname;

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    SmsService smsService;

    /**
     * 生成验证码信息 保存进数据库
     *
     * @param tel
     */
    public void captcha(String tel) throws IOException, ServiceException {
        TCaptcha captcha = new TCaptcha();

        //生成验证码
        String sources = "0123456789";
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < 6; j++) {
            flag.append(sources.charAt(rand.nextInt(9)) + "");
        }

        MapBean<String, Object> datas = new MapBean<>();
        datas.put("code", flag.toString());
        // 发送验证码
        smsService.send(tel, template, signname, JsonUtil.toString(datas));

        captcha.setTel(tel);
        captcha.setCaptcha(flag.toString());
        dao.save(captcha);
    }

    /**
     * 注册新用户
     *
     * @param mobile
     * @param captcha
     * @param userid
     * @param passwd
     */
    public void register(String mobile, String captcha, String userid, String passwd) throws ServiceException {
        //查询是否手机号已经被注册
        List<ORQuery> querys = new ArrayList<ORQuery>();
        querys.add(new ORQuery(Op.eq, "mobile", mobile));
        List<TUser> users = dao.list(TUser.class, querys);
        if (users.size() > 0) {
            throw new ServiceException("1", "该手机号已经被注册！");
        }
        if (isCaptcha) {
            //校验验证码
            querys = new ArrayList<ORQuery>();
            querys.add(new ORQuery(Op.eq, "tel", mobile));
            querys.add(new ORQuery(Op.order, "createTime", "desc"));
            //有效期 5分钟
            Date date = new Date();
            date = DateUtils.addMinutes(date, -5);
            Timestamp t = new Timestamp(date.getTime());
            querys.add(new ORQuery(Op.ge, "createTime", t));
            List<TCaptcha> captchas = dao.list(TCaptcha.class, querys);
            if (captchas.size() > 0) {
                if (!captchas.get(0).getCaptcha().equals(captcha)) {
                    throw new ServiceException("1", "验证码不正确");
                }
            } else {
                throw new ServiceException("1", "未找到验证码记录，请确认验证码在60秒内已发送！");
            }
        }

        //新建临时用户
        TUser user = new TUser();
        user.setMobile(mobile);
        user.setUserid(userid);
        user.setPasswd(passwd);
        user.setName(userid);
        user.setStatus("1");
        user = dao.save(user);

        //给与用户临时角色
        TUserRole ur = new TUserRole();
        ur.setRoleid("casual");
        ur.setUserid(user.getId());
        dao.save(ur);
    }

    /**
     * 找回密码
     *
     * @param mobile
     * @param passwd1
     * @param captcha
     */
    public void findPasswd(String mobile, String passwd1, String captcha) throws ServiceException {
        //查询手机号是否已完成注册
        List<ORQuery> querys = new ArrayList<ORQuery>();
        querys.add(new ORQuery(Op.eq, "mobile", mobile));
        List<TUser> users = dao.list(TUser.class, querys);
        if (users.size() == 0) {
            throw new ServiceException("1", "该手机号已经被注册");
        }
        TUser user = users.get(0);

        if (isCaptcha) {
            //校验验证码
            querys = new ArrayList<ORQuery>();
            querys.add(new ORQuery(Op.eq, "tel", mobile));
            querys.add(new ORQuery(Op.order, "createTime", "desc"));
            //有效期 5分钟
            Date date = new Date();
            date = DateUtils.addMinutes(date, -5);
            Timestamp t = new Timestamp(date.getTime());
            querys.add(new ORQuery(Op.ge, "createTime", t));
            List<TCaptcha> captchas = dao.list(TCaptcha.class, querys);
            if (captchas.size() > 0) {
                if (!captchas.get(0).getCaptcha().equals(captcha)) {
                    throw new ServiceException("1", "验证码不正确");
                }
            } else {
                throw new ServiceException("2", "未找到验证码记录，请确认验证码在60秒内已发送！");
            }
        }

        user.setPasswd(passwd1);
        dao.update(user);
    }
}
