package com.jfc.service.impl;

import com.jfc.base.BaseMapper;
import com.jfc.service.ProductService;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wj
 * @Date: 2018/8/22 12:04
 * @Description: 产品删除
 */

@Service("productServices")
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    public BaseMapper bizService;

  /*  @Override
    public Integer deleteInfo(Dto inDto) {
        return null;
    }*/

  @Override
    public Integer deleteInfo(Dto dto) {
        //删除产品类先判断该产品类型在产品表中有没有记录
        if ("productType".equals(dto.getAsString("tableName"))) {
            int count = (Integer) bizService.queryForObject("product.queryListCount", new BaseDto("typeid", dto.getAsString("ids")));
            //没有记录删除
            if(count == 0){
                bizService.delete("productType.deleteInfo",dto);
            }else{
                return -1;
            }
        }else if("productProperty".equals(dto.getAsString("tableName"))){
            int count = (Integer) bizService.queryForObject("product.queryListCount", new BaseDto("propertyid", dto.getAsString("ids")));
            //没有记录删除
            if(count == 0){
                bizService.delete("productProperty.deleteInfo",dto);
            }else{
                return -2;
            }
        }else if("productBrand".equals(dto.getAsString("tableName"))){
            int count = (Integer) bizService.queryForObject("product.queryListCount", new BaseDto("brandid", dto.getAsString("ids")));
            //没有记录删除
            if(count == 0){
                bizService.delete("productBrand.deleteInfo",dto);
            }else{
                return -3;
            }
        }
        return 0;
    }
}
