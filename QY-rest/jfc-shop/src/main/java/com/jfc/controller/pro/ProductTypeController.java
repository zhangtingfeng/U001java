package com.jfc.controller.pro;

import com.alibaba.fastjson.JSONArray;
import com.jfc.base.BaseController;
import com.jfc.bean.ProductType;
import com.jfc.util.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: WuMengChang
 * @Date: 2018/9/21 16:51
 * @Description: 产品分类
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/productType")
public class ProductTypeController extends BaseController {
    /**
     * 获取当前店铺所有商品分类
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryProductType")
    public BaseResult queryProductType(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //根据所属店铺查询商品类别
            if (StringUtils.isEmpty(dto.getAsString("shopid"))) {
                Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
                dto.put("shopid", member.getAsString("shopid"));
            }
            // 查询根目录下所有部门
            dto.put("parentid", dto.getAsString("id"));
            List<Dto> productTypes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
            List<Dto> list = new ArrayList<Dto>();
            productTypes.addAll( querySonProductType(list,productTypes,dto));
            result.setData(productTypes);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    public List<Dto> querySonProductType(List<Dto> list,List<Dto> productTypes,Dto dto){
        for (Dto productType : productTypes) {
            dto.put("parentid",productType.getAsString("id"));
            List<Dto> nodes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
            if (nodes.size() !=0){
                list.addAll(nodes);
                querySonProductType(list,nodes,dto);
            }
        }
        return list;
    }


    /**
     * 查询分类列表列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/proSortList")
    public BaseResult proSortList(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> paramList = bizService.queryForList("productType.queryProductTypeList", dto);
            List<Dto> leafList = new ArrayList<Dto>();
            if(paramList.size()!=0){
                leafList = bizService.queryForList("productType.queryLeafProTypeList", new BaseDto("parent_id",paramList.get(0).getAsString("id")));
            }
            Dto param = new BaseDto();
            param.put("paramList",paramList);
            param.put("leafList",leafList);
            result.setData(param);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询二级和三级分类列表列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryLeafProTypeList")
    public BaseResult queryLeafProTypeList(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> leafList = bizService.queryForList("productType.queryLeafProTypeList", new BaseDto("parent_id",dto.getAsString("id")));
            result.setData(leafList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 根据类型查询商品列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/typeProductList")
    public BaseResult typeProductList(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> leafList = bizService.queryForList("product.queryList", dto);
            result.setData(leafList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 根据类型查询商品列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/quertMainShowType")
    public BaseResult quertMainShowType(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<ProductType> leafList = bizService.queryList("productType.quertMainShowType", new BaseDto());
            result.setData(leafList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询出所有一级 二级 三级菜单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllTypeList")
    public BaseResult queryAllTypeList(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto menuList = redisService.getObject("MENU_LIST_PC", BaseDto.class);
            if(null==menuList){
                List<Dto> oneList = bizService.queryList("productType.queryMenuListOne", new BaseDto());
                List<Dto> twoList = bizService.queryList("productType.queryMenuListTwo", new BaseDto());
                Dto resultMap = new BaseDto();
                for(Dto two:twoList){
                    if(resultMap.containsKey(two.getAsString("parent_id"))){
                        resultMap.getAsList(two.getAsString("parent_id")).add(two);
                    }else{//map中不存在，新建key，用来存放数据
                        List tmpList = new ArrayList();
                        tmpList.add(two);
                        resultMap.put(two.getAsString("parent_id"), tmpList);

                    }
                }
                Dto resultMap1 = new BaseDto();
                List<Dto> threeList = bizService.queryList("productType.queryMenuListThree", new BaseDto());
                for(Dto three:threeList){
                    if(resultMap1.containsKey(three.getAsString("parent_id"))){
                        resultMap1.getAsList(three.getAsString("parent_id")).add(three);
                    }else{//map中不存在，新建key，用来存放数据
                        List tmpList = new ArrayList();
                        tmpList.add(three);
                        resultMap1.put(three.getAsString("parent_id"), tmpList);

                    }
                }
                Dto data = new BaseDto();
                data.put("oneList",oneList);
                data.put("twoList",resultMap);
                data.put("threeList",resultMap1);
                result.setData(data);
                redisService.setValue("MENU_LIST_PC", JSONArray.toJSONString(data),18000L);
                return result;
            }else{
                result.setData(menuList);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 商品列表查询二级、三级菜单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryListMenuType")
    public BaseResult queryListMenuType(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if(StringUtils.isNotEmpty(dto.getAsString("categoryId"))){
                List<ProductType> menuTwoList = bizService.queryList("productType.queryMenuListTwo", dto);
                List<ProductType> menuThreeList = bizService.queryList("productType.queryMenuListThree", dto);
                Dto data = new BaseDto();
                data.put("menuTwoList",menuTwoList);
                data.put("menuThreeList",menuThreeList);
                result.setData(data);
            }else if(StringUtils.isNotEmpty(dto.getAsString("subCategoryId"))){
                List<ProductType> menuTwoList = bizService.queryList("productType.queryTwoList", new BaseDto("type_id",dto.getAsString("subCategoryId")));
                List<ProductType> menuThreeList = bizService.queryList("productType.querySuperiorList", new BaseDto("type_id",dto.getAsString("subCategoryId")));
                Dto data = new BaseDto();
                data.put("menuTwoList",menuTwoList);
                data.put("menuThreeList",menuThreeList);
                result.setData(data);
            }else if(StringUtils.isNotEmpty(dto.getAsString("subParentId"))){
                List<ProductType> menuTwoList = bizService.queryList("productType.queryThreeList", new BaseDto("type_id",dto.getAsString("subParentId")));
                List<ProductType> menuThreeList = bizService.queryList("productType.getPrevaAllType", new BaseDto("type_id",dto.getAsString("subParentId")));
                Dto data = new BaseDto();
                data.put("menuTwoList",menuTwoList);
                data.put("menuThreeList",menuThreeList);
                result.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
