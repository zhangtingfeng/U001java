package com.hst.ii.sales.service;

import com.hst.core.ServiceContext;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.Op;
import com.hst.core.meta.MetaRepository;
import com.hst.ii.org.entity.TOrgInfo;
import com.hst.ii.org.entity.TOrgWarehouse;
import com.hst.ii.sales.entity.TSaleApproval;
import com.hst.ii.sales.entity.TSaleImg;
import com.hst.ii.sales.entity.TSaleInfo;
import com.hst.ii.sales.entity.TSalePropInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/13 15:13
 */
@Transactional(rollbackOn = Throwable.class)
@Service
public class SaleService {
    public static String NAME = "listing-sale";

    @Autowired
    MetaRepository repo;

    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    /**
     * 根据查询条件获取商品列表
     *
     * @param datas
     * @return
     * @throws ParseException
     */
    public List<TSaleInfo> getSalesList(Map<String, Object> datas) throws ParseException {
        List<ORQuery> querys = new ArrayList<>();

        String name = (String) datas.get("name");
        if (StringUtils.isNotBlank(name)) {
            querys.add(new ORQuery(Op.like, "goodsName", "%" + name + "%"));
        }
        String code = (String) datas.get("code");
        if (StringUtils.isNotBlank(code)) {
            querys.add(new ORQuery(Op.like, "code", "%" + code + "%"));
        }
        String title = (String) datas.get("title");
        if (StringUtils.isNotBlank(title)) {
            querys.add(new ORQuery(Op.like, "title", "%" + title + "%"));
        }
        String startStr = (String) datas.get("startDt");
        if (StringUtils.isNotBlank(startStr)) {
            Date startDt = DateUtils.parseDate(startStr, "yyyy-MM-dd");
            querys.add(new ORQuery(Op.ge, "createTime", startDt));
        }
        String endStr = (String) datas.get("endDt");
        if (StringUtils.isNotBlank(endStr)) {
            Date endDt = DateUtils.parseDate(endStr, "yyyy-MM-dd");
            querys.add(new ORQuery(Op.le, "createTime", endDt));
        }

        return dao.list(TSaleInfo.class, querys);
    }

    /**
     * 获取商品ID列表
     *
     * @param tSaleInfoList
     * @return
     */
    public List<String> getSaleIdList(List<TSaleInfo> tSaleInfoList) {
        List<String> saleIdList = new ArrayList<>();
        for (TSaleInfo saleInfo : tSaleInfoList) {
            saleIdList.add(saleInfo.getId());
        }
        return saleIdList;
    }

    /**
     * 获取组织ID列表
     *
     * @param tSaleInfoList
     * @return
     */
    public List<String> getOrgIdList(List<TSaleInfo> tSaleInfoList) {
        List<String> orgIdList = new ArrayList<>();
        for (TSaleInfo saleInfo : tSaleInfoList) {
            orgIdList.add(saleInfo.getOrgId());
        }
        return orgIdList;
    }

    /**
     * 获取仓库ID列表
     *
     * @param tSaleInfoList
     * @return
     */
    public List<String> getWhIdList(List<TSaleInfo> tSaleInfoList) {
        List<String> whIdList = new ArrayList<>();
        for (TSaleInfo saleInfo : tSaleInfoList) {
            whIdList.add(saleInfo.getWhId());
        }
        return whIdList;
    }

    public List<TSalePropInfo> getSaleProps(List<TSaleInfo> tSaleInfoList) {
        List<String> saleIdList = getSaleIdList(tSaleInfoList);
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", saleIdList));
        return dao.list(TSalePropInfo.class, querys);
    }

    public List<TSaleImg> getSaleImgs(List<TSaleInfo> tSaleInfoList) {
        List<String> saleIdList = getSaleIdList(tSaleInfoList);
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", saleIdList));
        querys.add(new ORQuery(Op.order, "salesId", "asc"));
        querys.add(new ORQuery(Op.order, "type", "asc"));
        querys.add(new ORQuery(Op.order, "idx", "asc"));
        return dao.list(TSaleImg.class, querys);
    }

    public List<TOrgInfo> getOrgs(List<TSaleInfo> tSaleInfoList) {
        List<String> orgIdList = getOrgIdList(tSaleInfoList);
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "id", orgIdList));
        return dao.list(TOrgInfo.class, querys);
    }

    public List<TOrgWarehouse> getWhs(List<TSaleInfo> tSaleInfoList) {
        List<String> whIdList = getWhIdList(tSaleInfoList);
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "id", whIdList));
        return dao.list(TOrgWarehouse.class, querys);
    }

    /**
     * 商品审批结果
     *
     * @param salesId 挂牌销售ID
     * @param result  ： 1（通过） / 2（有问题）
     * @param remark  备注信息
     * @throws Exception
     */
    public void approve(String salesId, String result, String remark) throws Exception {
        User u = ServiceContext.getInstance().getUser();

        TSaleApproval tSaleApproval = new TSaleApproval();
        tSaleApproval.setSalesId(salesId);
        tSaleApproval.setTime(new Date());
        tSaleApproval.setOperator(u.getUid());
        tSaleApproval.setResult(result);
        tSaleApproval.setOpinions(remark);
        dao.save(tSaleApproval);

        TSaleInfo tSaleInfo = dao.get(TSaleInfo.class, salesId);
        if (null == tSaleInfo) return;
        String status = "1".equals(result) ? "2" : "3";
        tSaleInfo.setStatus(status);
        dao.update(tSaleInfo);
    }

    /**
     * 下架
     *
     * @param salesId
     */
    public void offShelf(String salesId) {
        TSaleInfo tSaleInfo = dao.get(TSaleInfo.class, salesId);
        if (null == tSaleInfo)
            return;

        tSaleInfo.setStatus("9");
        dao.update(tSaleInfo);
    }

}
