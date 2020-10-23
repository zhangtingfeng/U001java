package com.hst.ii.basedata.web;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.basedata.entity.TBaseClassProp;
import com.hst.ii.basedata.entity.TBaseGoodsClass;
import com.hst.ii.basedata.entity.TBaseGoodsProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basedata")
public class BasedataController {
    @Autowired
    ISQLDao sqlDao;
    @Autowired
    IORDao dao;

    @RequestMapping("/getGoodsClassTreeStyle")
    public void getGoodsClassTreeStyle(@RequestBody String data, Model m) throws Exception {
        List list = sqlDao.query("mng.goodsClassTreeStyle");
        m.addAttribute("goodsClassTreeStyle", list);
    }

    @RequestMapping("/opNode")
    public void opNode(@RequestBody String data, Model m) throws Exception {
        Map<String, String> map = JsonUtil.read(data, Map.class);
        String op = map.get("op");
        String id = map.get("id");
        String pid = map.get("pid");
        if (pid == null || "".equals(pid)) {
            pid = "#";
        }
        String name = map.get("name");
        if ("1".equals(op)) {//新增
            TBaseGoodsClass bcc = new TBaseGoodsClass();
            bcc.setPid(pid);
            bcc.setName(name);
            dao.save(bcc);
        } else if ("0".equals(op)) {//修改
            TBaseGoodsClass bbc = dao.get(TBaseGoodsClass.class, id);
            bbc.setName(name);
            dao.save(bbc);
        } else if ("-1".equals(op)) {//删除
            TBaseGoodsClass bbc = new TBaseGoodsClass();
            bbc.setId(id);
            dao.delete(bbc);
        }
    }

    @RequestMapping("/getGoodsProp")
    public void getGoodsProp(@RequestBody String data, Model m) throws Exception {
        Map<String, String> map = JsonUtil.read(data, Map.class);
        List list = sqlDao.query("mng.goodsProp", map);
        m.addAttribute("goodsProp", list);
    }

    @RequestMapping("/getGoodsClassProp")
    public void getGoodsClassProp(@RequestBody String data, Model m) throws Exception {
        Map<String, String> map = JsonUtil.read(data, Map.class);
        List list = sqlDao.query("mng.goodsClassProp", map);
        m.addAttribute("goodsClassProp", list);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping("/saveCategory")
    public void saveCategory(@RequestBody String data, Model m) throws Exception {
        Map<String, ?> map = JsonUtil.read(data, Map.class);
        String aClassId = (String) map.get("classId");
        if (aClassId == null || "".equals(aClassId)) {
            throw new Exception("aClassId不能为空");
        }
        Map deleteQuery = new HashMap();
        deleteQuery.put("classId", aClassId);
        sqlDao.delete("deleteClassProp", deleteQuery);
        Map classPropDataMap = (Map) map.get("classPropData");
        Iterator it = classPropDataMap.keySet().iterator();
        while (it.hasNext()) {
            String propId = (String) it.next();
            Map propMap = (Map) classPropDataMap.get(propId);
            boolean checked = (boolean) propMap.get("checked");
            if (checked) {
                List<String> propValuesList = (List) propMap.get("propValues");
                String strPropValues = JsonUtil.toString(propValuesList);
                String visibleLevel = (String) propMap.get("visibleLevel");
                //保存分类属性关系表
                TBaseClassProp cp = new TBaseClassProp();
                cp.setClassId(aClassId);
                cp.setPropId(propId);
                cp.setVisibleLevel(visibleLevel);
                cp.setSelectValues(strPropValues);
                dao.save(cp);
            }
        }
    }

    @RequestMapping("/isGoodsClassLeaf")
    public void isGoodsClassLeaf(@RequestBody String data, Model m) throws Exception {
        Map<String, String> map = JsonUtil.read(data, Map.class);
        String classId = map.get("classId");
        Map<String, String> query = new HashMap<>();
        query.put("pid", classId);
        List list = sqlDao.query("mng.goodsClassTreeStyle", query);
        if (list.size() == 0) {
            m.addAttribute("isGoodsClassLeaf", true);
        } else {
            m.addAttribute("isGoodsClassLeaf", false);
        }
    }

    @RequestMapping("/classInfo")
    public void classInfo(Model m) {
        List<TBaseGoodsClass> classList = dao.list(TBaseGoodsClass.class);
        m.addAttribute("classInfo", classList);
    }

    @RequestMapping("/propsInfo")
    public void propsInfo(Model m) {
        List<TBaseGoodsProp> propList = dao.list(TBaseGoodsProp.class);
        m.addAttribute("propsInfo", propList);
    }

}
