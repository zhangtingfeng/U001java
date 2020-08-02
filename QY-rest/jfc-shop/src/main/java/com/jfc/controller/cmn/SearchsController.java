package com.jfc.controller.cmn;

import com.jfc.base.BaseController;
import com.jfc.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: WuMengChang
 * @Date: 2018/10/10 18:17
 * @Description:
 */
@RestController
@RequestMapping("/searchs")
public class SearchsController extends BaseController {
    /**
     * 删除X天之前的数据
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editDayDate")
    public BaseResult editDayDate(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            dto.put("days",formatter.format( new Date(new Date().getTime()-(60*60*24*1000*dto.getAsLong("days")))));
            dto.put("method","deleteInfo");
            bizService.delete(dto);
            result.setData(dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
