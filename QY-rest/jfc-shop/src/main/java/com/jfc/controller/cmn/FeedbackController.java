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

/**
 * @Auther: WuMengChang
 * @Date: 2018/9/3 16:48
 * @Description: 反馈业务
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController extends BaseController {

    /**
     * 回复反馈
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/replyFeedback")
    public BaseResult addCarouselFigure(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            Dto feedback = new BaseDto();
            feedback.put("pid",dto.getAsString("pid"));
            feedback.put("content",dto.getAsString("replyContent"));
            feedback.put("username",member.getAsInteger("id"));
            feedback.put("creator",member.getAsString("id"));
            feedback.put("method","updateInfo");
            feedback.put("tableName","feedback");
            bizService.update(feedback);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
