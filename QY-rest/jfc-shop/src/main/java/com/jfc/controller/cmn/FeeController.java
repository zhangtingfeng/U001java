package com.jfc.controller.cmn;

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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: WuMengChang
 * @Date: 2018/9/3 16:48
 * @Description: 反馈业务
 */
@RestController
@RequestMapping("/fee")
public class FeeController extends BaseController {

    /**
     *\
     * 查询运费
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryFeeOrderList")
    public BaseResult queryFeeOrderList(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        Map<String,BigDecimal> returnMap = new HashMap<>();
        try {
            Dto memberAddress = (Dto) bizService.queryForDto("memberAddress.getInfo",new BaseDto("id",dto.getAsString("addressId")));
            Dto addressType = (Dto) bizService.queryForDto("SysFeeProvince.getInfo",new BaseDto("province_id",memberAddress.getAsString("province_id")));
            if("Y".equals(dto.getAsString("flag"))){
                //购物车
                List<Dto> proDto = bizService.queryForList("cmnFee.getCartList", new BaseDto("ids", dto.getAsString("ids")));
                for (Dto cartDto : proDto){
                    if (returnMap.get(cartDto.getAsString("shop_id")) != null){
                        returnMap.put(cartDto.getAsString("shop_id"),returnMap.get(cartDto.getAsString("shop_id")).add(cartDto.getAsBigDecimal("product_money")));
                    }else{
                        returnMap.put(cartDto.getAsString("shop_id"),cartDto.getAsBigDecimal("product_money"));
                    }
                }
            }else{
                //立即购买
                Dto proDto = (Dto) bizService.queryForDto("cmnFee.getProInfo", new BaseDto("ids", dto.getAsString("ids")));
                returnMap.put(proDto.getAsString("shop_id"),proDto.getAsBigDecimal("price").multiply(new BigDecimal(dto.getAsInteger("num"))));
            }

            //TODO 迭代 查询所有运费
            BaseDto baseDto = new BaseDto();
            baseDto.put("id",addressType.getAsString("fee_type_id"));
//            baseDto.put("shop_id",dto.getAsString("id"));
            Dto fee = (Dto) bizService.queryForDto("cmnFee.queryFeeOrderInfo",baseDto);
            result.setData(fee);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
