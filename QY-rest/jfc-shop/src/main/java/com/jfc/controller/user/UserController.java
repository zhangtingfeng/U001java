package com.jfc.controller.user;

import com.alibaba.fastjson.JSONArray;
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
import java.util.List;

/**
 * 用户信息相关controller
 *
 * @author liujie
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    


    /**
     * 反馈
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userFeedback")
    public BaseResult userFeedback(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member =  redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode("4000");
                return result;
            }
            dto.put("member_id", member.getAsString("id"));
            dto.put("creator", member.getAsString("id"));
            dto.put("tableName", "feedback");
            bizService.save(dto);
            result.setData(member);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 积分明细
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userPointsLog")
    public BaseResult userPointsLog(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member =  redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode("4000");
                return result;
            }
            dto.put("member_id", member.getAsString("id"));
            dto.put("month", "month");
            List<Dto> pointsList = bizService.queryList("memberPointsLog.queryList",dto);
            dto.put("lastMonth", "lastMonth");
            List<Dto> lastPointsList = bizService.queryList("memberPointsLog.queryList",dto);
            Dto resDto = new BaseDto();
            resDto.put("pointsList", pointsList);
            resDto.put("lastPointsList", lastPointsList);
            result.setData(resDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    /**
     * 修改密码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatePassword")
    public BaseResult updatePassword(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto userDto = new BaseDto("password", dto.getAsString("oldpwd"));
            Dto member = (BaseDto) bizService.queryForObject("member.getInfo", userDto);
            if (member == null) {
                result.setCode("4000");
                return result;
            }
            userDto.put("password", dto.getAsString("newpwd"));
            userDto.put("id", member.getAsString("id"));
            userDto.put("tableName", "member");
            bizService.update(userDto);
            redisService.setValue(dto.getAsString("loginChannel") + "_" + dto.getAsString("token"), JSONArray.toJSONString(member), 18000l);
            result.setData(member);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 快速登录和找回密码验证
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/fastlogin")
    public BaseResult fastlogin(HttpServletRequest request, HttpServletResponse response) {
        // 根据手机号码查询是否存在，不存在就注册，
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String visitToken = redisService.getValue(dto.getAsString("loginChannel") + dto.getAsString("visitToken"));
            visitToken = "1111";
            // 获取表单手机验证码
            String phoneCode = dto.getAsString("validCode");
            if (phoneCode.length() == 0 || phoneCode == "" || !phoneCode.equals(visitToken)) {
                // 手机验证码错误
                throw new Exception("手机验证码错误");
            } else {
                // 注册前先查询输入的手机号码是否存在
                Dto dtoInfo = new BaseDto();
                dtoInfo.put("telephone", dto.getAsString("telephone"));
                Dto member = (BaseDto) (Dto) bizService.queryForObject("member.getInfo", dtoInfo);
                if (member == null) {
                    // 失败
                    throw new Exception("手机号码不存在");
                }
                member.put("validCode", phoneCode);
                result.setData(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 返回当前用户信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/member")
    public BaseResult meber(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            Dto user = (BaseDto)bizService.queryForObject("member.getInfo",new BaseDto("id",member.getAsString("id")));
            if(null!=user && StringUtils.isNotEmpty(user.getAsString("telephone"))){
                String telephone =  user.getAsString("telephone");
                String telephone1  = telephone.substring(0,3);
                String telephone2  = telephone.substring(telephone.length()-4);
                user.put("mobile",telephone1+"****"+telephone2);
            }
            if(null==user){
                result.setCode("4000");
                return result;
            }
            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 返回当前用户升级余下名额
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/memberGrade")
    public BaseResult memberGrade(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            Dto user = (BaseDto)bizService.queryForObject("member.getMemberGrade",new BaseDto("id",member.getAsString("id")));
            if(null!=user && StringUtils.isNotEmpty(user.getAsString("telephone"))){
                String telephone =  user.getAsString("telephone");
                String telephone1  = telephone.substring(0,3);
                String telephone2  = telephone.substring(telephone.length()-4);
                user.put("mobile",telephone1+"****"+telephone2);
            }
            if(null==user){
                result.setCode("4000");
                return result;
            }
            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 个人中心我的收藏
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/memberCollect")
    public BaseResult memberCollect(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            List<Dto> user = bizService.queryForList("memberCollect.queryList", new BaseDto("member_id", member.getAsString("id")));
            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 个人中心我的店铺收藏
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryShopCollect")
    public BaseResult queryShopCollect(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            List<Dto> user = bizService.queryForList("memberCollect.queryShopList", new BaseDto("member_id", member.getAsString("id")));
            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 个人中心删除收藏
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/removeCollect")
    public BaseResult removeCollect(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            Dto inDto = new BaseDto();
            inDto.put("ids", member.getAsString("id")+",");
            inDto.put("tableName", "memberCollect");
            bizService.delete(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 个人中心收藏商品加入购物车
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveCartMember")
    public BaseResult saveCartMember(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            dto.put("member_id", member.getAsString("id"));
            dto.put("product_num", dto.getAsString("product_num"));
            dto.put("product_money", dto.getAsString("price"));
            dto.put("tableName", "memberCart");
            bizService.save(dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 会员注册
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/regist")
    public BaseResult regist(HttpServletRequest request, HttpServletResponse response) {
        // 根据手机号码查询是否存在，不存在就注册，
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        String data = null;
        String visitToken = redisService.getValue(dto.getAsString("loginChannel") + dto.getAsString("visitToken"));
        visitToken = "1111";
        // 获取表单手机验证码
        String phoneCode = dto.getAsString("code");
        try {
            if (phoneCode.length() == 0 || phoneCode == "" || !phoneCode.equals(visitToken)) {
                // 手机验证码错误
                data = "3";
            } else {
                // 注册前先查询输入的手机号码是否存在
                Dto dtoInfo = new BaseDto();
                dtoInfo.put("telephone", dto.getAsString("telephone"));
                Dto member = (BaseDto) (Dto) bizService.queryForObject("member.getInfo", dtoInfo);
                if (member != null) {
                    // 失败
                    data = "2";
                } else {
                    // 成功
                    dto.put("status", "0");
                    dto.put("tableName", "member");
                    bizService.save(dto);
                    data = "1";
                }
            }
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 根据旧密码修改密码
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/modifypwd")
    public BaseResult modifypwd(HttpServletRequest request, HttpServletResponse response) {
        //1.根据登录人token获取用户信息
        // 2.通过用户id和旧密码（）去数据库查询取得id
        // 3.不匹配，抛出异常
        // 4.如果匹配，修改密码
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("loginChannel") + "_" + dto.getAsString("token"), BaseDto.class);
            if (member != null) {
                dto.put("id", member.getAsString("id"));
            }
            // 1.拿取到手机号码
            dto.put("password", dto.getAsString("odlpwd"));
            // 2.通过手机号码和旧密码（）去数据库查询取得id
            Dto user = (BaseDto) (Dto) bizService.queryForObject("member.getInfo", dto);
            if (user == null) {
                // 3.不匹配，抛出异常
                throw new Exception("旧密码输入错误！");
            }
            dto.put("id", user.getAsString("id"));
            dto.put("password", dto.getAsString("newpwdone"));
            dto.put("tableName", "member");
            // 4.如果匹配，修改密码
            bizService.update(dto);
            result.setData("1");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 重置密码
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/resetpassword")
    public BaseResult resetpassword(HttpServletRequest request, HttpServletResponse response) {
        // 1.拿取表单传递的验证码和Redis里面的验证码作对比
        // 2.如果传递的验证码为空或者与Redis里面的不一致，抛出异常
        // 3.一致，拿到表单传递的手机号码，去数据库校验是否存在
        // 4.如果不存在，抛出异常
        // 5.如果存在，进入下一步
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        String visitToken = redisService.getValue(dto.getAsString("loginChannel") + dto.getAsString("visitToken"));
        visitToken = "1111";
        // 获取表单手机验证码
        String phoneCode = dto.getAsString("validCode");
        try {
            if (phoneCode.length() == 0 || phoneCode == "" || !phoneCode.equals(visitToken)) {
                // 手机验证码错误
                throw new Exception("手机验证码错误");
            }
            // 查询手机号码是否存在
            Dto dtoInfo = new BaseDto();
            dtoInfo.put("telephone", dto.getAsString("telephone"));
            Dto member = (BaseDto) (Dto) bizService.queryForObject("member.getInfo", dtoInfo);
            if (member == null) {
                // 失败
                throw new Exception("手机号码不存在");
            }
            dto.put("id", member.getAsString("id"));
            dto.put("tableName", "member");
            bizService.update(dto);
            result.setData("1");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 安全中心，修改手机号码
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/changeTelephone")
    public BaseResult changeTelephone(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        String visitToken = redisService.getValue(dto.getAsString("loginChannel") + dto.getAsString("visitToken"));
        visitToken = "1111";
        // 获取表单手机验证码
        String phoneCode = dto.getAsString("validCode");
        try {
            if (phoneCode.length() == 0 || phoneCode == "" || !phoneCode.equals(visitToken)) {
                // 手机验证码错误
                throw new Exception("手机验证码错误");
            }

            Dto member = redisService.getObject(dto.getAsString("loginChannel") + "_" + dto.getAsString("token"), BaseDto.class);
            if (member == null) {
                throw new Exception("登录人信息有误！");
            }

            // 查询登录人是否存在
            Dto dtoInfo = new BaseDto();
            dtoInfo.put("telephone", member.getAsString("telephone"));
            Dto user = (BaseDto) (Dto) bizService.queryForObject("member.getInfo", dtoInfo);
            if (user == null) {
                throw new Exception("用户不存在");
            }
            dtoInfo.put("telephone", dto.getAsString("telephone"));
            Dto result2 = (BaseDto) (Dto) bizService.queryForObject("member.getInfo", dtoInfo);
            if (result2 != null) {
                throw new Exception("手机号码已存在，请更换手机号码！");
            }
            dto.put("id", user.getAsString("id"));
            dto.put("tableName", "member");
            bizService.update(dto);
            result.setData("1");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 忘记密码
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/editPwd")
    public BaseResult editPwd(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto dtoInfo = (Dto) bizService.queryForObject("member.getInfo", dto);
            if (dtoInfo != null) {
                dto.put("id", dtoInfo.getAsString("id"));
                dto.put("tableName", "member");
                bizService.update(dto);
            }
            result.setData("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


}
