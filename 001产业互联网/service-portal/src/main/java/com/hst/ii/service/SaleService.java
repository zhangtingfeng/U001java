package com.hst.ii.service;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.Op;
import com.hst.ii.entity.*;
import com.hst.ii.po.PropQuery;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    IORDao dao;

    @Autowired
    ISQLDao sql;

    public List<ORQuery> getListSalesQuerys(Map<String, Object> datas) {
        List<ORQuery> querys = new ArrayList<>();

        // 商品分类
        if (datas.containsKey("catalog")) {
            String catalog = (String) datas.get("catalog");
            // 非全部
            if (!"0".equals(catalog)) {
                List<String> classIdList = getAllChildClass(catalog);
                querys.add(new ORQuery(Op.in, "goodsClassId", classIdList));
            }
        }
        // 属性
        if (datas.containsKey("filter")) {
            Map<String, List<String>> filters = (Map<String, List<String>>) datas.get("filter");

            if (filters.size() > 0) {
                List<PropQuery> propQueryList = new ArrayList<>();
                for (String key : filters.keySet()) {
                    PropQuery propQuery = new PropQuery();
                    propQuery.setId(key);
                    propQuery.setValueList(filters.get(key));
                    propQueryList.add(propQuery);
                }

                Map<String, Object> params = new HashMap<>();
                params.put("propList", propQueryList);
                List<String> saleIdList = (List<String>) sql.query("sales.getSaleIdsByPropsLimit", params);
                querys.add(new ORQuery(Op.in, "id", saleIdList));
            }
        }

        // 搜索框
        if (datas.containsKey("search")) {
            String search = (String) datas.get("search");
            if (StringUtils.isNotBlank(search)) {
                List<ORQuery> tmpQuerys = new ArrayList<>();
                tmpQuerys.add(new ORQuery(Op.like, "name", "%" + search + "%"));
                List<TOrgInfo> tOrgInfoList = dao.list(TOrgInfo.class, tmpQuerys);
                if (tOrgInfoList.size() > 0) {
                    List<String> orgIdList = new ArrayList<>();
                    for (TOrgInfo orgInfo : tOrgInfoList) {
                        orgIdList.add(orgInfo.getId());
                    }
                    querys.add(new ORQuery(Op.in, "orgId", orgIdList));
                } else {
                    querys.add(new ORQuery(Op.like, "title", "%" + search + "%"));
                }
            }
        }
        // 发布中商品
        querys.add(new ORQuery(Op.eq, "status", "2"));
        return querys;
    }

    public List<String> getAllChildClass(String classId) {
        List<String> childIdList = new ArrayList<>();
        childIdList.add(classId);
        List<Map<String, String>> classList = (List<Map<String, String>>) sql.query("dicts.goods-tree");

        List<String> tmpList = new ArrayList<>();
        do {
            tmpList = new ArrayList<>();

            for (Map<String, String> c : classList) {
                String id = c.get("id");
                String pid = c.get("pid");
                if (childIdList.contains(pid) && !childIdList.contains(id)) {
                    tmpList.add(id);
                }
            }
            for (String tmp : tmpList) {
                childIdList.add(tmp);
            }
        } while (tmpList.size() != 0);

        return childIdList;
    }

    public List<TSaleInfo> getSalesList(List<ORQuery> querys) throws ParseException {
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

    public List<TSalePropInfo> getSalePropInfos(List<String> saleIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", saleIdList));
        return dao.list(TSalePropInfo.class, querys);
    }

    public List<TSalePropInfo> getSaleProps(List<TSaleInfo> tSaleInfoList) {
        List<String> saleIdList = getSaleIdList(tSaleInfoList);
        return getSalePropInfos(saleIdList);
    }

    public List<TSalePropInfo> getSaleProps(String saleId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "salesId", saleId));
        return dao.list(TSalePropInfo.class, querys);
    }

    public List<TSaleImg> getSaleImgInfos(List<String> saleIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", saleIdList));
        querys.add(new ORQuery(Op.order, "salesId", "asc"));
        querys.add(new ORQuery(Op.order, "type", "asc"));
        querys.add(new ORQuery(Op.order, "idx", "asc"));
        return dao.list(TSaleImg.class, querys);
    }

    /**
     * 商品单张图片显示
     *
     * @param tSaleInfoList
     * @return
     */
    public List<TSaleImg> getSaleImgs(List<TSaleInfo> tSaleInfoList) {
        List<String> saleIdList = getSaleIdList(tSaleInfoList);
        return getSaleImgInfos(saleIdList);
    }

    public List<TSaleImg> getSaleImgs(String saleId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "salesId", saleId));
        querys.add(new ORQuery(Op.order, "salesId", "asc"));
        querys.add(new ORQuery(Op.order, "type", "asc"));
        querys.add(new ORQuery(Op.order, "idx", "asc"));
        return dao.list(TSaleImg.class, querys);
    }

    public List<TSaleImg> getSaleImgList(List<TSaleInfo> tSaleInfoList) {
        List<String> saleIdList = getSaleIdList(tSaleInfoList);
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", saleIdList));
        querys.add(new ORQuery(Op.eq, "type", "1"));
        querys.add(new ORQuery(Op.eq, "idx", "1"));
        querys.add(new ORQuery(Op.order, "salesId", "asc"));

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

    public TOrgInfo getOrg(String orgId) {
        return dao.get(TOrgInfo.class, orgId);
    }

    public TOrgWarehouse getWh(String whId) {
        return dao.get(TOrgWarehouse.class, whId);
    }
}
