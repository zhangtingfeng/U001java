package com.jfc.controller.user;

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
 * Created by MJ on 2018-11-28.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/invoice")
public class UserInvoiceController extends BaseController {
    


    /**
     * 用户发票抬头信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveUserInvoice")
    public BaseResult userInvoice(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            Dto info = new BaseDto();
            info.put("types",dto.getAsString("types"));
            info.put("member_id",member.getAsString("id"));
            Dto invoice = (Dto) bizService.queryForObject("memberInvoice.getInfo",info);
            dto.put("tableName","memberInvoice");
            dto.put("method","updateInfo");
            if(null==invoice){
                bizService.save(dto);
                result.setData(dto.getAsString("id"));
            }else{
                result.setData(invoice.getAsString("id"));
                dto.put("id",invoice.getAsString("id"));
                bizService.update(dto);
                info.put("types",dto.getAsInteger("types")==1?"2":"1");
                Dto invoices = (Dto) bizService.queryForObject("memberInvoice.getInfo",info);
                if(null!=invoices){
                    dto.put("method","updateDefStatus");
                    dto.put("status","96");
                    dto.put("id",invoices.getAsString("id"));
                    bizService.update(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    /**
     * 查询用户发票抬头信息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryInvoiceInfo")
    public BaseResult queryInvoiceInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("clt") + "_login_" + dto.getAsString("token"), BaseDto.class);
            if(null==member){
                result.setCode("4000");
                return result;
            }
            dto.put("member_id",member.getAsString("id"));
            Dto invoice = (Dto) bizService.queryForObject("memberInvoice.getInfo",dto);
            result.setData(invoice);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
