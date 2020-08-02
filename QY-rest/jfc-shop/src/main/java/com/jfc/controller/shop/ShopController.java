package com.jfc.controller.shop;

import com.jfc.base.BaseController;
import com.jfc.util.CommonUtil;
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
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mloong on 2018-09-26.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/shop")
public class ShopController extends BaseController {

    /**
     * 保存信息
     *
     * @param
     * @returnlIST
     */
    @ResponseBody
    @RequestMapping(value = "/setShwoRoomShop")
    public BaseResult setShwoRoomShop(HttpServletRequest request, HttpServletResponse response) {
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
            bizService.updateInfo("showroomShop.saveInfoBySelect",inDto);

            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
