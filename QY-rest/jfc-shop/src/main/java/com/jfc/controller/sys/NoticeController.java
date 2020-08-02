package com.jfc.controller.sys;

import com.jfc.base.BaseController;
import com.jfc.util.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *公告
 * @since 2017年6月20日15:49:06
 * @see NoticeController
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {


	/**
	 * 发布公告
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/editNoticeStatus")
	public BaseResult editNoticeStatus(HttpServletRequest request, HttpServletResponse response) {
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			Dto user = redisService.getObject(dto.getAsString("token"),BaseDto.class);
			String ids = dto.getAsString("ids");
			if(StringUtils.isBlank(ids)){
				throw new Exception("需要发布的公告为空");
			}
			String[] idsSp = ids.split(",");
			Dto basDto = new BaseDto();
			basDto.put("tableName","cmnNotice");
			basDto.put("status",1);
			basDto.put("updator",user.getAsString("id"));
			for (String id : idsSp) {
				basDto.put("id",id);
				bizService.update(basDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
		}
		return result;
	}
	
}