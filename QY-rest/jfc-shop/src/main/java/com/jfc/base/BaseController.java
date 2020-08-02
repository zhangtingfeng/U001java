package com.jfc.base;

import com.jfc.util.BaseResult;
import com.jfc.util.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController {

    @Autowired
    public RedisService redisService;

    @Autowired
    public BaseMapper bizService;

    /**
     *
     * 交易成功提示信息
     *
     * @param pMsg
     *            提示信息
     * @param pMsg
     * @return
     * @throws IOException
     */
    protected BaseResult reduceErr(String pMsg){
        BaseResult result = new BaseResult();
        if (!StringUtils.isNotEmpty(pMsg)) {
            pMsg = "系统异常";
        }else{
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(pMsg);
            if (!m.find()) {
                pMsg = "操作异常!";
            }
        }
        result.setCode("9999");
        result.setMsg(pMsg);
        return result;
    }
    public BaseResult returnMsg(String code,String pMsg){
        BaseResult result = new BaseResult();
        if("".equals(pMsg)){
            pMsg = code;
        }
        result.setCode(code);
        result.setMsg(pMsg);
        return result;
    }
}
