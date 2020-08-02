package com.jfc.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfc.base.BaseController;
import com.jfc.util.BaseResult;
import com.jfc.util.CommonUtil;
import com.jfc.util.HttpClientUtil;
import com.jfc.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;

/**
 * 微信支付信息相关controller
 *
 * @author liujie
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WxController extends BaseController {



    /**
     * 获取openid
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getOpenidPay")
    public BaseResult getOpenidPay(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx3abf420b5d673535");
            param.put("secret", "4072402735e1caed09114b8966658955");
            param.put("js_code", dto.getAsString("code"));
            param.put("grant_type", "authorization_code");
            Map<String, String> head = new HashMap<>();
            head.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            String s = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/sns/jscode2session", param, head);
            if (StringUtils.isNotEmpty(s)) {
                JSONObject jsonObject = JSONObject.fromObject(s);
                //获取openid
                String openid = jsonObject.getString("openid");
                redisService.setValue("openid",openid);
                //调用统一下单接口
                Map<String,String> paramOrder=new LinkedHashMap<>();
                //小程序ID
                paramOrder.put("appid","wx703f85c99508abae");
                //商户号
                paramOrder.put("mch_id","1517576291");
                //随机字符串
                paramOrder.put("nonce_str",String.valueOf(new Date().getTime()));
                //签名类型
                paramOrder.put("sign_type","MD5");
                //商品描述
                paramOrder.put("body","shopping");
                //商户订单号
                paramOrder.put("out_trade_no",String.valueOf(new Date().getTime()));
                //标价金额
                paramOrder.put("total_fee",String.valueOf(new BigDecimal(String.valueOf(dto.getAsDouble("amount"))).movePointRight(2).intValue()));
                //终端IP
                paramOrder.put("spbill_create_ip", InetAddress.getLocalHost().toString().split("/")[1]);
                //通知地址
//                paramOrder.put("notify_url","http://payzcj.free.idcfengye.com/jfc-shop/weixin/getPaySuccess");
                paramOrder.put("notify_url","http://rest.zaintea.com/jfc-shop/weixin/getPaySuccess");
                //交易类型
                paramOrder.put("trade_type","JSAPI");
                //用户标识
                paramOrder.put("sub_openid",openid);
                //子商户号
                paramOrder.put("sub_mch_id","1546327341");
                //子appid
                paramOrder.put("sub_appid","wx3abf420b5d673535");
                //调用签名
                paramOrder= CommonUtil.formatUrlMap(paramOrder, true, true);
                String paramPay=CommonUtil.mapToxml(paramOrder);
                System.out.println(paramPay);
                String payResult = HttpClientUtil.doRequestPostStr("https://api.mch.weixin.qq.com/pay/unifiedorder", paramPay, null);
                System.out.println(payResult);
                Document doc = DocumentHelper.parseText(payResult);
                Element root = doc.getRootElement();
                String prepay_id=root.elementText("prepay_id");
                System.out.println(prepay_id);
                Map<String, String> resultDataInfo=new LinkedHashMap<>();
                resultDataInfo.put("appId","wx3abf420b5d673535");
                resultDataInfo.put("timeStamp",String.valueOf(System.currentTimeMillis()));
                resultDataInfo.put("nonceStr",String.valueOf(System.currentTimeMillis()));
                resultDataInfo.put("package","prepay_id="+prepay_id);
                resultDataInfo.put("signType","MD5");
                resultDataInfo=CommonUtil.formatUrlMap2(resultDataInfo,true,true);
                resultDataInfo.put("prepay_id",prepay_id);
                result.setData(resultDataInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 支付回调
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getPaySuccess")
    public void getPaySuccess(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
            String resXml="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            response.setContentType("text/xml");
            BufferedOutputStream out = new BufferedOutputStream(
                    response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信模板推送
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getWxMsgInfo")
    public void getWxMsgInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                throw new Exception("请登录后操作");
            }
            //获取openid
            String openid = member.getAsString("wopenid");
            //获取access_token
            Map mapGet=new HashMap();
            mapGet.put("grant_type","client_credential");
            mapGet.put("appid","wx3abf420b5d673535");
            mapGet.put("secret","4072402735e1caed09114b8966658955");
            String s1 = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/cgi-bin/token", mapGet, null);
            if (StringUtils.isNotEmpty(s1)) {
                JSONObject jsonObjectAccess = JSONObject.fromObject(s1);
                //获取access_token
                String access_token = jsonObjectAccess.getString("access_token");
                System.out.println(s1);
                Map<String,Object> paramSend=new HashMap<>();
                paramSend.put("access_token",access_token);
                paramSend.put("touser",openid);
                //keyword1
                Map<String,Object> keyword1=new HashMap();
                keyword1.put("value","02155451515151");
                keyword1.put("color","#173177");
                //keyword2
                Map<String,Object> keyword2=new HashMap();
                keyword2.put("value","2份香飘飘奶茶大杯");
                keyword2.put("color","#173177");
                //keyword3
                Map<String,Object> keyword3=new HashMap();
                keyword3.put("value","36元");
                keyword3.put("color","#173177");
                //keyword4
                Map<String,Object> keyword4=new HashMap();
                keyword4.put("value","2杯");
                keyword4.put("color","#173177");
                //keyword5
                Map<String,Object> keyword5=new HashMap();
                keyword5.put("value","浦东新区益江路门店");
                keyword5.put("color","#173177");
                //keyword5
                Map<String,Object> keyword6=new HashMap();
                keyword6.put("value","2019-08-08");
                keyword6.put("color","#173177");
                Map<String,Object> object=new HashMap();
                object.put("page","/pages/index/main");
                object.put("template_id","AafCRCZGMjeZKBMXFA6bD7eu3P9YWEsQ1gSb-o6xdzg");
                Map<String,Object> objectData=new HashMap();
                objectData.put("keyword1",keyword1);
                objectData.put("keyword2",keyword2);
                objectData.put("keyword3",keyword3);
                objectData.put("keyword4",keyword4);
                objectData.put("keyword5",keyword5);
                objectData.put("keyword6",keyword6);
                object.put("data",objectData);
                object.put("form_id",dto.getAsString("form_id"));
                object.put("emphasis_keyword","2份香飘飘奶茶大杯");
                paramSend.put("weapp_template_msg",object);

                //productType
                JSONObject productType=new JSONObject();
                productType.put("value","奶茶名称");
                productType.put("color","#173177");
                //name
                JSONObject name=new JSONObject();
                name.put("value","2份香飘飘奶茶大杯");
                name.put("color","#173177");
                //number
                JSONObject number=new JSONObject();
                number.put("value","2份");
                number.put("color","#173177");
                //expDate
                JSONObject expDate=new JSONObject();
                expDate.put("value","2019-08-08");
                expDate.put("color","#173177");
                //remark
                JSONObject remark=new JSONObject();
                remark.put("value","一大一小，少冰");
                remark.put("color","#173177");
                String datasWx=productType.toString()+","+name.toString()+","+number.toString()+","+expDate.toString()+","+remark.toString();
                JSONObject objectWx=new JSONObject();
                objectWx.put("appid","wx05263cbb6f4a1c26");
                objectWx.put("template_id","SOOzzS09tnbJA3tkxHcuDYBCZM50_Q_o2l2Ums8yEH8");
                objectWx.put("data",datasWx);
                objectWx.put("url","pages/login/main");
                objectWx.put("emphasis_keyword","keyword1.DATA");
                JSONObject objectPro=new JSONObject();
                objectPro.put("appid","wx3abf420b5d673535");
                objectPro.put("pagepath","pages/login/main");
                objectWx.put("miniprogram",objectPro.toString());
                paramSend.put("mp_template_msg",objectWx.toString());
                System.out.println(HttpClientUtil.doRequestPostJson("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token="+access_token, JSON.toJSONString(paramSend),null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getOpenid")
    public BaseResult getOpenid(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try{
            String clt = dto.getAsString("clt");
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx3abf420b5d673535");
            param.put("secret", "4072402735e1caed09114b8966658955");
            param.put("js_code", dto.getAsString("code"));
            param.put("grant_type", "authorization_code");
            Map<String, String> head = new HashMap<>();
            head.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            String s = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/sns/jscode2session", param, head);
            if (StringUtils.isNotEmpty(s)) {
                JSONObject jsonObject = JSONObject.fromObject(s);
                //获取openid
                String openid = jsonObject.getString("openid");
                Dto paramData = new BaseDto();
                paramData.put("wopenid", openid);
                Dto resultData = (Dto) bizService.queryForObject("member.getInfo", paramData);
                if (null != resultData) {
                    Dto resultParam = new BaseDto();
                    String uuid = UUID.randomUUID().toString();
                    resultParam.put("username", resultData.getAsString("nickname"));
                    resultParam.put("token", uuid);
                    resultParam.put("id", resultData.getAsString("id"));
                    resultParam.put("pic", resultData.getAsString("pic"));
                    resultParam.put("telephone", resultData.getAsString("telephone"));
                    resultParam.put("wopenid", resultData.getAsString("wopenid"));
                    resultParam.put("zopenid", resultData.getAsString("zopenid"));
                    resultParam.put("grade_id", resultData.getAsString("grade_id"));
                    redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
                    result.setData(resultParam);
                } else {
                    Dto resultParam = new BaseDto();
                    resultParam.put("wopenid", openid);
                    resultParam.put("flg", "1");
                    result.setData(resultParam);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result=reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clientLogin")
    public BaseResult clientLogin(HttpServletRequest request, HttpServletResponse response) throws DocumentException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //判断验证码是否正确
            String code = redisService.getValue(dto.getAsString("telephone"));
            String clt = dto.getAsString("clt");
            if(!StringUtils.isNotEmpty(dto.getAsString("telephone"))){
                throw new Exception("手机号不能为空!");
            }
            if(StringUtils.isNotEmpty(dto.getAsString("code"))){
                if(!dto.getAsString("code").equals(redisService.getValue(dto.getAsString("telephone")))){
                    throw new Exception("验证码有误");
                }
            }else{
                throw new Exception("验证码不能为空");
            }
            //if (StringUtils.isNotEmpty(code) && code.equals(dto.getAsString("code"))) {
                //判断手机号是否存在，不存在新增用户登录
                Dto resultParam = new BaseDto();
//                resultParam.put("telephone", dto.getAsString("telephone"));
                resultParam.put("wopenid", dto.getAsString("wopenid"));
                Dto paramList = (Dto)bizService.queryForDto("member.getInfo", resultParam);
                if(null!=paramList){
                    //判断手机号是否存在
                    if(!StringUtils.isNotEmpty(paramList.getAsString("telephone"))){
                        //更新手机号进去
                        Dto updatePhoneParam=new BaseDto();
                        updatePhoneParam.put("telephone",dto.getAsString("telephone"));
                        updatePhoneParam.put("id",paramList.getAsString("id"));
                        bizService.updateInfo("member.updateInfo",updatePhoneParam);
                    }
                    //存在，直接登录
                    String uuid = UUID.randomUUID().toString();
                    resultParam.put("token", uuid);
                    resultParam.put("id", paramList.getAsString("id"));
                    resultParam.put("username", paramList.getAsString("nickname"));
                    resultParam.put("pic", paramList.getAsString("pic"));
                    resultParam.put("telephone", paramList.getAsString("telephone"));
                    resultParam.put("wopenid", paramList.getAsString("wopenid"));
                    resultParam.put("zopenid", paramList.getAsString("zopenid"));
                    resultParam.put("grade_id", paramList.getAsString("grade_id"));
                    redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
                }else{
                    dto.put("username", dto.getAsString("nickname"));
                    bizService.saveInfo("member.saveInfo",dto);
                    //添加新用户优惠券
                    List<Dto> addList = bizService.queryList("product.getNewAddUserInfo", new BaseDto());
                    //增加优惠券
                    if(null!=addList && addList.size()>0){
                        for(Dto datas:addList){

                        }
                    }
                    // 添加MGM优惠券
                    //老带新mgm赠送
                    Dto marketData = (Dto)bizService.queryForObject("marketMgm.getOldAndUserMarket", new BaseDto());
                    //判断当前登录用户有没有邀请人id
                    Dto userData=(Dto)bizService.queryForObject("member.getInfo",new BaseDto("id",dto.getAsString("id")));
                    if(null!=userData && org.g4studio.core.resource.util.StringUtils.isNotEmpty(userData.getAsString("recommond_id"))){
                        //查询新用户优惠券信息
                        Dto couponNew=(Dto)bizService.queryForObject("marketCoupon.getInfo",new BaseDto("id",marketData.getAsString("coupon_id")));
                        if(null!=couponNew){
                            /*Dto addParam=new BaseDto();
                            addParam.put("member_id",dto.getAsString("id"));
                            addParam.put("coupon_id",marketData.getAsString("coupon_id"));
                            addParam.put("rmember",userData.getAsString("recommond_id"));
                            addParam.put("starttime",couponNew.getAsString("starttime"));
                            addParam.put("endtime",couponNew.getAsString("endtime"));
                            addParam.put("status","180");
                            bizService.saveInfo("marketCouponUser.saveInfo",addParam);*/
                        }
                    }

                    String uuid = UUID.randomUUID().toString();
                    resultParam.put("token", uuid);
                    resultParam.put("id", dto.getAsString("id"));
                    resultParam.put("username", dto.getAsString("nickname"));
                    resultParam.put("pic", dto.getAsString("pic"));
                    resultParam.put("telephone", dto.getAsString("telephone"));
                    resultParam.put("wopenid", dto.getAsString("wopenid"));
                    resultParam.put("zopenid", dto.getAsString("zopenid"));
                    resultParam.put("grade_id",1);
                    redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
                }
                result.setData(resultParam);
//            } else {
//                result.setCode("9999");
//                result.setData("验证码错误");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getClientOpenid")
    public BaseResult getClientOpenid(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try{
            String clt = dto.getAsString("clt");
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx3abf420b5d673535");
            param.put("secret", "4072402735e1caed09114b8966658955");
            param.put("js_code", dto.getAsString("code"));
            param.put("grant_type", "authorization_code");
            Map<String, String> head = new HashMap<>();
            head.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            String s = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/sns/jscode2session", param, head);
            if (StringUtils.isNotEmpty(s)) {
                JSONObject jsonObject = JSONObject.fromObject(s);
                //获取openid
                String openid = jsonObject.getString("openid");
                Dto resultParam = new BaseDto();
                resultParam.put("openid", openid);
                result.setData(resultParam);
            }
        }catch (Exception e){
            e.printStackTrace();
            result=reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/clientLoginShare")
    public BaseResult clientLoginShare(HttpServletRequest request, HttpServletResponse response) throws DocumentException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //判断验证码是否正确
            String code = redisService.getValue(dto.getAsString("telephone"));
            String clt = dto.getAsString("clt");
            if(!StringUtils.isNotEmpty(dto.getAsString("telephone"))){
                throw new Exception("手机号不能为空!");
            }
            if(StringUtils.isNotEmpty(dto.getAsString("code"))){
                if(!dto.getAsString("code").equals(redisService.getValue(dto.getAsString("telephone")))){
                    throw new Exception("验证码有误");
                }
            }else{
                throw new Exception("验证码不能为空");
            }
            //if (StringUtils.isNotEmpty(code) && code.equals(dto.getAsString("code"))) {
            //判断手机号是否存在，不存在新增用户登录
            Dto resultParam = new BaseDto();
            resultParam.put("wopenid", dto.getAsString("wopenid"));
            Dto paramList = (Dto)bizService.queryForDto("member.getInfo", resultParam);
            if(null!=paramList){
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", paramList.getAsString("id"));
                resultParam.put("username", paramList.getAsString("nickname"));
                resultParam.put("pic", paramList.getAsString("pic"));
                resultParam.put("telephone", paramList.getAsString("telephone"));
                resultParam.put("wopenid", paramList.getAsString("wopenid"));
                resultParam.put("zopenid", paramList.getAsString("zopenid"));
                resultParam.put("oldFlg", "1");
                //存在，直接登录
                //throw new Exception("亲，老用户不能参加!");
            }else{
                dto.put("username", dto.getAsString("nickname"));
                bizService.saveInfo("member.saveInfo",dto);
                //添加新用户优惠券
                List<Dto> addList = bizService.queryList("product.getNewAddUserInfo", new BaseDto());
                //增加优惠券
                List<String>  arrays=new ArrayList<>();
                if(null!=addList && addList.size()>0){
                    for(Dto datas:addList){
                        Dto addParam=new BaseDto();
//                        arrays.add(datas.getAsString("id"));
                        /*addParam.put("member_id",dto.getAsString("id"));
                        addParam.put("coupon_id",datas.getAsString("id"));
                        addParam.put("starttime",datas.getAsString("starttime"));
                        addParam.put("endtime",datas.getAsString("endtime"));
                        addParam.put("status","180");
                        bizService.saveInfo("marketCouponUser.saveInfo",addParam);*/
                    }
                }
                // 添加MGM优惠券
                //老带新mgm赠送
                Dto marketData = (Dto)bizService.queryForObject("marketMgm.getOldAndUserMarket", new BaseDto());
                //判断当前登录用户有没有邀请人id
                Dto userData=(Dto)bizService.queryForObject("member.getInfo",new BaseDto("id",dto.getAsString("id")));
                if(null!=userData && org.g4studio.core.resource.util.StringUtils.isNotEmpty(userData.getAsString("recommond_id"))){
                    //查询新用户优惠券信息
                    Dto couponNew=(Dto)bizService.queryForObject("marketCoupon.getInfo",new BaseDto("id",marketData.getAsString("coupon_id")));
                    if(null!=couponNew){
                        arrays.add(marketData.getAsString("coupon_id"));
                        /*Dto addParam=new BaseDto();
                        addParam.put("member_id",dto.getAsString("id"));
                        addParam.put("coupon_id",marketData.getAsString("coupon_id"));
                        addParam.put("rmember",userData.getAsString("recommond_id"));
                        addParam.put("starttime",couponNew.getAsString("starttime"));
                        addParam.put("endtime",couponNew.getAsString("endtime"));
                        addParam.put("status","180");
                        bizService.saveInfo("marketCouponUser.saveInfo",addParam);*/
                    }
                }
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", dto.getAsString("id"));
                resultParam.put("username", dto.getAsString("nickname"));
                resultParam.put("pic", dto.getAsString("pic"));
                resultParam.put("telephone", dto.getAsString("telephone"));
                resultParam.put("wopenid", dto.getAsString("wopenid"));
                resultParam.put("zopenid", dto.getAsString("zopenid"));
                resultParam.put("oldFlg", "0");
                List<Dto> listParam = bizService.queryList("marketCoupon.getCouponListById", new BaseDto("ids", StringUtils.join(arrays, ",")));
                if(null!=listParam && listParam.size()>0){
                    for(Dto params:listParam){
                        params.put("timesDesc","距离优惠券有效期还剩下"+CommonUtil.getDistanceDays(params.getAsString("starttime"),params.getAsString("endtime"))+"天");
                    }
                }
                resultParam.put("coupons",listParam);
            }
            result.setData(resultParam);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 微信模板推送
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getWxMsgSendInfo")
    public void getWxMsgSendInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                throw new Exception("请登录后操作");
            }
            //获取openid
            String openid = member.getAsString("wopenid");
            //获取access_token
            Map mapGet=new HashMap();
            mapGet.put("grant_type","client_credential");
            mapGet.put("appid","wx3abf420b5d673535");
            mapGet.put("secret","4072402735e1caed09114b8966658955");
            String s1 = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/cgi-bin/token", mapGet, null);
            if (StringUtils.isNotEmpty(s1)) {
                JSONObject jsonObjectAccess = JSONObject.fromObject(s1);
                //获取access_token
                String access_token = jsonObjectAccess.getString("access_token");
                System.out.println(s1);
                Map<String,Object> paramSend=new HashMap<>();
                paramSend.put("access_token",access_token);
                paramSend.put("touser",openid);
                //查询订单信息
                Dto paramsOrder=(Dto)bizService.queryForObject("order.getInfo",new BaseDto("id",dto.getAsString("id")));
                if(null!=paramsOrder){
                    //keyword1
                    Map<String,Object> keyword0=new HashMap();
                    keyword0.put("value",paramsOrder.getAsString("diningCode"));
                    keyword0.put("color","#173177");
                    //keyword1
                    Map<String,Object> keyword1=new HashMap();
                    keyword1.put("value",paramsOrder.getAsString("number"));
                    keyword1.put("color","#173177");
                    //keyword2
                    Map<String,Object> keyword2=new HashMap();
                    keyword2.put("value",paramsOrder.getAsInteger("total")>1?paramsOrder.getAsString("productName")+"等":paramsOrder.getAsString("productName"));
                    keyword2.put("color","#173177");
                    //keyword3
                    Map<String,Object> keyword3=new HashMap();
                    keyword3.put("value",paramsOrder.getAsString("pay_money"));
                    keyword3.put("color","#173177");
                    //keyword4
                    Map<String,Object> keyword4=new HashMap();
                    keyword4.put("value",paramsOrder.getAsString("total")+"杯");
                    keyword4.put("color","#173177");
                    //keyword5
                    Map<String,Object> keyword5=new HashMap();
                    keyword5.put("value",paramsOrder.getAsString("shopName"));
                    keyword5.put("color","#173177");
                    //keyword5
                    Map<String,Object> keyword6=new HashMap();
                    keyword6.put("value",paramsOrder.getAsString("create_time"));
                    keyword6.put("color","#173177");
                    Map<String,Object> object=new HashMap();
                    object.put("page","pages/orderCode/main?order_id="+paramsOrder.getAsString("id")+"&status="+paramsOrder.getAsString("status"));
                    object.put("template_id","yILOSjznKgYZXS98rL1348_2aoxe4RcCOBpQS47UrMo");
                    Map<String,Object> objectData=new HashMap();
                    objectData.put("keyword1",keyword0);
                    objectData.put("keyword2",keyword1);
                    objectData.put("keyword3",keyword2);
                    objectData.put("keyword4",keyword3);
                    objectData.put("keyword5",keyword4);
                    objectData.put("keyword6",keyword5);
                    objectData.put("keyword7",keyword6);
                    object.put("data",objectData);
                    object.put("form_id",dto.getAsString("form_id"));
                    object.put("emphasis_keyword","keyword1.DATA");
                    paramSend.put("weapp_template_msg",object);

                    //productType
                    JSONObject productType=new JSONObject();
                    productType.put("value","奶茶名称");
                    productType.put("color","#173177");
                    //name
                    JSONObject name=new JSONObject();
                    name.put("value","2份香飘飘奶茶大杯");
                    name.put("color","#173177");
                    //number
                    JSONObject number=new JSONObject();
                    number.put("value","2份");
                    number.put("color","#173177");
                    //expDate
                    JSONObject expDate=new JSONObject();
                    expDate.put("value","2019-08-08");
                    expDate.put("color","#173177");
                    //remark
                    JSONObject remark=new JSONObject();
                    remark.put("value","一大一小，少冰");
                    remark.put("color","#173177");
                    String datasWx=productType.toString()+","+name.toString()+","+number.toString()+","+expDate.toString()+","+remark.toString();
                    JSONObject objectWx=new JSONObject();
                    objectWx.put("appid","wx05263cbb6f4a1c26");
                    objectWx.put("template_id","SOOzzS09tnbJA3tkxHcuDYBCZM50_Q_o2l2Ums8yEH8");
                    objectWx.put("data",datasWx);
                    objectWx.put("url","pages/login/main");
                    objectWx.put("emphasis_keyword","keyword1.DATA");
                    JSONObject objectPro=new JSONObject();
                    objectPro.put("appid","wx3abf420b5d673535");
                    objectPro.put("pagepath","pages/login/main");
                    objectWx.put("miniprogram",objectPro.toString());
                    paramSend.put("mp_template_msg",objectWx.toString());
                    HttpClientUtil.doRequestPostJson("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token="+access_token, JSON.toJSONString(paramSend),null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密微信手机号
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/decryptWxMobile")
    public BaseResult decryptWxMobile(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result=new BaseResult();
        Dto resultParam = new BaseDto();
        try {
            String clt=dto.getAsString("clt");
            String mobileData=CommonUtil.decryptS5(dto.getAsString("decryptData"),"UTF-8",dto.getAsString("key"),dto.getAsString("iv"));
            String mobile="";
            if(StringUtils.isNotEmpty(mobileData)){
                mobile=JSONUtil.parseJSON2Map(mobileData).get("phoneNumber").toString();
            }
            //获取openid
            String openid = dto.getAsString("wopenid");
            resultParam.put("wopenid", openid);
            Dto paramList = (Dto)bizService.queryForDto("member.getInfo", resultParam);
            if(null!=paramList){
                //判断手机号是否存在
                if(!StringUtils.isNotEmpty(paramList.getAsString("telephone"))){
                    //更新手机号
                    Dto paramInfo=new BaseDto();
                    paramInfo.put("telephone",mobile);
                    paramInfo.put("id",paramList.getAsString("id"));
                    bizService.updateInfo("member.updateInfo",paramInfo);
                }
                //存在，直接登录
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", paramList.getAsString("id"));
                resultParam.put("username", paramList.getAsString("nickname"));
                resultParam.put("pic", paramList.getAsString("pic"));
                resultParam.put("telephone", paramList.getAsString("telephone"));
                resultParam.put("wopenid", paramList.getAsString("wopenid"));
                redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            }else{
                dto.put("pic","http://static.zaintea.com/log/loginUser.png");
                dto.put("username","尾号"+mobile.substring(mobile.length()-4,mobile.length()));
                dto.put("telephone",mobile);
                bizService.saveInfo("member.saveInfo",dto);
                //存在，直接登录
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", dto.getAsString("id"));
                resultParam.put("username", dto.getAsString("nickname"));
                resultParam.put("pic", dto.getAsString("pic"));
                resultParam.put("telephone", dto.getAsString("telephone"));
                resultParam.put("wopenid", dto.getAsString("wopenid"));
                redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            }
           result.setData(resultParam);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 校验用户是否登录过
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkOpenidIsOk")
    public BaseResult checkOpenidIsOk(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try{
            Dto resultParam = new BaseDto();
            String clt = dto.getAsString("clt");
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx3abf420b5d673535");
            param.put("secret", "4072402735e1caed09114b8966658955");
            param.put("js_code", dto.getAsString("code"));
            param.put("grant_type", "authorization_code");
            Map<String, String> head = new HashMap<>();
            head.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            String s = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/sns/jscode2session", param, head);
            if (StringUtils.isNotEmpty(s)) {
                JSONObject jsonObject = JSONObject.fromObject(s);
                //获取openid
                String openid = jsonObject.getString("openid");
                String session_key=jsonObject.getString("session_key");
                Dto paramData = new BaseDto();
                paramData.put("wopenid", openid);
                Dto resultData = (Dto) bizService.queryForObject("member.getInfo", paramData);
                if (null != resultData) {
                    resultData = (Dto) bizService.queryForObject("member.getInfo", new BaseDto("id",resultData.get("id")));
                    resultParam.put("username", resultData.getAsString("username"));
                    resultParam.put("id", resultData.getAsString("id"));
                    resultParam.put("pic", resultData.getAsString("pic"));
                    resultParam.put("telephone", resultData.getAsString("telephone"));
                    resultParam.put("session_key", session_key);
                    resultParam.put("type", 1);
                    resultParam.put("wopenid", openid);
                    resultParam.put("oflg", resultData.get("oflg"));
                    String uuid = UUID.randomUUID().toString();
                    resultParam.put("token", uuid);
                    redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
                }else{
                    resultParam.put("type", 0);
                }
                resultParam.put("session_key", session_key);
                resultParam.put("wopenid", openid);
                result.setData(resultParam);
            }
        }catch (Exception e){
            e.printStackTrace();
            result=reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 微信自动openid获取用户资料登录
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/wxLoginInfo")
    public BaseResult wxLoginInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try{
            Dto resultParam = new BaseDto();
            //获取openid
            Dto paramData = new BaseDto();
            paramData.put("wopenid", dto.getAsString("wopenid"));
            String clt = dto.getAsString("clt");
            Dto resultData = (Dto) bizService.queryForObject("member.getInfo", paramData);
            if (null == resultData) {
                //插入用户
                dto.put("username", dto.getAsString("nickname"));
                bizService.saveInfo("member.saveInfo",dto);
                //判断是否登录
                //添加新用户优惠券
                List<Dto> addList = bizService.queryList("product.getNewAddUserInfo", new BaseDto());
                //增加优惠券
                if(null!=addList && addList.size()>0){
                    for(Dto datas:addList){
                        /*Dto addParam=new BaseDto();
                        addParam.put("member_id",dto.getAsString("id"));
                        addParam.put("coupon_id",datas.getAsString("id"));
                        addParam.put("starttime",datas.getAsString("starttime"));
                        addParam.put("endtime",datas.getAsString("endtime"));
                        addParam.put("status","180");
                        bizService.saveInfo("marketCouponUser.saveInfo",addParam);*/
                    }
                }
                // 添加MGM优惠券
                //老带新mgm赠送
                Dto marketData = (Dto)bizService.queryForObject("marketMgm.getOldAndUserMarket", new BaseDto());
                //判断当前登录用户有没有邀请人id
                Dto userData=(Dto)bizService.queryForObject("member.getInfo",new BaseDto("id",dto.getAsString("id")));
                if(null!=userData && org.g4studio.core.resource.util.StringUtils.isNotEmpty(userData.getAsString("recommond_id"))){
                    //查询新用户优惠券信息
                    Dto couponNew=(Dto)bizService.queryForObject("marketCoupon.getInfo",new BaseDto("id",marketData.getAsString("coupon_id")));
                    if(null!=couponNew){
                        /*Dto addParam=new BaseDto();
                        addParam.put("member_id",dto.getAsString("id"));
                        addParam.put("coupon_id",marketData.getAsString("coupon_id"));
                        addParam.put("rmember",userData.getAsString("recommond_id"));
                        addParam.put("starttime",couponNew.getAsString("starttime"));
                        addParam.put("endtime",couponNew.getAsString("endtime"));
                        addParam.put("status","180");
                        bizService.saveInfo("marketCouponUser.saveInfo",addParam);*/
                    }
                }
            }else{
                dto.put("id",resultData.getAsString("id"));
            }
            String uuid = UUID.randomUUID().toString();
            resultParam.put("token", uuid);
            resultParam.put("id", dto.getAsString("id"));
            resultParam.put("username", dto.getAsString("nickname"));
            resultParam.put("pic", dto.getAsString("pic"));
            resultParam.put("telephone", dto.getAsString("telephone"));
            resultParam.put("wopenid", dto.getAsString("wopenid"));
            resultParam.put("zopenid", dto.getAsString("zopenid"));
            resultParam.put("grade_id",1);
            redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            result.setData(resultParam);
        }catch (Exception e){
            e.printStackTrace();
            result=reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 微信取餐模板推送
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getWxTakeSendInfo")
    public BaseResult getWxTakeInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result=new BaseResult();
        try {

            //查询订单信息
            Dto paramsOrder=(Dto)bizService.queryForObject("order.getInfo",new BaseDto("id",dto.getAsString("id")));
            Dto member = (Dto)bizService.queryForObject("member.getInfo",new BaseDto("id",paramsOrder.getAsString("member_id")));
            //获取openid
            String openid = member.getAsString("wopenid");
            //获取access_token
            Map mapGet=new HashMap();
            mapGet.put("grant_type","client_credential");
            mapGet.put("appid","wx3abf420b5d673535");
            mapGet.put("secret","4072402735e1caed09114b8966658955");
            String s1 = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/cgi-bin/token", mapGet, null);
            if (StringUtils.isNotEmpty(s1)) {
                JSONObject jsonObjectAccess = JSONObject.fromObject(s1);
                //获取access_token
                String access_token = jsonObjectAccess.getString("access_token");
                System.out.println(s1);
                Map<String,Object> paramSend=new HashMap<>();
                paramSend.put("access_token",access_token);
                paramSend.put("touser",openid);

                if(null!=paramsOrder){
                    //keyword1
                    Map<String,Object> keyword1=new HashMap();
                    keyword1.put("value",paramsOrder.getAsString("shopName"));
                    keyword1.put("color","#173177");
                    //keyword2
                    Map<String,Object> keyword2=new HashMap();
                    keyword2.put("value",paramsOrder.getAsString("shopAddress"));
                    keyword2.put("color","#173177");
                    //keyword3
                    Map<String,Object> keyword3=new HashMap();
                    keyword3.put("value",paramsOrder.getAsInteger("total")>1?paramsOrder.getAsString("productName")+"等":paramsOrder.getAsString("productName"));
                    keyword3.put("color","#173177");
                    //keyword4
                    Map<String,Object> keyword4=new HashMap();
                    keyword4.put("value",paramsOrder.getAsString("diningCode"));
                    keyword4.put("color","#173177");
                    //keyword5
                    Map<String,Object> keyword5=new HashMap();
                    keyword5.put("value",paramsOrder.getAsString("create_time"));
                    keyword5.put("color","#173177");
                    //keyword5
                    Map<String,Object> keyword6=new HashMap();
                    keyword6.put("value",paramsOrder.getAsString("start_time")+" ~ "+paramsOrder.getAsString("end_time"));
                    keyword6.put("color","#173177");
                    //keyword5
                    Map<String,Object> keyword7=new HashMap();
                    if(paramsOrder.getAsString("is_delivery").equals("0")){
                        keyword7.put("value","你的茶饮已经制作完成，请尽快到店取茶，口感更佳");
                    }else{
                        keyword7.put("value","请你耐心等候");
                    }
                    keyword7.put("color","#173177");
                    Map<String,Object> object=new HashMap();
                    object.put("page","pages/orderCode/main?order_id="+paramsOrder.getAsString("id")+"&status="+paramsOrder.getAsString("status"));
                    object.put("template_id","_ZbW1mPds8JDfZkcTV2VHXXWl9s8GzM3yTkL1KajbL4");
                    Map<String,Object> objectData=new HashMap();
                    objectData.put("keyword1",keyword1);
                    objectData.put("keyword2",keyword2);
                    objectData.put("keyword3",keyword3);
                    objectData.put("keyword4",keyword4);
                    objectData.put("keyword5",keyword5);
                    objectData.put("keyword6",keyword6);
                    objectData.put("keyword7",keyword7);
                    object.put("data",objectData);
                    if(StringUtils.isNotEmpty(paramsOrder.getAsString("form_id"))){
                        object.put("form_id",paramsOrder.getAsString("form_id"));
                    }else{
                        object.put("form_id",redisService.getValue(openid));
                        redisService.delete(openid);
                    }
                    object.put("emphasis_keyword","keyword4.DATA");
                    paramSend.put("weapp_template_msg",object);

                    //productType
                    JSONObject productType=new JSONObject();
                    productType.put("value","奶茶名称");
                    productType.put("color","#173177");
                    //name
                    JSONObject name=new JSONObject();
                    name.put("value","2份香飘飘奶茶大杯");
                    name.put("color","#173177");
                    //number
                    JSONObject number=new JSONObject();
                    number.put("value","2份");
                    number.put("color","#173177");
                    //expDate
                    JSONObject expDate=new JSONObject();
                    expDate.put("value","2019-08-08");
                    expDate.put("color","#173177");
                    //remark
                    JSONObject remark=new JSONObject();
                    remark.put("value","一大一小，少冰");
                    remark.put("color","#173177");
                    String datasWx=productType.toString()+","+name.toString()+","+number.toString()+","+expDate.toString()+","+remark.toString();
                    JSONObject objectWx=new JSONObject();
                    objectWx.put("appid","wx05263cbb6f4a1c26");
                    objectWx.put("template_id","SOOzzS09tnbJA3tkxHcuDYBCZM50_Q_o2l2Ums8yEH8");
                    objectWx.put("data",datasWx);
                    objectWx.put("url","pages/login/main");
                    objectWx.put("emphasis_keyword","keyword1.DATA");
                    JSONObject objectPro=new JSONObject();
                    objectPro.put("appid","wx3abf420b5d673535");
                    objectPro.put("pagepath","pages/login/main");
                    objectWx.put("miniprogram",objectPro.toString());
                    paramSend.put("mp_template_msg",objectWx.toString());
                    HttpClientUtil.doRequestPostJson("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token="+access_token, JSON.toJSONString(paramSend),null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 微信核销模板推送
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getWxOkSendInfo")
    public BaseResult getWxOkSendInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result=new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            //查询订单信息
            Dto paramsOrder=(Dto)bizService.queryForObject("order.getInfo",new BaseDto("id",dto.getAsString("id")));
            Dto member = (Dto)bizService.queryForObject("member.getInfo",new BaseDto("id",paramsOrder.getAsString("member_id")));
            //获取openid
            String openid = member.getAsString("wopenid");
            //获取access_token
            Map mapGet=new HashMap();
            mapGet.put("grant_type","client_credential");
            mapGet.put("appid","wx3abf420b5d673535");
            mapGet.put("secret","4072402735e1caed09114b8966658955");
            String s1 = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/cgi-bin/token", mapGet, null);
            if (StringUtils.isNotEmpty(s1)) {
                JSONObject jsonObjectAccess = JSONObject.fromObject(s1);
                //获取access_token
                String access_token = jsonObjectAccess.getString("access_token");
                System.out.println(s1);
                Map<String,Object> paramSend=new HashMap<>();
                paramSend.put("access_token",access_token);
                paramSend.put("touser",openid);

                if(null!=paramsOrder){
                    //keyword1
                    Map<String,Object> keyword1=new HashMap();
                    keyword1.put("value",paramsOrder.getAsString("shopName"));
                    keyword1.put("color","#173177");
                    //keyword2
                    Map<String,Object> keyword2=new HashMap();
                    keyword2.put("value",paramsOrder.getAsString("shopAddress"));
                    keyword2.put("color","#173177");
                    //keyword3
                    Map<String,Object> keyword3=new HashMap();
                    keyword3.put("value",paramsOrder.getAsString("number"));
                    keyword3.put("color","#173177");
                    //keyword4
                    Map<String,Object> keyword4=new HashMap();
                    keyword3.put("value",paramsOrder.getAsInteger("total")>1?paramsOrder.getAsString("productName")+"等":paramsOrder.getAsString("productName"));
                    keyword4.put("color","#173177");
                    //keyword5
                    Map<String,Object> keyword5=new HashMap();
                    keyword5.put("value","已完成");
                    keyword5.put("color","#173177");
                    //keyword5
                    Map<String,Object> keyword6=new HashMap();
                    keyword6.put("value",paramsOrder.getAsString("update_time"));
                    keyword6.put("color","#173177");
                    Map<String,Object> object=new HashMap();
                    object.put("page","pages/orderCode/main?order_id="+paramsOrder.getAsString("id")+"&status="+paramsOrder.getAsString("status"));
                    object.put("template_id","Zgc0zTaUstQnj8oYKlFpWs-0gljYrJns1Cm5ipBY32w");
                    Map<String,Object> objectData=new HashMap();
                    objectData.put("keyword1",keyword1);
                    objectData.put("keyword2",keyword2);
                    objectData.put("keyword3",keyword3);
                    objectData.put("keyword4",keyword4);
                    objectData.put("keyword5",keyword5);
                    objectData.put("keyword6",keyword6);
                    object.put("data",objectData);
                    if(StringUtils.isNotEmpty(redisService.getValue(openid))){
                        object.put("form_id",redisService.getValue(openid));
                        redisService.delete(openid);
                    }else{
                        object.put("form_id",paramsOrder.getAsString("form_id"));
                    }
                    object.put("emphasis_keyword","keyword4.DATA");
                    paramSend.put("weapp_template_msg",object);

                    //productType
                    JSONObject productType=new JSONObject();
                    productType.put("value","奶茶名称");
                    productType.put("color","#173177");
                    //name
                    JSONObject name=new JSONObject();
                    name.put("value","2份香飘飘奶茶大杯");
                    name.put("color","#173177");
                    //number
                    JSONObject number=new JSONObject();
                    number.put("value","2份");
                    number.put("color","#173177");
                    //expDate
                    JSONObject expDate=new JSONObject();
                    expDate.put("value","2019-08-08");
                    expDate.put("color","#173177");
                    //remark
                    JSONObject remark=new JSONObject();
                    remark.put("value","一大一小，少冰");
                    remark.put("color","#173177");
                    String datasWx=productType.toString()+","+name.toString()+","+number.toString()+","+expDate.toString()+","+remark.toString();
                    JSONObject objectWx=new JSONObject();
                    objectWx.put("appid","wx05263cbb6f4a1c26");
                    objectWx.put("template_id","SOOzzS09tnbJA3tkxHcuDYBCZM50_Q_o2l2Ums8yEH8");
                    objectWx.put("data",datasWx);
                    objectWx.put("url","pages/login/main");
                    objectWx.put("emphasis_keyword","keyword1.DATA");
                    JSONObject objectPro=new JSONObject();
                    objectPro.put("appid","wx3abf420b5d673535");
                    objectPro.put("pagepath","pages/login/main");
                    objectWx.put("miniprogram",objectPro.toString());
                    paramSend.put("mp_template_msg",objectWx.toString());
                    HttpClientUtil.doRequestPostJson("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token="+access_token, JSON.toJSONString(paramSend),null);
                    redisService.delete(openid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 收集用户form_id 存入redis
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/setUserFormId")
    public BaseResult getWxOkSe3ndInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result=new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null!=member){
                if(StringUtils.isNotEmpty(dto.getAsString("form_id"))){
                    if(!redisService.getValue(member.getAsString("wopenid")).toString().equals(dto.getAsString("form_id"))){
                        redisService.setValue(member.getAsString("wopenid"),dto.getAsString("form_id"),518400l);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 收集用户form_id 快速退款
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/orderReturnMoney")
    public BaseResult orderReturnMoney(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result=new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            //判断是支付退款还是微信退款
            //调用统一下单接口
            Dto orderPayInfo = (Dto) bizService.queryForObject("payOrderRecord.getPayOrderRecordByOrderNo", new BaseDto("order_no", dto.getAsString("order_no")));
            Map<String, String> paramOrder = new LinkedHashMap<>();
            //小程序ID
            paramOrder.put("appid", "wx703f85c99508abae");
            //商户号
            paramOrder.put("mch_id", "1517576291");
            //随机字符串
            paramOrder.put("nonce_str", String.valueOf(new Date().getTime()) + "_" + dto.getAsString("type"));
            //签名类型
            paramOrder.put("sign_type", "MD5");
            //商户订单号
            paramOrder.put("out_trade_no", orderPayInfo.getAsString("order_no"));
            //标价金额
            paramOrder.put("total_fee", orderPayInfo.getAsString("pay_amount"));
            //标价金额
            paramOrder.put("refund_fee", orderPayInfo.getAsString("pay_amount"));
            //退款单号
            paramOrder.put("out_refund_no", String.valueOf(new Date().getTime()));
            //微信订单号
            paramOrder.put("transaction_id", orderPayInfo.getAsString("pay_order_id"));
            //子商户号
            paramOrder.put("sub_mch_id", "1546327341");
            //子appid
            paramOrder.put("sub_appid", "wx3abf420b5d673535");
            //调用签名
            paramOrder = CommonUtil.formatUrlMap(paramOrder, true, true);
            String paramPay = CommonUtil.mapToxml(paramOrder);
            System.out.println(paramPay);
            String payResult = HttpClientUtil.invokeRequestWithP12("https://api.mch.weixin.qq.com/secapi/pay/refund", paramPay, true);
            System.out.println(payResult);
            Document doc = DocumentHelper.parseText(payResult);
            Element root = doc.getRootElement();
            String return_code = root.elementText("return_code");
            if (return_code.equals("SUCCESS")) {
                result.setMsg("退款成功");
            } else {
                result.setCode("9999");
                result.setMsg("退款接口异常，请稍后再试");
                result.setData("退款接口异常，请稍后再试");
                System.out.println("调用失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clientMobileLogin")
    public BaseResult clientMobileLogin(HttpServletRequest request, HttpServletResponse response) throws DocumentException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //判断验证码是否正确
            String code = redisService.getValue(dto.getAsString("telephone"));
            String clt = dto.getAsString("clt");
            if(!StringUtils.isNotEmpty(dto.getAsString("telephone"))){
                throw new Exception("手机号不能为空!");
            }
            if(StringUtils.isNotEmpty(dto.getAsString("code"))){
                if(!dto.getAsString("code").equals(redisService.getValue(dto.getAsString("telephone")))){
                    throw new Exception("验证码有误");
                }
            }else{
                throw new Exception("验证码不能为空");
            }
            //判断手机号是否存在，不存在新增用户登录
            Dto resultParam = new BaseDto();
                resultParam.put("telephone", dto.getAsString("telephone"));
            Dto paramList = (Dto)bizService.queryForDto("member.getInfo", resultParam);
            if(null!=paramList){
                //存在，直接登录
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", paramList.getAsString("id"));
                resultParam.put("username", paramList.getAsString("nickname"));
                resultParam.put("pic", paramList.getAsString("pic"));
                resultParam.put("telephone", paramList.getAsString("telephone"));
                resultParam.put("wopenid", paramList.getAsString("wopenid"));
                resultParam.put("zopenid", paramList.getAsString("zopenid"));
                resultParam.put("grade_id", paramList.getAsString("grade_id"));
                redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            }else{
                dto.put("username", dto.getAsString("telephone"));
                bizService.saveInfo("member.saveInfo",dto);
                //添加新用户优惠券
                List<Dto> addList = bizService.queryList("product.getNewAddUserInfo", new BaseDto());
                //增加优惠券
                if(null!=addList && addList.size()>0){
                    for(Dto datas:addList){
                    }
                }
                // 添加MGM优惠券
                //老带新mgm赠送
                Dto marketData = (Dto)bizService.queryForObject("marketMgm.getOldAndUserMarket", new BaseDto());
                //判断当前登录用户有没有邀请人id
                Dto userData=(Dto)bizService.queryForObject("member.getInfo",new BaseDto("id",dto.getAsString("id")));
                if(null!=userData && org.g4studio.core.resource.util.StringUtils.isNotEmpty(userData.getAsString("recommond_id"))){
                    //查询新用户优惠券信息
                    Dto couponNew=(Dto)bizService.queryForObject("marketCoupon.getInfo",new BaseDto("id",marketData.getAsString("coupon_id")));
                    if(null!=couponNew){
                    }
                }

                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", dto.getAsString("id"));
                resultParam.put("username", dto.getAsString("nickname"));
                resultParam.put("pic", dto.getAsString("pic"));
                resultParam.put("telephone", dto.getAsString("telephone"));
                resultParam.put("wopenid", dto.getAsString("wopenid"));
                resultParam.put("zopenid", dto.getAsString("zopenid"));
                resultParam.put("grade_id",1);
                redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            }
            result.setData(resultParam);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 绑定用户信息
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getUserAddInfo")
    public BaseResult getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result=new BaseResult();
        Dto resultParam = new BaseDto();
        try {
            String clt=dto.getAsString("clt");
            //获取openid
            String openid = dto.getAsString("wopenid");
            resultParam.put("wopenid", openid);
            Dto paramList = (Dto)bizService.queryForDto("member.getInfo", resultParam);
            if(null!=paramList){
                //存在，直接登录
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", paramList.getAsString("id"));
                resultParam.put("username", paramList.getAsString("username"));
                resultParam.put("pic", paramList.getAsString("pic"));
                resultParam.put("telephone", paramList.getAsString("telephone"));
                resultParam.put("wopenid", paramList.getAsString("wopenid"));
                redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            }else{
                dto.put("pic",dto.getAsString("pic"));
                dto.put("username",dto.getAsString("username"));
                bizService.saveInfo("member.saveInfo",dto);
                //存在，直接登录
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", dto.getAsString("id"));
                resultParam.put("username", dto.getAsString("username"));
                resultParam.put("pic", dto.getAsString("pic"));
                resultParam.put("telephone", dto.getAsString("telephone"));
                resultParam.put("wopenid", dto.getAsString("wopenid"));
                redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            }
            result.setData(resultParam);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
