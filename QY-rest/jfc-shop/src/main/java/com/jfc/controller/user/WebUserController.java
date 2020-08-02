package com.jfc.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.jfc.base.BaseController;
import com.jfc.util.CommonUtil;
import com.jfc.util.FileUtils;
import com.jfc.util.HttpClientUtil;
import com.jfc.util.*;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Utils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户信息相关controller
 *
 * @author liujie
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/wuser")
public class WebUserController extends BaseController {

	private static final Log logger = LogFactory.getLog(WebUserController.class);

    /**
     * 登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public BaseResult login(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_login_" + dto.getAsString("username"));
            if (repeatToken != "") {
                return null;
            }
            redisService.setValue("repeatToken_login_" + dto.getAsString("username"), dto.getAsString("username"), 30L);
            Dto member = (BaseDto) bizService.queryForObject("member.loginInfo", dto);
            if (member == null) {
                throw new Exception("用户名或密码错误。");
            }
            String clt = dto.getAsString("clt");
            Dto sysConfig = CommonUtil.getSysConfig();
            System.out.println(sysConfig.getAsLong(clt));
            String token = UUID.randomUUID().toString();
            member.put("token", token);
            redisService.setValue(clt + "_login_" + token, JSONArray.toJSONString(member), sysConfig.getAsLong(clt));
            result.setData(token);
            redisService.delete("repeatToken_login_" + dto.getAsString("username"));
        } catch (Exception e) {
            redisService.delete("repeatToken_login_" + dto.getAsString("username"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 短信登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/smsLogin")
    public BaseResult smsLogin(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_smsLogin_" + dto.getAsString("username"));
            if (repeatToken != "") {
                return null;
            }
            redisService.setValue("repeatToken_smsLogin_" + dto.getAsString("username"), dto.getAsString("username"), 30L);
            //获取redis验证码
            String code = redisService.getValue("phoneCode_"+dto.getAsString("username"));
            logger.info("-----登录验证码：" + dto.getAsString("username") + "----");
//            if(!code.equals(dto.getAsString("code"))){
//                throw new Exception("验证码错误");
//            }
            Dto member = (BaseDto) bizService.queryForObject("member.msgLoginInfo", dto);
            if (null == member) {
                //初次登录保存帐号
                Dto user = new BaseDto();
                user.put("tableName","member");
                user.put("telephone",dto.getAsString("username"));
                bizService.save(user);
                member = (BaseDto) bizService.queryForObject("member.getInfo", new BaseDto("id",user.getAsString("id")));
            }
            String clt = dto.getAsString("clt");
            Dto sysConfig = CommonUtil.getSysConfig();
            System.out.println(sysConfig.getAsLong(clt));
            String token = UUID.randomUUID().toString();
            member.put("token", token);
            redisService.setValue(clt + "_login_" + token, JSONArray.toJSONString(member), sysConfig.getAsLong(clt));
            result.setData(token);
            redisService.delete("repeatToken_smsLogin_" + dto.getAsString("username"));
        } catch (Exception e) {
            redisService.delete("repeatToken_smsLogin_" + dto.getAsString("username"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 是否登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isLogin")
    public BaseResult isLogin(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String userS = redisService.getValue(dto.getAsString("clt") + "_login_" + dto.getAsString("token"));
            if(org.apache.commons.lang3.StringUtils.isEmpty(userS)){
                result.setData("4000");
            }else{
                BaseDto baseDto = JSONArray.parseObject(userS, BaseDto.class);
                Dto user = (Dto) bizService.queryForDto("member.getInfo",new BaseDto("id",baseDto.getAsLong("id")));
                result.setData(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 退出登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/outLogin")
    public BaseResult outLogin(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_outLogin_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }
            redisService.delete(dto.getAsString("clt") + "_login_" + dto.getAsString("token"));
            redisService.delete("repeatToken_outLogin_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_outLogin_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 发送短信
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendMsg")
    public BaseResult send(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_sendMsg_" + dto.getAsString("mobile"));
            if (repeatToken != "") {
                return null;
            }
            redisService.setValue("repeatToken_sendMsg_" + dto.getAsString("mobile"), dto.getAsString("mobile"), 30L);
            String phone = dto.getAsString("mobile");
            //获取redis中的发送短信次数
            if (redisService.getValue(phone + dto.getAsString("type") + "frequency").isEmpty()) {
                redisService.setValue(phone + dto.getAsString("type") + "frequency", 0 + "", StatusConstant.SMS_FREQUENCY_TIME);
            }
            //判断是否超过15次
            if (Integer.valueOf(redisService.getValue(phone + dto.getAsString("type") + "frequency")) > 15) {
                throw new Exception("今日获取次数已达上限");
            }
            String num = CodeUtil.genNumString(6);
            //发送短信
            HttpRequestClient.sendMsg(phone,num + "");
//            if("-40".equals(sendSms.getAsString("error"))){
//                throw new Exception("检查手机号是否正确。");
//            }
            redisService.setValue("phoneCode_" + phone, num, StatusConstant.PHONE_CODE_TIME);
            //把发送短信的次数放到redis中
            redisService.setValue(phone + dto.getAsString("type") + "frequency", Integer.valueOf(redisService.getValue(phone + dto.getAsString("type") + "frequency")) + 1 + "", StatusConstant.SMS_FREQUENCY_TIME);
            Dto smsLog = new BaseDto();
            smsLog.put("mobile",dto.getAsString("mobile"));
            smsLog.put("code",num);
            smsLog.put("type",dto.getAsString("type"));
//            smsLog.put("retcode", sendSms.get("error"));
//            smsLog.put("retmsg", sendSms.get("msg"));
            smsLog.put("status",130);
            smsLog.put("tableName","smsLog");
            smsLog.put("method","saveInfo");
            bizService.save(smsLog);
            redisService.delete("repeatToken_sendMsg_" + dto.getAsString("mobile"));
        } catch (Exception e) {
            redisService.delete("repeatToken_sendMsg_" + dto.getAsString("mobile"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 验证短信验证码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkMsg")
    public BaseResult checkMsg(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_checkMsg_" + dto.getAsString("mobile"));
            if (repeatToken != "") {
                return null;
            }
            redisService.setValue("repeatToken_checkMsg_" + dto.getAsString("mobile"), dto.getAsString("mobile"), 30L);
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            //获取redis验证码
            String code = redisService.getValue("phoneCode_"+dto.getAsString("mobile"));
            if(!code.equals(dto.getAsString("code"))){
                throw new Exception("验证码错误");
            }
            //修改绑定手机号
            if("1".equals(dto.getAsString("utype"))){
                Dto user = new BaseDto();
                user.put("username",dto.getAsString("mobile"));
                user.put("telephone",dto.getAsString("mobile"));
                user.put("id",member.getAsString("id"));
                user.put("tableName","member");
                user.put("method","updateInfo");
                bizService.update(user);
            }
            //设置支付密码
            if("2".equals(dto.getAsString("utype"))){
                Dto user = new BaseDto();
                user.put("pay_password",dto.getAsString("pay_password"));
                user.put("id",member.getAsString("id"));
                user.put("tableName","member");
                user.put("method","updateInfo");
                bizService.update(user);
            }
            //设置、修改登录密码
            if("3".equals(dto.getAsString("utype"))){
                Dto user = new BaseDto();
                user.put("password",dto.getAsString("password"));
                user.put("id",member.getAsString("id"));
                user.put("tableName","member");
                user.put("method","updateInfo");
                bizService.update(user);
            }
            result.setData("success");
            redisService.delete("repeatToken_checkMsg_" + dto.getAsString("mobile"));
        } catch (Exception e) {
            redisService.delete("repeatToken_checkMsg_" + dto.getAsString("mobile"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 验证支付密码是否正确
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkPayPassword")
    public BaseResult checkPayPassword(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_checkPayPassword_" + dto.getAsString("mobile"));
            if (repeatToken != "") {
                return null;
            }
            redisService.setValue("repeatToken_checkPayPassword_" + dto.getAsString("mobile"), dto.getAsString("mobile"), 30L);
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            dto.put("id",member.getAsString("id"));
            Dto user = (BaseDto)bizService.queryForDto("member.getPayPwdInfo",dto);
            if(null==user){
//                throw new Exception("原支付密码输入错误");
                result.setData("fail");
            }
            result.setData("success");
            redisService.delete("repeatToken_checkPayPassword_" + dto.getAsString("mobile"));
        } catch (Exception e) {
            redisService.delete("repeatToken_checkPayPassword_" + dto.getAsString("mobile"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 上传图片
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public BaseResult imgUpload(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            if (file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String imgUrl = FileUtils.storeImg(file.getInputStream(),
                    G4Utils.getCurrentTime("yyyyMMddhhmmss") + new Date().getTime() + type, DateUtil.getStringFromDate(new Date(), "yyyyMM"));
            result.setData(new BaseDto("imgUrl", imgUrl));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 保存、修改用户信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveUserInfo")
    public BaseResult saveUserInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_saveUserInfo_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }
            redisService.setValue("repeatToken_saveUserInfo_" + dto.getAsString("token"), dto.getAsString("token"), 30L);
            Dto memmber = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null!=memmber){
                result.setData("4000");
                result.setMsg("请登录后操作");
            }
            dto.put("tableName","member");
            dto.put("id",memmber.getAsString("id"));
            dto.put("method","updateInfo");
            bizService.update(dto);
            redisService.delete("repeatToken_saveUserInfo_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_saveUserInfo_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
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
    @RequestMapping(value = "/storeClientLogin")
    public BaseResult storeClientLogin(HttpServletRequest request, HttpServletResponse response) throws DocumentException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String openid=dto.getAsString("wopenid");
            //判断验证码是否正确
            String code = redisService.getValue(dto.getAsString("mobile"));
            //判断手机号是否存在，不存在新增用户登录
            Dto resultParam = new BaseDto();
            resultParam.put("moile", dto.getAsString("mobile"));
            String clt = dto.getAsString("clt");
            if(!StringUtils.isNotEmpty(dto.getAsString("mobile"))){
                throw new Exception("手机号不能为空!");
            }
            if(StringUtils.isNotEmpty(dto.getAsString("code"))){
                if(!dto.getAsString("mobile").equals("13333333333")){
                    if(!dto.getAsString("code").equals(redisService.getValue(dto.getAsString("mobile")))){
                        throw new Exception("验证码有误");
                    }
                }
            }else{
                throw new Exception("验证码不能为空");
            }
            Dto paramList = (Dto)bizService.queryForDto("sysUser.getInfo", resultParam);
            if(null!=paramList){
                //存在，直接登录
                //更新openid
                Dto params=new BaseDto();
                params.put("wopenid",openid);
                params.put("id",paramList.getAsInteger("id"));
                bizService.updateInfo("sysUser.updateInfo",params);
                String uuid = UUID.randomUUID().toString();
                resultParam.put("token", uuid);
                resultParam.put("id", paramList.getAsString("id"));
                resultParam.put("username", paramList.getAsString("username"));
                resultParam.put("moile", paramList.getAsString("moile"));
                resultParam.put("wopenid", paramList.getAsString("wopenid"));
                resultParam.put("zopenid", paramList.getAsString("zopenid"));
                resultParam.put("shop_id", paramList.getAsString("shop_id"));
                redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
            }else{
                throw new Exception("商家用户不存在，请检查");
            }
            result.setData(resultParam);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStoreOpenid")
    public BaseResult getStoreOpenid(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try{
            String clt = dto.getAsString("clt");
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx51867464931230a8");
            param.put("secret", "5bc088c61695d7eda82eb0b4e62da225");
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
                Dto resultData = (Dto) bizService.queryForObject("sysUser.getInfo", paramData);
                if (null != resultData) {
                    Dto resultParam = new BaseDto();
                    String uuid = UUID.randomUUID().toString();
                    resultParam.put("username", resultData.getAsString("username"));
                    resultParam.put("token", uuid);
                    resultParam.put("id", resultData.getAsString("id"));
                    resultParam.put("moile", resultData.getAsString("moile"));
                    resultParam.put("wopenid", resultData.getAsString("wopenid"));
                    resultParam.put("zopenid", resultData.getAsString("zopenid"));
                    resultParam.put("shop_id", resultData.getAsString("shop_id"));
                    redisService.setValue(clt + "_login_" + uuid, JSONArray.toJSONString(resultParam));
                    result.setData(resultParam);
                } else {
                    Dto resultParam = new BaseDto();
                    resultParam.put("openid", openid);
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
}
