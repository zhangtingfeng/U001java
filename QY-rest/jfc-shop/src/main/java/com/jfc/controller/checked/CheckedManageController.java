package com.jfc.controller.checked;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jfc.base.BaseController;
import com.jfc.base.debugLog;
import com.jfc.controller.common.CommonController;
import com.jfc.util.BaseResult;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/checked")
public class CheckedManageController extends BaseController {


    @ResponseBody
    @RequestMapping(value = "/doChecked")
    public BaseResult doChecked(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        debugLog.send("QY","收到参数dto="+request.getRequestURI(),dto);

        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode("4000");
                result.setMsg("请登录");
                return result;
            }

            String checkEdStatus = dto.getAsString("checkEdStatus");
            if (StringUtils.isEmpty(checkEdStatus)) {
                throw new Exception("审核状态为空");
            }

            String tableName = dto.getAsString("t");
            String sql = dto.getAsString("sql");
            if (StringUtils.isEmpty(sql)) {
                sql = "updateStatus";
            }

           // if (checkEdStatus.equals("0")) {
                //修改为初始化，直接修改
           // } else {
           //     //只修改待审核的数据
            //    dto.put("oldStatus", "0");
            //}


            dto.put("status", checkEdStatus);
            dto.put("tableName", tableName);
            dto.put("method", sql);
            bizService.updateInfo(tableName + "." + sql, dto);


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }

        return result;
    }
}
