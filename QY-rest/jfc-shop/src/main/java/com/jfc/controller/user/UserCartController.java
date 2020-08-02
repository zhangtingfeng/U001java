package com.jfc.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.jfc.base.BaseController;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息相关controller
 *
 * @author liujie
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/cart")
public class UserCartController extends BaseController {

    /**
     * queryUserAddress
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryUserCart")
    public BaseResult queryProvinCity(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(member == null){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            dto.put("member_id",member.getAsInteger("id"));
            List<Dto> cartList = bizService.queryForList("memberCart.queryList",dto);
            //查询属性
            cartList.forEach(cart->{
                List<String> attrList = new ArrayList<>();
                List<String> attrIds = new ArrayList<>();
                List<Dto> propertys = bizService.queryForList("memberCart.queryProAttrList",new BaseDto("attrIds",cart.getAsString("property_shop_id")));
                Dto parm = new BaseDto("product_id",cart.getAsString("product_id"));
                List<Dto> dataPrice = bizService.queryForList("productSpecs.getSpecsShopPriceById",parm);
                for (Dto property:propertys) {
                    attrList.add(property.getAsString("name"));
                    attrIds.add(property.getAsString("id"));
                }
                String arrtStr = String.join(",", attrList);
                cart.put("attrStr",arrtStr);
                cart.put("attrIds",String.join(",", attrIds));
                cart.put("dataPrice",dataPrice);
            });
            result.setData(cartList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 删除购物车
     * */
    @ResponseBody
    @RequestMapping(value = "/deleteCartPro")
    public BaseResult deleteCartPro(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String ids = dto.getAsString("ids");
            if (StringUtils.isEmpty(ids)){
                throw new Exception("ids不能为空");
            }
            String[] idsS = ids.split(",");
            for (String id : idsS){
                bizService.delete("memberCart.deleteInfo",new BaseDto("id",id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 添加购物车
     * */
    @ResponseBody
    @RequestMapping(value = "/saveCartPro")
    public BaseResult saveCartPro(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(member == null){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            Dto userCart = new BaseDto();
            userCart.put("member_id",member.getAsString("id"));
            userCart.put("product_id",dto.getAsString("id"));
            userCart.put("property_shop_id",dto.getAsString("proTypeId"));
            Dto cart = (BaseDto)bizService.queryForDto("memberCart.getInfo",userCart);
            if(null==cart){
                userCart.put("product_num",dto.getAsString("number"));
                userCart.put("product_money",dto.getAsString("product_money"));
                bizService.saveInfo("memberCart.saveInfo",userCart);
            }else{
                Dto cartDto = new BaseDto();
                cartDto.put("product_num",dto.getAsInteger("number")+cart.getAsInteger("product_num"));
                cartDto.put("product_money",dto.getAsDouble("product_money")+cart.getAsDouble("product_money"));
                cartDto.put("id",cart.getAsString("id"));
                cartDto.put("tableName","memberCart");
                cartDto.put("method","updateInfo");
                bizService.update(cartDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 同步购物车
     * */
    @ResponseBody
    @RequestMapping(value = "/syncCart")
    public BaseResult syncCart(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(member == null){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            String[] carts = dto.getAsString("cart").split("-");
            for (String cart : carts){
                Dto cartDto = (Dto) JSONArray.parseObject(cart,BaseDto.class);
                Dto pro = (Dto) bizService.queryForDto("memberCart.queryProInfo", new BaseDto("id", cartDto.getAsInteger("id")));
                Dto cartPro = new BaseDto();
                cartPro.put("member_id",member.getAsInteger("id"));
                cartPro.put("product_id",cartDto.getAsInteger("id"));
                cartPro.put("property_shop_id",cartDto.getAsString("product_property_id"));
                cartPro = (Dto) bizService.queryForObject("memberCart.getInfo",cartPro);
                BaseDto baseDto = new BaseDto();
                baseDto.put("member_id",member.getAsInteger("id"));
                baseDto.put("product_id",cartDto.getAsInteger("id"));
                baseDto.put("property_shop_id",cartDto.getAsString("product_property_id"));
                baseDto.put("updator",member.getAsInteger("id"));
                if (cartPro != null && StringUtils.isNotEmpty(cartPro.getAsString("id"))){
                        //修改
                    baseDto.put("product_num",cartDto.getAsInteger("number") + cartPro.getAsInteger("product_num"));
                    baseDto.put("product_money",(pro.getAsBigDecimal("price").multiply(new BigDecimal(cartDto.getAsInteger("number")))).add(cartPro.getAsBigDecimal("product_money")));
                    baseDto.put("id",cartPro.getAsString("id"));
                    bizService.updateInfo("memberCart.updateInfo",baseDto);
                }else{
                    baseDto.put("product_num",cartDto.getAsInteger("number"));
                    baseDto.put("product_money",pro.getAsBigDecimal("price").multiply(new BigDecimal(cartDto.getAsInteger("number"))));
                    baseDto.put("creator",member.getAsInteger("id"));
                    bizService.saveInfo("memberCart.saveInfo",baseDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 修改购物车数据
     * */
    @ResponseBody
    @RequestMapping(value = "/editProCartNum")
    public BaseResult editProCartNum(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(member == null){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            bizService.updateInfo("memberCart.updateInfo",dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 获取购物车的数量
     * */
    @ResponseBody
    @RequestMapping(value = "/userCartNum")
    public BaseResult userCartNum(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(member == null){
                result.setData(new BaseDto("num","0"));
                return result;
            }
            Dto userCart = (Dto) bizService.queryForDto("memberCart.queryUserCartNum", new BaseDto("member_id", member.getAsInteger("id")));
            result.setData(userCart);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 购物车加减
     * */
    @ResponseBody
    @RequestMapping(value = "/cartAddSub")
    public BaseResult cartAddSub(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(member == null){
                result.setData(new BaseDto("num","0"));
                return result;
            }
            Dto userCart = (Dto) bizService.queryForDto("memberCart.queryUserCartNum", new BaseDto("member_id", member.getAsInteger("id")));
            result.setData(userCart);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

}
