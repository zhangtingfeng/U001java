package com.jfc.controller.sys;

import com.jfc.base.BaseController;
import com.jfc.base.debugLog;
import com.jfc.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 后台用户业务处理
 * @author mcl
 * @since 2017年6月20日15:49:06
 * @see SysController
 */
@RestController
@RequestMapping("/sys")
public class SysController extends BaseController {


	/**
	 * 查询列表
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryShopList")
	public BaseResult queryList(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		String t = request.getParameter("t");
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
			if(!member.getAsString("shop_id").equals("1")){// 管理员
				dto.put("shopId", member == null ? "" : member.get("shop_id"));
			}
			List paramList = bizService.queryForList("shop.queryList", dto);
			result.setData(paramList);
		} catch (Exception e) {
			e.printStackTrace();
			reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}
}

