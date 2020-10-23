package com.hst.ii.sales.service;

import com.hst.core.MapBean;
import com.hst.core.ServiceContext;
import com.hst.core.auth.User;
import com.hst.core.dao.IDService;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.Op;
import com.hst.core.meta.MetaField;
import com.hst.core.meta.MetaInfo;
import com.hst.core.meta.MetaRepository;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.sales.entity.TSaleImg;
import com.hst.ii.sales.entity.TSaleInfo;
import com.hst.ii.sales.entity.TSalePropInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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

    @Autowired
    IDService idService;

    List<MetaField> lists = new ArrayList<>();
    List<MetaField> querys = new ArrayList<>();
    Map<String, MetaField> fields = new HashMap<>();

    /**
     * 根据商品分类获取对应属性信息
     *
     * @param goods
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public MetaInfo getGoodsMetaInfo(String goods) throws IllegalAccessException, InvocationTargetException {
        // 创建新对象使用
        MetaInfo info = repo.getMetaInfo(NAME);
        List<MetaField> newFields = new ArrayList<>();
//        for(MetaField m:info.getFields()){
//            newFields.add(m);
//        }
        MetaInfo newInfo = new MetaInfo(info.getName(), info.getTitle(), new String[]{}, newFields);
        newInfo.setAuth(info.getAuth());
        Map<String, MetaInfo> newChidren = new HashMap<String, MetaInfo>();
//        Iterator<Map.Entry<String,MetaInfo>> iterator=info.getChildren().entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String,MetaInfo> entry= iterator.next();
//            newChidren.put(entry.getKey(),entry.getValue());
//        }
        newInfo.setChildren(newChidren);

        List<MetaField> mfs = new ArrayList<>();
        List<MetaField> lists = new ArrayList<>();
        List<MetaField> querys = new ArrayList<>();

        List<String> groupList = (List<String>) sql.query("sale.getGroupTitles");

        MapBean<String, Object> params = new MapBean<>();
        params.put("id_class", goods);
        List<Map<String, Object>> results = (List<Map<String, Object>>) sql.query("sale.getPagePropConfigs", params);
        for (Map<String, Object> r : results) {
            MetaField f = new MetaField((String) r.get("id"), (String) r.get("name"));
            f.setIdx((int) r.get("idx"));
            f.setView((String) r.get("view"));
            f.setViewArgs((String) r.get("view_args"));
            f.setEditor((String) r.get("editor"));
            f.setEditorArgs((String) r.get("value"));
            f.setForm((int) r.get("form"));
            // group 处理逻辑
            String group = (String) r.get("prop_group");
            for (String g : groupList) {
                if (g.equals(group)) {
                    f.setGroup(group);
                    groupList.remove(g);
                    break;
                }
            }
            f.setValidate((String) r.get("required"));

            f.setWidth((float) r.get("width"));
            f.setSort((String) r.get("sort"));
            mfs.add(f);

            if (f.isList())
                lists.add(f);
            if (f.getQuery() != FieldQuery.None)
                querys.add(f);
        }

//        List<ORQuery> orquerys=new ArrayList<>();
//        orquerys.add(new ORQuery(Op.eq, "goodsClassId", goods));
//        orquerys.add(new ORQuery(Op.order, "idx", "asc"));
//        for (TListingSaleFieldConfig config : dao.list(TListingSaleFieldConfig.class,orquerys)) {
//            MetaField f = new MetaField(config.getId(), config.getTitle());
//            f.setIdx(config.getIdx());
//            f.setView(config.getView());
//            f.setViewArgs(config.getViewArgs());
//            f.setEditor(config.getEditor());
//            f.setEditorArgs(config.getEditorArgs());
//            f.setQuery(FieldQuery.valueOf(config.getQuery()));
//            f.setList(config.getList() != 0);
//            f.setForm(config.getForm());
//            f.setGroup(config.getGroupTitle());
//            f.setWidth(config.getWidth());
//            f.setValidate(config.getValidate());
//            f.setSort(config.getSort());
//            mfs.add(f);
//
//            if (f.isList())
//                lists.add(f);
//            if (f.getQuery() != FieldQuery.None)
//                querys.add(f);
//        }

        List<MetaField> mfs1 = newInfo.getFields();
        mfs1.addAll(mfs);
        this.lists = lists;
        this.querys = querys;
        mfs.forEach(mf -> {
            this.fields.put(mf.getName(), mf);
        });

        return newInfo;
    }

    /**
     * 保存发布信息
     */
    public void saveReleaseGoods(Map<String, Object> reqData) {
        // 前端-交易信息
        Map<String, String> dealMap = (Map<String, String>) reqData.get("meta");
        TSaleInfo tSaleInfo = new TSaleInfo();
        // 前端-商品分类
        String goodsId = (String) reqData.get("goodsId");
        tSaleInfo.setGoodsClassId(goodsId);
        tSaleInfo.setGoodsName(getClassName(goodsId));
        // 前端交易信息
        tSaleInfo.setTitle((String) dealMap.get("title"));
        tSaleInfo.setUnitPrice(BigDecimal.valueOf(Double.valueOf(dealMap.get("unitPrice"))));
        tSaleInfo.setMinQty(Integer.valueOf(dealMap.get("minQty")));
        tSaleInfo.setTotalQty(Integer.valueOf(dealMap.get("totalQty")));
        tSaleInfo.setWhId(dealMap.get("whId"));
        tSaleInfo.setUnit1(dealMap.get("unit1"));
        tSaleInfo.setUnit2(dealMap.get("unit2"));
        // 默认信息
        tSaleInfo.setOrgId(ServiceContext.getInstance().getUser().getOrgcode());
        tSaleInfo.setCreateUser(ServiceContext.getInstance().getUser().getUid());
        tSaleInfo.setCreateTime(new Date());
        tSaleInfo.setStatus("1");
        long value = idService.getCode("listingSale", true);
        String code = idService.formatCode(18, value, true, "GDXS");
        tSaleInfo.setCode(code);
        dao.save(tSaleInfo);

        // 前端-轮播图片
        List<String> images1 = (List<String>) reqData.get("images1");
        int i = 1;
        for (String img : images1) {
            TSaleImg saleImg = new TSaleImg();
            saleImg.setSalesId(tSaleInfo.getId());
            saleImg.setImgPath(img);
            saleImg.setType("1");
            saleImg.setIdx(i);
            dao.save(saleImg);
            i++;
        }
        // 前端-下方图片
        List<String> images2 = (List<String>) reqData.get("images2");
        i = 1;
        for (String img : images2) {
            TSaleImg saleImg = new TSaleImg();
            saleImg.setSalesId(tSaleInfo.getId());
            saleImg.setImgPath(img);
            saleImg.setType("2");
            saleImg.setIdx(i);
            dao.save(saleImg);
            i++;
        }

        List<Map<String, String>> propList = (List<Map<String, String>>) sql.query("sale.getPropsName");
        // 前端-属性配置
        Map<String, String> propMap = (Map<String, String>) reqData.get("meta2");
        Iterator<Map.Entry<String, String>> iterator = propMap.entrySet().iterator();
        while (iterator.hasNext()) {
            TSalePropInfo tPropInfo = new TSalePropInfo();
            Map.Entry<String, String> entry = iterator.next();
            tPropInfo.setSalesId(tSaleInfo.getId());
            tPropInfo.setPropId(entry.getKey());
            tPropInfo.setPropName(getPropName(propList, entry.getKey()));// 保存属性名称
            tPropInfo.setPropValue(entry.getValue());
            dao.save(tPropInfo);
        }
    }

    public String getPropName(List<Map<String, String>> propList, String propId) {
        String propName = "";
        for (Map<String, String> prop : propList) {
            if (propId.equals(prop.get("id"))) {
                propName = prop.get("name");
                break;
            }
        }
        return propName;
    }

    public List<TSaleInfo> getSalesList(Map<String, Object> datas) throws ParseException {
        List<ORQuery> querys = new ArrayList<>();

        User user = ServiceContext.getInstance().getUser();
        querys.add(new ORQuery(Op.eq, "createUser", user.getUid()));

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

    public String getClassName(String classId) {
        MapBean<String, Object> args = new MapBean<String, Object>();
        args.put("id_class", classId);
        List<String> classList = (List<String>) sql.query("sale.getClassName", args);
        String className = "";
        if (classList.size() > 0) {
            className = classList.get(0);
        }
        return className;
    }

    public Map<String, String> getClassName(List<TSaleInfo> tSaleInfoList) {
        Map<String, String> dataMap = new HashMap<>();
        for (TSaleInfo t : tSaleInfoList) {

            dataMap.put(t.getId(), getClassName(t.getGoodsClassId()));
        }
        return dataMap;
    }

    /**
     * 删除发布商品
     *
     * @param saleId
     */
    public void delete(String saleId) {

        TSaleInfo saleInfo = dao.get(TSaleInfo.class, saleId);
        dao.delete(saleInfo);

        MapBean<String, Object> params = new MapBean<>();
        params.put("id_sales", saleId);
        sql.delete("sale.deleteSaleImgs", params);
        sql.delete("sale.deleteSaleProps", params);
    }

    /**
     * 下架发布商品
     *
     * @param saleId
     */
    public void remove(String saleId) {

        TSaleInfo saleInfo = dao.get(TSaleInfo.class, saleId);
        saleInfo.setStatus("9");
        dao.update(saleInfo);
    }

    /**
     * 重新发布商品
     *
     * @param saleId
     */
    public void releaseAgain(String saleId) {

        TSaleInfo saleInfo = dao.get(TSaleInfo.class, saleId);
        saleInfo.setStatus("1");
        dao.update(saleInfo);
    }

    /**
     * 商品详情
     */

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

    public List<TSalePropInfo> getSalePropInfos(List<String> saleIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", saleIdList));
        return dao.list(TSalePropInfo.class, querys);
    }

    public List<TSalePropInfo> getSaleProps(List<TSaleInfo> tSaleInfoList) {
        List<String> saleIdList = getSaleIdList(tSaleInfoList);
        return getSalePropInfos(saleIdList);
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


}
