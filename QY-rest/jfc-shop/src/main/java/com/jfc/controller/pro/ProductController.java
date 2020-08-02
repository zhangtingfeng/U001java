package com.jfc.controller.pro;


import com.jfc.base.BaseController;
import com.jfc.service.ProductService;
import com.jfc.util.*;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 商品
 * Created by wj on 2018/8/21.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/pro")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productServices;

    /**
     * 获取所有商品树状数据结构
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllProductType")
    public BaseResult queryAllProductType(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //根据所属店铺查询商品类别
            if (StringUtils.isEmpty(dto.getAsString("shop_id"))) {
                Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
                dto.put("shopid", member.getAsString("shop_id"));
            }
            // 查询根目录下所有部门
            dto.put("parent_id", 0);
            List<Dto> productTypes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
            for (Dto productType : productTypes) {
                dto.put("parent_id", productType.get("id"));
                List<Dto> nodes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
                for (Dto node : nodes) {
                    dto.put("parent_id", node.get("id"));
                    List<Dto> dnodes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
                    node.put("nodes", dnodes);
                }
                productType.put("nodes", nodes);
            }
            result.setData(productTypes);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editProductTypeInfo")
    public BaseResult editProductTypeInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        int save=0;
        try {
            inDto.put("parent_id", inDto.getAsString("parent_id").split("-")[0]);
            inDto.put("type_id", inDto.getAsString("type_id").split("-")[0]);
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (inDto.getAsLong("id") != null) {//修改
                save=inDto.getAsInteger("id");
                inDto.put("updator", member == null ? "" : member.get("id"));
                inDto.put("method","updateInfo");
                bizService.update(inDto);
                if(StringUtils.isNotEmpty(inDto.getAsString("specsValues"))){
                    List<Map<String, Object>> specsValues = JSONUtil.parseJSON2List(inDto.getAsString("specsValues"));
                    for(Map specsValue:specsValues){
                        specsValue.put("product_id",inDto.get("id"));
                        specsValue.put("updator", member == null ? "" : member.get("id"));
                        specsValue.put("creator", member == null ? "" : member.get("id"));
                        List<Dto> list = bizService.queryList("productSpecs.getSpecsShopPriceById", specsValue);
                        if(null==list || list.size()<=0){
                            bizService.saveInfo("productSpecs.saveSpecsShopPrice",specsValue);
                        }else{
                            specsValue.put("id",list.get(0).get("id"));
                            bizService.updateInfo("productSpecs.updateSpecsShopPrice",specsValue);
                        }
                    }
                }
            } else {//新增
                inDto.put("creator", member == null ? "" : member.get("id"));
                inDto.put("method","saveInfo");
                save = bizService.save(inDto);
                if(StringUtils.isNotEmpty(inDto.getAsString("specsValues"))){
                    if(save>0){
                        List<Map<String, Object>> specsValues = JSONUtil.parseJSON2List(inDto.getAsString("specsValues"));
                        for(Map specsValue:specsValues){
                            specsValue.put("product_id",inDto.get("id"));
                            specsValue.put("updator", member == null ? "" : member.get("id"));
                            specsValue.put("creator", member == null ? "" : member.get("id"));
                            bizService.saveInfo("productSpecs.saveSpecsShopPrice",specsValue);
                        }
                    }
                }
            }
            result.setData(save);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveBath")
    public BaseResult saveBath(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //查询商铺
            if (inDto.getAsInteger("shop_id") == 1) {
                return null;
            }
            List<Dto> list = bizService.queryForList(inDto.getAsString("t") + ".queryList", new BaseDto("productId", 1));
            List<Dto> reData=null;
            List<Dto> reDataTwo=null;
            List<Dto> reDataThree=null;
            if(null!=list && list.size()>0){
                reData=new ArrayList<>();
                for(Dto dtoRes:list){
                    if(dtoRes.getAsString("parent_id").equals("0")){
                        reData.add(dtoRes);
                    }
                }
                for(Dto dtoResTwo:reData){
                    reDataTwo=new ArrayList<>();
                    for(Dto dtoRes:list){
                        if(dtoRes.getAsString("parent_id").equals(dtoResTwo.getAsString("id"))){
                            reDataTwo.add(dtoRes);
                        }
                    }
                    dtoResTwo.put("menu2",reDataTwo);
                }
                for(Dto dtoResThree:reDataTwo){
                    reDataThree=new ArrayList<>();
                    for(Dto dtoRes:list){
                        if(dtoRes.getAsString("parent_id").equals(dtoResThree.getAsString("id"))){
                            reDataThree.add(dtoRes);
                        }
                    }
                    dtoResThree.put("menu3",reDataThree);
                }
            }
            //添加
            if(null!=reData && reData.size()>0){
                for(Dto dto1:reData){
                    //bizService.save(inDto.getAsString("t") + ".saveBatch2",dto1);
                    dto1.put("shop_id", inDto.getAsInteger("shop_id"));
                    bizService.saveInfo(inDto.getAsString("t") + ".saveBatch2", dto1);
                    if(StringUtils.isNotEmpty(dto1.getAsString("id"))){
                        for(Dto dto2:(List<Dto>)dto1.getAsList("menu2")){
                            dto2.put("shop_id", inDto.getAsInteger("shop_id"));
                            dto2.put("parent_id",dto1.getAsString("id"));
                            bizService.saveInfo(inDto.getAsString("t") + ".saveBatch2", dto2);
                            if(StringUtils.isNotEmpty(dto2.getAsString("id"))){
                                for(Dto dto3:(List<Dto>)dto2.getAsList("menu3")){
                                    dto3.put("shop_id", inDto.getAsInteger("shop_id"));
                                    dto3.put("parent_id", dto2.getAsString("id"));
                                    bizService.saveInfo(inDto.getAsString("t") + ".saveBatch2", dto3);
                                }
                            }
                        }
                    }
                }
            }
            /*list.forEach(shopList -> {
                shopList.put("shop_id", inDto.getAsString("shop_id"));
            });
            bizService.insert(inDto.getAsString("t") + ".saveBatch", list);*/
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * * 删除信息
     */
    @ResponseBody
    @RequestMapping(value = "/deleteProductTypeInfo")
    public BaseResult deleteProductTypeInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
             Integer flag = productServices.deleteInfo(inDto);
           if (flag == -1) {
                return returnMsg("9999", "已有商品关联该类别，删除失败！");
            } else if (flag == -2) {
                return returnMsg("9999", "已有商品关联该属性，删除失败！");
            } else if (flag == -3) {
                return returnMsg("9999", "已有商品关联该品牌，删除失败！");
            }
            result.setData(new BaseDto("msg", "数据操作成功"));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryList")
    public BaseResult queryList(HttpServletRequest request, HttpServletResponse response) {
        String t = request.getParameter("t");
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            //根据所属店铺查询商品类别
            if (StringUtils.isEmpty(dto.getAsString("shopid")) && !"1".equals(member.getAsString("id"))) {
                dto.put("shopid", member.getAsString("shopid"));
            }
            dto.put("userid", member == null ? "" : member.get("id"));
            List paramList = bizService.queryForList(dto.getAsString("t") + ".queryList", dto);
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询商品属性
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAttributesList")
    public BaseResult queryAttributesList(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Map<String, List<Dto>> returnMap = new HashMap<>();
            List<Dto> paramList = bizService.queryForList("productPropertyShop.queryList", dto);
            paramList.forEach(proty -> {
                if (returnMap.get(proty.getAsString("propertName")) == null) {
                    List<Dto> addDto = new ArrayList<Dto>();
                    addDto.add(proty);
                    returnMap.put(proty.getAsString("propertName"), addDto);
                } else {
                    returnMap.get(proty.getAsString("propertName")).add(proty);
                    returnMap.put(proty.getAsString("propertName"), returnMap.get(proty.getAsString("propertName")));
                }
            });
            result.setData(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/queryProInfo")
    public BaseResult queryProInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
//            Dto info = (BaseDto) bizService.queryForDto("product.getInfo", dto);
            Dto info = new BaseDto();
            if(StringUtils.isNotEmpty(dto.getAsString("type"))){
                if("seckill".equals(dto.getAsString("type"))){
                    info = (BaseDto) bizService.queryForDto("product.getMarketSeckillInfo", dto);
                }else if("special".equals(dto.getAsString("type"))){
                    info = (BaseDto) bizService.queryForDto("product.getMarketSpeciallInfo", dto);
                }else if("group".equals(dto.getAsString("type"))){
                    info = (BaseDto) bizService.queryForDto("product.getMarketGroupInfo", dto);
                }

            }else{
                info = (BaseDto) bizService.queryForDto("product.getInfo", dto);
            }
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/queryProPerIdInfo")
    public BaseResult queryProPerIdInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> list = bizService.queryForList("productPropertyShop.queryList", dto);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 根据类型查询二级下的商品
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryListProductType")
    public BaseResult queryListProductType(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List paramList = bizService.queryForList("product.queryListProductType", dto);
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询对象
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getProductInfo")
    public BaseResult getProductInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto info = new BaseDto();

            if(StringUtils.isNotEmpty(dto.getAsString("type"))){
                if("seckill".equals(dto.getAsString(""))){
                    info = (BaseDto) bizService.queryForDto("product.getMarketSeckillInfo", dto);
                }else if("special".equals(dto.getAsString("type"))){
                    info = (BaseDto) bizService.queryForDto("product.getMarketSpeciallInfo", dto);
                }else if("group".equals(dto.getAsString("type"))){
                    info = (BaseDto) bizService.queryForDto("product.getMarketGroupInfo", dto);
                }

            }else{
                info = (BaseDto) bizService.queryForDto("product.getInfo", dto);
            }
            List<Dto> list = bizService.queryList("product.getProductResult", dto);
            Map map=new HashMap<>();
            if(null!=list && list.size()>0){
                for(int i=0;i<list.size();i++){
                    Dto dto1 = list.get(i);
                    if(map.containsKey(dto1.getAsString("name"))){
                        List returnList = (List)map.get(dto1.getAsString("name"));
                        Map mapnews=new HashMap<>();
                        mapnews.put("proid",dto1.getAsString("proId"));
                        mapnews.put("proval",dto1.getAsString("proVal"));
                        returnList.add(mapnews);
                        map.put(dto1.getAsString("name"),returnList);
                    }else{
                        List newList=new ArrayList<>();
                        Map mapnews=new HashMap<>();
                        mapnews.put("proid",dto1.getAsString("proId"));
                        mapnews.put("proval",dto1.getAsString("proVal"));
                        newList.add(mapnews);
                        map.put(dto1.getAsString("name"),newList);
                    }
                }
                info.put("propertys",map);
            }
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询积分商品列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/integralProduct")
    public BaseResult integralProduct(HttpServletRequest request,HttpServletResponse response){
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try{
            List<Dto> proList = bizService.queryList("productIntegral.queryList",dto);
            result.setData(proList);
        }catch (Exception e){
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }

       return result;
    }
    /**
     *
     * 查询商品和属性值
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getProductListAndAttrbutes")
    public BaseResult getProductListAndAttrbutes(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto info = (BaseDto) bizService.queryForDto("product.getInfo", dto);
            List<Dto> list = bizService.queryList("product.getProductResult", dto);
            Map map=new HashMap<>();
            if(null!=list && list.size()>0){
                for(int i=0;i<list.size();i++){
                    Dto dto1 = list.get(i);
                    if(map.containsKey(dto1.getAsString("name"))){
                        List returnList = (List)map.get(dto1.getAsString("name"));
                        Map mapnews=new HashMap<>();
                        mapnews.put("proid",dto1.getAsString("proid"));
                        mapnews.put("proval",dto1.getAsString("proval"));
                        returnList.add(mapnews);
                        map.put(dto1.getAsString("name"),returnList);
                    }else{
                        List newList=new ArrayList<>();
                        Map mapnews=new HashMap<>();
                        mapnews.put("proid",dto1.getAsString("proid"));
                        mapnews.put("proval",dto1.getAsString("proval"));
                        newList.add(mapnews);
                        map.put(dto1.getAsString("name"),newList);
                    }
                }
                info.put("propertys",map);
            }
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
    /**
     *
     * 查询商品和属性值
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/editProStatus")
    public BaseResult editProStatus(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if(!StringUtils.isNotEmpty(inDto.getAsString("loginCode"))){
                if(null == member){
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }
            if (inDto.getAsLong("id") != null) {// 修改
                    bizService.updateInfo("product.editProStatus",inDto);
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    /**
     *
     * 上下架修改
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editStatusForproduct")
    public BaseResult editStatusForproduct(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if(!StringUtils.isNotEmpty(inDto.getAsString("loginCode"))){
                if(null == member){
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }
            Dto pdto=new BaseDto();
            if (inDto.getAsLong("id") != null) {// 修改
                String [] ids=inDto.getAsString("id").split(",");
                for (int i=0;i<ids.length;i++){
                    pdto.put("status",inDto.getAsString("status"));
                    pdto.put("shop_id",inDto.get("shop_id"));
                    pdto.put("id",ids[i]);
                    if(inDto.getAsString("status").equals("1")){
                        pdto.put("stock",0);
                    }else{
                        pdto.put("stock",5000);
                    }
                    bizService.updateInfo("product.editStatusForproduct",pdto);
                    pdto.put("pro_id",ids[i]);
                    // 更新产品库存
                    bizService.updateInfo("product.editStockPro",pdto);
                    // 更新预售库存
                    bizService.updateInfo("product.editStockPre",pdto);
                    // 更新买赠库存
                    bizService.updateInfo("product.editStockBuysend",pdto);
                    // 清理缓存
                    redisService.deleteByPrex("queryMenuProductInfo");
                    redisService.deleteByPrex("getSpecsLists");
                    redisService.deleteByPrex("getSpecsShopPriceById");
                }
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     *
     * 上下架修改
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bornNumber")
    public BaseResult bornNumber(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if(!StringUtils.isNotEmpty(inDto.getAsString("loginCode"))){
                if(null == member){
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }
            String number ="N" + DateUtil.getStringFromDate(new Date(), "yyyyMMddHHss");
            inDto.put("number",number);
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



}