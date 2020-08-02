package com.jfc.controller.cms;

import com.jfc.base.BaseController;
import com.jfc.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: WuMengChang
 * @Date: 2018/8/21 22:16
 * @Description: 轮播图
 */
@RestController
@RequestMapping("/carouselFigure")
public class CarouselFigureController extends BaseController {

    /**
     * 保存轮播图
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addCarouselFigure")
    public BaseResult addCarouselFigure(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("creator",member == null? "0" : member.get("id"));
            dto.put("tableName","carouselFigure");
            dto.put("method","saveInfo");
            if (dto.getAsString("endDate") !=null && dto.getAsString("endDate") !=""){
                dto.put("endDate",dto.getAsString("endDate")+" "+"23:59:59");
            }

            bizService.save(dto);
            if (dto.getAsInteger("config_id")==36){
                Dto adto=new BaseDto();
                adto.put("id",340);
                if (dto.getAsString("box").equals("Y")){
                    adto.put("val","Y");
                }else {
                    adto.put("val","N");
                }
                if (dto.getAsString("endDate") !=null && dto.getAsString("endDate") !=""){
                    adto.put("endDate",dto.getAsString("endDate")+" "+"23:59:59");
                }
              bizService.updateInfo("sysConfig.updateInfo",adto);
            }
            result.setData(dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
