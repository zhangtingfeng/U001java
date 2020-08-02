package com.jfc.controller.admin;

import com.jfc.base.BaseController;
import com.jfc.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MJ on 2018-08-21.
 * 活动管理配置
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/market")
public class MarketConteoller extends BaseController {

    /**
     * 查询优惠券配置表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryCoupons")
    public BaseResult queryCoupons(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getAsString("shopid")) && !"1".equals(member.getAsString("id"))) {
                dto.put("shopid", member.getAsString("shopid"));
            }
            List<Dto> list = bizService.queryForList("marketCoupon.queryList", dto);
            for (Dto coupons : list) {
                if ("0".equals(coupons.getAsString("the_products"))) {
                    coupons.put("theProducts", "平台通用");
                } else if ("1".equals(coupons.getAsString("the_products"))) {
                    coupons.put("theProducts", "本商铺通用");
                } else {
                    String[] arrChecked = coupons.getAsString("the_products").split(",");
                    String proName = "";
                    for (int i = 0; i < arrChecked.length; i++) {
                        if (StringUtils.isNotEmpty(arrChecked[i])) {
                            Dto pro = (BaseDto) bizService.queryForDto("product.getInfo", new BaseDto("id", arrChecked[i]));
                            proName = proName + pro.getAsString("name") + ",";
                        }
                    }
                    coupons.put("theProducts", proName);
                }
            }

            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询优惠券配置表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/addMarketProduct")
    public BaseResult addMarketProduct(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member != null) {
                String id = member.getAsString("id");
                dto.put("creator", id);
            }
            String[] arrChecked = dto.getAsString("ids").split(",");
            for (int i = 0; i < arrChecked.length; i++) {
                Dto market = new BaseDto();
                market.put("tableName", "marketProduct");
                if (StringUtils.isNotEmpty(arrChecked[i])) {
                    Dto inquire = new BaseDto();
                    inquire.put("productid", arrChecked[i]);
                    inquire.put("shopid", member.getAsString("shop_id"));
                    market.putAll(inquire);
                    Dto info = (BaseDto) bizService.queryForDto("marketProduct.getInfo", inquire);
                    if (null != info) {
                        market.put("updator", member.getAsString("id"));
                        market.put("id", info.getAsString("id"));
                        market.put("method", "updateInfo");
                        if ("kill".equals(dto.getAsString("type"))) {
                            market.put("secid", dto.getAsString("marketid"));
                            bizService.update(market);
                        } else if ("full".equals(dto.getAsString("type"))) {
                            market.put("fullid", dto.getAsString("marketid"));
                            bizService.update(market);
                        } else if ("new".equals(dto.getAsString("type"))) {
                            market.put("newuserid", dto.getAsString("marketid"));
                            bizService.update(market);
                        } else if ("pro".equals(dto.getAsString("type"))) {
                            market.put("specialpid", dto.getAsString("marketid"));
                            bizService.update(market);
                        } else if ("group".equals(dto.getAsString("type"))) {
                            market.put("groupid", dto.getAsString("marketid"));
                            bizService.update(market);
                        }
                    } else {
                        market.put("method", "saveInfo");
                        market.put("creator", member.getAsString("id"));
                        if ("kill".equals(dto.getAsString("type"))) {
                            market.put("secid", dto.getAsString("marketid"));
                            bizService.save(market);
                        } else if ("full".equals(dto.getAsString("type"))) {
                            market.put("fullid", dto.getAsString("marketid"));
                            bizService.save(market);
                        } else if ("new".equals(dto.getAsString("type"))) {
                            market.put("newuserid", dto.getAsString("marketid"));
                            bizService.save(market);
                        } else if ("pro".equals(dto.getAsString("type"))) {
                            market.put("specialpid", dto.getAsString("marketid"));
                            bizService.save(market);
                        } else if ("group".equals(dto.getAsString("type"))) {
                            market.put("groupid", dto.getAsString("marketid"));
                            bizService.save(market);
                        }
                    }
                }
            }
            result.setData("success");
            result.setMsg("商品关联成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询优惠券配置表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryShopMarket")
    public BaseResult queryShopMarket(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member != null) {
                String id = member.getAsString("id");
                dto.put("creator", id);
            }
            List<Dto> paramList = new ArrayList();
            if ("1".equals(member.getAsString("id"))) {
                paramList = bizService.queryForList(dto.getAsString("t") + ".queryList", dto);
            } else {
                Dto shopDto = (BaseDto) bizService.queryForObject("shop.getInfo", new BaseDto("sysUserId", member.getAsString("id")));
                dto.put("shopId", shopDto.getAsString("id"));
                paramList = bizService.queryForList(dto.getAsString("t") + ".queryList", dto);
            }

            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询秒杀集合
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryMarketSec")
    public BaseResult queryMarketSec(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List paramList = bizService.queryForList("marketProduct.queryListSec", dto);
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询秒杀商品集合
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryListMarketProduct")
    public BaseResult queryListMarketProduct(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List paramList = bizService.queryForList("marketProduct.queryListMarketProduct", dto);
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

}
