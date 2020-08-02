package com.jfc.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.jfc.base.BaseController;
import com.jfc.base.debugLog;
import com.jfc.util.BaseResult;
import com.jfc.util.GraphicHelper;
import com.jfc.util.StatusConstant;
import com.sun.org.apache.xpath.internal.objects.XObject;
import io.micrometer.core.instrument.util.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.jfc.util.CommonUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * 后台用户业务处理
 *
 * @author mcl
 * @since 2017年6月20日15:49:06
 * @see SysUserController
 */
@RestController
@RequestMapping("/suser")
public class SysUserController extends BaseController {


	/**
	 * 登录
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login")
	public BaseResult login(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ParseException {
		Dto dto = WebUtils.getParamAsDto(request);
		debugLog.send("QY","dto="+request.getRequestURI(),dto);

		BaseResult result = new BaseResult();
		try {
			if(!redisService.getValue(dto.getAsString("vtoken") + "_IMAGE_CODE").toString().equalsIgnoreCase(dto.getAsString("verifyCode"))){
				throw new Exception("验证码错误。");
			}
			//String strPass=  password("123456");

			Dto member = (BaseDto) bizService.queryForObject(
					"sysUser.loginByAccount", dto);
			debugLog.send("QY","member="+request.getRequestURI(),member);
			if (member != null && member.getAsLong("id") != null) {
				if(member.getAsString("status").equals("0")){
					throw new Exception("用户已离职，帐号失效");
				}
				String token = UUID.randomUUID().toString();
				String loginChannel = dto.getAsString("loginChannel");
				Dto sysConfig = CommonUtil.getSysConfig();
				redisService.setValue(token, JSONArray.toJSONString(member),
						sysConfig.getAsLong(loginChannel));

				Dto chatMap = new BaseDto();
				chatMap.put("userId", member.get("id"));
				chatMap.put("username", member.get("username"));
				chatMap.put("portraitUri", member.get("pic"));
				chatMap.put("chat_token", member.get("token"));
				chatMap.put("rytoken", member.get("rytoken"));
				member.put("token", token);
				member.put("chatUser", chatMap);
				member.put("isAuth", true);
				member.put("result", "图形验证码正确");


				//查詢商鋪信息
				String shop_id = member.getAsString("shop_id");
				member.put("login_shop_id",shop_id);

				result.setData(member);
			} else {
				throw new Exception("用户名或密码错误。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 用户编辑
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editUserInfo")
	public BaseResult editUserInfo(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException, ParseException {
		Dto inDto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		inDto.put("tableName", inDto.getAsString("t"));
		try {
			Dto repeatUser = (BaseDto) bizService.queryForDto("sysUser.getInfo",new BaseDto("account", inDto.getAsString("account")));//登录名
			if(inDto.getAsLong("id") != null){// 修改
				if (repeatUser != null && !repeatUser.getAsString("id").equals(inDto.getAsString("id"))) {
					throw new Exception("您输入的登录账号已经存在，请重新输入。");
				}
				Dto repeatWN = (BaseDto) bizService.queryForDto("sysUser.getInfo",new BaseDto("number1", inDto.getAsString("number")));//工号
				if (repeatWN != null && !repeatWN.getAsString("id").equals(inDto.getAsString("id"))) {
					throw new Exception("您输入的工号已经存在，请重新输入。");
				}
			}else{// 新增
				if (repeatUser != null) {
					throw new Exception("您输入的登录账号已经存在，请重新输入。");
				}
				Dto repeatWN = (BaseDto) bizService.queryForDto("sysUser.getInfo",new BaseDto("number", inDto.getAsString("number")));//工号
				if (repeatWN != null) {
					throw new Exception("您输入的工号已经存在，请重新输入。");
				}
			}

			Dto member = redisService.getObject(inDto.getAsString("token"),BaseDto.class);
//			inDto.put("creator", member == null ? "" : member.get("id"));
			if (inDto.getAsLong("id") != null) {// 修改
				// 获取上级id
				if(StringUtils.isNotEmpty(inDto.getAsString("numbername"))){
					String number = inDto.getAsString("numbername").split("-")[0];
					Dto pmember = (BaseDto)bizService.queryForObject("sysUser.getInfo", new BaseDto("number",number));
					if(pmember == null){
						throw new Exception("您输入的汇报上级工号不存在，请重新输入。");
					}
					if(pmember.getAsString("id").equals(inDto.getAsString("id"))){
						throw new Exception("汇报上级不能是自己。");
					}
					if(pmember.getAsString("pid").equals(inDto.getAsString("id"))){
						throw new Exception("汇报上级不能是自己下级。");
					}
					Dto proUser = (BaseDto) bizService.queryForDto("sysUser.getInfo",new BaseDto("id", inDto.getAsString("id")));
					if(proUser.getAsString("password").equals(inDto.getAsString("password"))){// 如果相等 password未修改
						inDto.put("password", null);
					}
					inDto.put("pid", pmember.get("id"));
				}
				bizService.update(inDto);
			}else {// 新增
				// 获取上级id
				if(StringUtils.isNotEmpty(inDto.getAsString("numbername"))){
					String number = inDto.getAsString("numbername").split("-")[0];
					Dto pmember = (BaseDto)bizService.queryForObject("sysUser.getInfo", new BaseDto("number",number));
					if(pmember == null){
						throw new Exception("您输入的汇报上级工号不存在，请重新输入。");
					}
					inDto.put("pid", pmember.get("id"));
				}
//				inDto.put("pic",SYS_USER_URL);
				inDto.put("token", UUID.randomUUID().toString());
				bizService.save(inDto);
			}
			result.setData(inDto);
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 获取用户菜单权限
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMenuByUser")
	public BaseResult getMenuByUser(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException, ParseException {
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			Dto member =redisService.getObject(dto.getAsString("token"),BaseDto.class);
			if (member != null) {
				List<Dto> menulist = new ArrayList<>();
				if (menulist == null || menulist.size()==0 ) {
					dto.put("types", "0");
					// 查询出所有模块列表 因为权限页面主要是按照模块设置的
					List<Dto> localMenulist = bizService.queryForList("sysMenu.queryList", dto);
	 				for (Dto module : localMenulist) {
						List<Dto> roles = new ArrayList<Dto>();
						// 查看该模块下是否有子菜单
						dto.put("types", "1");
						dto.put("pid", module.get("id"));
						dto.put("roleid", member.get("roleid"));
						List<Dto> menus = bizService.queryForList("sysMenu.queryMenuList", dto);
						if(menus != null && menus.size()>0){
							module.put("menus", menus);
							menulist.add(module);
						}
					}
					redisService.setValue("INIT_MENU_ROLE_" + member.get("roleid"), JSONArray.toJSONString(menulist));
				}
				result.setData(menulist);
			} else {
				result.setCode("4000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 获取用户操作权限
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOperationByUser")
	public BaseResult getOperationByUser(HttpServletRequest request,
                                         HttpServletResponse response) throws IOException, ParseException {
		org.g4studio.core.metatype.Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			Dto member = redisService.getObject(dto.getAsString("token"),BaseDto.class);
			if (member != null) {
				List<Dto> operationlist = (List)redisService.getList("INIT_OPERATION_ROLE_" + member.get("roleid"),BaseDto.class);
				if (operationlist == null) {
					dto.put("types", "0");
					// 查询出所有模块列表 因为权限页面主要是按照模块设置的
					operationlist = bizService.queryForList("sysMenu.queryList",dto);
					for (Dto module : operationlist) {
						// 查看该模块下是否有按钮
						dto.put("types", "2");
						dto.put("roleid", member.get("roleid"));
						List<Dto> bts = bizService.queryForList("sysMenu.queryList", dto);
						module.put("operates", bts);
					}
				}
				result.setData(operationlist);
				redisService.setValue("INIT_OPERATION_ROLE_" + member.get("roleid"), JSONArray.toJSONString(operationlist));
			} else {
				result.setCode("4000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 获取用户数据权限
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDataByUser")
	public BaseResult getDataByUser(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException, ParseException {
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			Dto member = redisService.getObject(dto.getAsString("token"),BaseDto.class);
			if (member != null) {
				List<Dto> datas = (List) redisService.getList("INIT_DATA_ROLE_" + member.get("roleid"),BaseDto.class);
				if (datas == null) {
					dto.put("types", "0");
					// 查询出所有模块列表 因为权限页面主要是按照模块设置的
					datas = bizService.queryForList("sysMenu.queryList", dto);
					for (Dto module : datas) {
						// 查看该模块下是否有设置权限归属
						dto.put("menuid", module.get("id"));
						dto.put("roleid", member.get("roleid"));
						List<Dto> brs = bizService.queryForList("sysMenu.queryBranchCodeList", dto);
						module.put("datas", brs);
					}
				}
				result.setData(datas);
				redisService.setValue("INIT_DATA_ROLE_" + member.get("roleid"), JSONArray.toJSONString(datas));
			} else {
				result.setCode("4000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 新增系统用户
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addUsrInfo")
	public BaseResult addUsrInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			dto.put("token", UUID.randomUUID());
			bizService.save(dto);
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 获取用户信息
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserInfo")
	public BaseResult getUserInfo(HttpServletRequest request) throws IOException, ParseException {
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			if (dto.getAsString("token") != null) {
				Dto member = redisService.getObject(dto.getAsString("token"),BaseDto.class);
				if (member != null) {
					Dto dbmember = (BaseDto) bizService.queryForObject("sysUser.getInfo",new BaseDto("id", member.get("id")));
					result.setData(dbmember);
				} else {
					result.setCode("4000");
				}
			} else {
				result.setCode("4000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 设置员工离职
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/leaveInfo")
	public BaseResult leaveInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		Dto inDto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			inDto.put("tableName", "sysUser");
			inDto.put("method", "leaveInfo");
			bizService.update(inDto);
			result.setMsg("数据操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 查询修改用户
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryUserInfo")
	public BaseResult queryInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			Dto info = (BaseDto) bizService.queryForDto(dto.getAsString("t")+ ".queryUserList", dto);
			result.setData(info);
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}



	/**
	 * 生成图形验证码
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/getVtoken")
	public BaseResult getVtoken(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		Dto inDto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		debugLog.send("QY",request.getRequestURI(), com.alibaba.fastjson.JSONObject.toJSONString(inDto));
		//debugLog.send("QY",request.getRequestURI(),"dd");
		try {
			result.setData(UUID.randomUUID());
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

		}
		return result;
	}

	/**
	 * 生成图形验证码
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/getLoginCode")
	public void createAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		Dto inDto = WebUtils.getCodeImageParamAsDto(request);
		BaseResult result = new BaseResult();
		String vtoken = inDto.getAsString("vtoken");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Gelpe");
		map.put("age", 30);


		debugLog.send("QY",request.getRequestURI(), com.alibaba.fastjson.JSONObject.toJSONString(map));
		debugLog.send("QY",request.getRequestURI(),vtoken);
		try {
			String verifyCode = GraphicHelper.createImge(130, 36, "png",response.getOutputStream());
			// 图片验证码存放到缓存60秒
			redisService.delete(vtoken + "_IMAGE_CODE");
			redisService.setValue(vtoken + "_IMAGE_CODE", verifyCode, StatusConstant.VERIFICATION_TIME);
			debugLog.send("QY","放入Redis成功"+request.getRequestURI(),verifyCode+"    "+vtoken + "_IMAGE_CODE");

		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());
			e.printStackTrace();
			result = reduceErr("生成验证码失败");
		}
	}
}
