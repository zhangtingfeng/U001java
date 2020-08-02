package com.jfc.controller.pro;

import com.jfc.base.BaseController;
import com.jfc.util.BaseResult;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: WuMengChang
 * @Date: 2018/9/21 16:51
 * @Description: 产品分类
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/productBrand")
public class ProductBrandController extends BaseController {
    /**
     * 获取当前店铺所有商品分类
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryListBrand")
    public BaseResult queryListBrand(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List paramList = bizService.queryForList("productBrand.queryListBrand", dto);
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
