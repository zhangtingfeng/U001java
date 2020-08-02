package com.jfc.controller.common;

import com.alibaba.fastjson.JSON;
import com.jfc.base.BaseController;
import com.jfc.util.*;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 通用业务处理
 *
 * @author mcl
 * @see
 * @since 16点36分
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/mcmn")
public class MCommonController extends BaseController {


    /**
     * 查询对象
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryInfo")
    public BaseResult queryInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            String sql = dto.getAsString("sql");
            if (!StringUtils.isNotBlank(sql)) {
                sql = "getInfo";
            }
            Dto info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + "." + sql, dto);
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
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
    public BaseResult queryListRole(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            if(null!=member){
              Dto userInfo= (Dto)bizService.queryForObject("member.getInfo",new BaseDto("id",member.getAsString("id")));
              if(null!=userInfo){
                  member.put("grade_id",userInfo.getAsString("grade_id"));
              }
            }
            List<Dto> paramList = null;
            //菜单列表加缓存
            if(StringUtils.isNotEmpty(dto.getAsString("sql")) && dto.getAsString("sql").equals("queryMenuProductInfo")){
                String queryMenuProductInfo="";
                if(null!=member){
                    if(StringUtils.isNotEmpty(member.getAsString("grade_id"))){
                        queryMenuProductInfo=redisService.getValue("queryMenuProductInfo"+"_"+member.getAsString("grade_id"));
                    }else{
                        queryMenuProductInfo=redisService.getValue("queryMenuProductInfo"+"_"+"1");
                    }
                }else{
                    queryMenuProductInfo=redisService.getValue("queryMenuProductInfo"+"_"+"1");
                }
                if(StringUtils.isNotEmpty(queryMenuProductInfo)){
                    paramList=JSONUtil.parseJSON2ListDto(queryMenuProductInfo);
                }else{
                    paramList=getParamList(dto);
                    if(null!=member){
                        redisService.setValue("queryMenuProductInfo"+"_"+member.getAsString("grade_id"), JSON.toJSONString(paramList),6048000l);
                    }else{
                        redisService.setValue("queryMenuProductInfo"+"_"+"1", JSON.toJSONString(paramList),6048000l);
                    }
                }
                if(null!=paramList && paramList.size()>0){
                    boolean flg=false;
                    for(Dto dtoParam:paramList){
                        if(dtoParam.getAsDouble("shop_stock")>0){
                            flg=true;
                        }
                    }
                    if(!flg){
                        paramList=getParamList(dto);
                        if(null!=member){
                            redisService.setValue("queryMenuProductInfo"+"_"+member.getAsString("grade_id"), JSON.toJSONString(paramList),6048000l);
                        }else{
                            redisService.setValue("queryMenuProductInfo"+"_"+"1", JSON.toJSONString(paramList),6048000l);
                        }
                    }
                }
            }else if(StringUtils.isNotEmpty(dto.getAsString("sql")) && dto.getAsString("sql").equals("getSpecsLists")){
                String getSpecsLists=redisService.getValue("getSpecsLists"+"_"+dto.getAsString("type_id"));
                if(StringUtils.isNotEmpty(dto.getAsString("product_id"))){
                    getSpecsLists=redisService.getValue("getSpecsLists"+"_"+dto.getAsString("product_id"));
                }

                if(StringUtils.isNotEmpty(getSpecsLists)){
                    paramList=JSONUtil.parseJSON2ListDto(getSpecsLists);
                }else{
                    paramList=getParamList(dto);
                    if(StringUtils.isNotEmpty(dto.getAsString("product_id"))){
                        String jsonStr = JSON.toJSONString(paramList);
                        if(dto.getAsString("product_id").equals("53")){
                            jsonStr = jsonStr.replace("有芝士","去芝士");
                        }
                        paramList=JSONUtil.parseJSON2ListDto(jsonStr);
                        redisService.setValue("getSpecsLists"+"_"+dto.getAsString("product_id"), jsonStr,6048000l);
                    }else{
                        redisService.setValue("getSpecsLists"+"_"+dto.getAsString("type_id"), JSON.toJSONString(paramList),6048000l);
                    }
                }
            }else if(StringUtils.isNotEmpty(dto.getAsString("sql")) && dto.getAsString("sql").equals("getSpecsShopPriceById")){
                String getSpecsShopPriceById="";
                if(null!=member){
                    if(StringUtils.isNotEmpty(member.getAsString("grade_id"))){
                        getSpecsShopPriceById=redisService.getValue("getSpecsShopPriceById"+"_"+dto.getAsString("product_id")+"_"+member.getAsString("grade_id"));
                    }else{
                        getSpecsShopPriceById=redisService.getValue("getSpecsShopPriceById"+"_"+dto.getAsString("product_id")+"_"+"1");
                    }
                }else{
                    getSpecsShopPriceById=redisService.getValue("getSpecsShopPriceById"+"_"+dto.getAsString("product_id")+"_"+"1");
                }
                if(StringUtils.isNotEmpty(getSpecsShopPriceById)){
                    paramList=JSONUtil.parseJSON2ListDto(getSpecsShopPriceById);
                }else{
                    paramList=getParamList(dto);
                    if(null!=member){
                        redisService.setValue("getSpecsShopPriceById"+"_"+dto.getAsString("product_id")+"_"+member.getAsString("grade_id"), JSON.toJSONString(paramList),6048000l);
                    }else{
                        redisService.setValue("getSpecsShopPriceById"+"_"+dto.getAsString("product_id")+"_"+"1", JSON.toJSONString(paramList),6048000l);
                    }
                }
//                String getSpecsLists=redisService.getValue("getSpecsShopPriceById"+"_"+dto.getAsString("product_id")+"_"+member.getAsString("grade_id"));
//                if(StringUtils.isNotEmpty(getSpecsLists)){
//                    paramList=JSONUtil.parseJSON2ListDto(getSpecsLists);
//                }else{
//                    paramList=getParamList(dto);
//                    redisService.setValue("getSpecsShopPriceById"+"_"+dto.getAsString("product_id")+"_"+member.getAsString("grade_id"), JSON.toJSONString(paramList),6048000l);
//                }
            }else {
                paramList=getParamList(dto);
            }
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    public List<Dto> getParamList(Dto dto){
        List paramList = null;
        if (StringUtils.isNotBlank(dto.getAsString("sql"))) {
            paramList = bizService.queryForList(dto.getAsString("t") + "." + dto.getAsString("sql"), dto);
        } else {
            paramList = bizService.queryForList(dto.getAsString("t") + ".queryList", dto);
        }
        return paramList;
    }

    /**
     * 删除缓存
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCacheData")
    public BaseResult deleteCacheData(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result=new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        if(StringUtils.isNotEmpty(dto.getAsString("key"))){
            redisService.deleteByPrex(dto.getAsString("key"));
            result.setData("删除成功");
        }else{
            result.setData("key不能为空");
        }
        return result;
    }

    public static void main(String[] args) {
        String sss = "[{\"id\":\"31\",\"list\":[{\"shop_id\":\"1\",\"specs_id\":\"31\",\"val_id\":\"61\",\"val_name\":\"有芝士(推荐)\"}],\"name\":\"芝士\"}]";
        sss = sss.replace("有芝士","去芝士");
        System.out.println(sss);
    }

    @ResponseBody
    @RequestMapping(value = "/test")
    public BaseResult test(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result=new BaseResult();
        List<Dto> list = bizService.queryForList("cmnNotice.queryInfoAdd", new BaseDto());
        if(null!=list){
            for(Dto  dto:list){
                bizService.saveInfo("cmnNotice.saveAddInfo",dto);
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/test2")
    public BaseResult test2(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result=new BaseResult();
        List<Dto> list = bizService.queryForList("cmnNotice.queryInfoAddInfo", new BaseDto());
        if(null!=list){
            for(Dto  dto:list){
                bizService.saveInfo("cmnNotice.saveAddInfoInfo",dto);
            }
        }
        return result;
    }
}