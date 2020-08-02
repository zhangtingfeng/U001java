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
@RequestMapping("/city")
public class CityController extends BaseController {


    /**
     * queryUserAddress
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryProvinCity")
    public BaseResult queryProvinCity(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> addressList = bizService.queryForList("sysProvinceCity.queryList",dto);
            result.setData(addressList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    @ResponseBody
    @RequestMapping(value = "/editUserCity")
    public BaseResult saveProvinCity(HttpServletRequest request, HttpServletResponse response) {
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
            Dto addr = (BaseDto)bizService.queryForDto("memberAddress.getInfo",new BaseDto("member_id",member==null?0:member.getAsString("id")));
            if(StringUtils.isEmpty(dto.getAsString("id"))){
                if(null==addr){
                    dto.put("status","95");
                }else{
                    dto.put("status","96");
                }
                //新增
                dto.put("creator",member.getAsInteger("id"));
                dto.put("updator",member.getAsInteger("id"));
                bizService.saveInfo("memberAddress.saveInfo",dto);
            }else{
                dto.put("updator",member.getAsInteger("id"));
                bizService.updateInfo("memberAddress.updateInfo",dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

}
