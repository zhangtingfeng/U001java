package com.jfc.controller.user;

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
import java.util.List;

/**
 * 用户信息相关controller
 *
 * @author liujie
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/userAddress")
public class UserAddressController extends BaseController {

    /**
     * queryUserAddress
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryUserAddressInfo")
    public BaseResult queryUserAddressInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
//            String userS = redisService.getValue(dto.getAsString("clt")+"_login_"+dto.getAsString("token"));
//            if(StringUtils.isEmpty(userS)){
//                result.setCode("4000");
//                result.setMsg("请登录后操作");
//                return result;
//            }
           Dto addressDto = (Dto) bizService.queryForDto("memberAddress.getInfo",new BaseDto("id",dto.getAsString("id")));

            result.setData(addressDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    /**
     * 查询用户收货地址
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryUserAddress")
    public BaseResult queryUserAddress(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto userS = redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if(null == userS){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            List<Dto> addressList = bizService.queryForList("memberAddress.queryList",new BaseDto("member_id",userS.getAsString("id")));
            result.setData(addressList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    /**
     * 删除用户收货地址
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteAddress")
    public BaseResult deleteAddress(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto userS = redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if(null == userS){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            dto.put("tableName","memberAddress");
            dto.put("method","deleteInfo");
            bizService.delete(dto);
            result.setData("success");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 设置默认收货地址
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/siteDefaultAddress")
    public BaseResult siteDefaultAddress(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if(null == member){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            dto.put("tableName","memberAddress");
            dto.put("method","updateInfo");
            dto.put("memberid",member.getAsString("id"));
            String addrId = dto.getAsString("id");
            List<Dto> addressList = bizService.queryForList("memberAddress.queryList",new BaseDto("member_id",member.getAsString("id")));
            for (Dto address:addressList){
                if(addrId.equals(address.getAsString("id"))){
                    dto.put("status","95");
                    bizService.update(dto);
                }else{
                    Dto user = new BaseDto();
                    user.put("status","96");
                    user.put("tableName","memberAddress");
                    user.put("method","updateInfo");
                    user.put("id",address.getAsString("id"));
                    bizService.update(user);
                }
            }
            result.setData("success");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 新增/修改收货地址
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveAddress")
    public BaseResult saveAddress(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if(null == member){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            if("95".equals(dto.getAsString("status"))){
                Dto dftDto = new BaseDto();
                dftDto.put("status","95");
                dftDto.put("member_id",member.getAsString("id"));
                Dto address = (BaseDto)bizService.queryForDto("memberAddress.queryList",dftDto);
                if(null !=address){
                    dftDto.put("tableName","memberAddress");
                    dftDto.put("status","96");
                    dftDto.put("id",address.getAsString("id"));
                    dftDto.put("method","updateInfo");
                    bizService.update(dftDto);
                }
            }
            List<Dto> addressList = bizService.queryForList("memberAddress.queryList",new BaseDto("member_id",member.getAsString("id")));
            if(addressList.size()==0){
                dto.put("status","95");
            }
            dto.put("tableName","memberAddress");
            dto.put("member_id",member.getAsString("id"));
            dto.put("creator",member.getAsString("id"));
            dto.put("updator",member.getAsString("id"));
            if(StringUtils.isNotEmpty(dto.getAsString("id"))){//修改
                dto.put("method","updateInfo");
                bizService.update(dto);
            }else{//新增
                dto.put("method","saveInfo");
                bizService.save(dto);
            }

            result.setData("success");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 地址回显信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryEditAddressInfo")
    public BaseResult queryEditAddressInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if(null == member){
                result.setCode("4000");
                result.setMsg("请登录后操作");
                return result;
            }
            Dto address = (Dto) bizService.queryForDto("memberAddress.getInfo",new BaseDto("id",dto.getAsString("id")));
            List<Dto> provinceList = bizService.queryForList("sysProvinceCity.queryList",new BaseDto("pid","0"));
            List<Dto> cityList = bizService.queryForList("sysProvinceCity.queryList",new BaseDto("pid",address.getAsString("province_id")));
            List<Dto> countryList = bizService.queryForList("sysProvinceCity.queryList",new BaseDto("pid",address.getAsString("city_id")));
            address.put("provinceList",provinceList);
            address.put("cityList",cityList);
            address.put("countryList",countryList);
            result.setData(address);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询登录用户的默认地址
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/proUserAddrInfo")
    public BaseResult proUserAddrInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto userS = redisService.getObject(dto.getAsString("clt")+"_login_"+dto.getAsString("token"), BaseDto.class);
            if(null == userS){
                result.setCode("0000");
                result.setData("");
                return result;
            }
            Dto parm = new BaseDto();
            parm.putAll(dto);
            parm.put("member_id",userS.getAsString("id"));
            Dto addressDto = (Dto) bizService.queryForDto("memberAddress.getInfo",parm);
            result.setData(addressDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
