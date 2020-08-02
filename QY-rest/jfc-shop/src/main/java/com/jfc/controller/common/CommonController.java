package com.jfc.controller.common;

import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jfc.base.BaseController;
import com.jfc.base.debugLog;
import com.jfc.util.CommonUtil;
import com.jfc.util.FileUtils;
import com.jfc.util.QrCodeUtils;
import com.jfc.util.*;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesResponse;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.DocumentException;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Utils;
import org.g4studio.core.web.report.excel.ExcelExporter;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用业务处理
 *
 * @author mcl
 * @see
 * @since 16点36分
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/cmn")
public class CommonController extends BaseController {


    /**
     * 查询对象
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryInfo")
    public BaseResult queryInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            /*Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if(!StringUtils.isNotEmpty(dto.getAsString("loginCode"))){
                if(null == member){
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }*/
            String sql = dto.getAsString("sql");
            if (!StringUtils.isNotBlank(sql)) {
                sql = "getInfo";
            }

            Dto info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + "." + sql, dto);
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 查询列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryList")
    public BaseResult queryList(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        debugLog.send("QY","接收参数"+request.getRequestURI()+dto.getAsString("t"),dto);

        BaseResult result = new BaseResult();
        try {
           /* Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if(!StringUtils.isNotEmpty(dto.getAsString("loginCode"))){
                if(null == member){
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }
            // dto.put("flag",member.getAsString("shopid"));
            dto.put("userid", member == null ? "" : member.get("id"));*/
            //查询总网点
            String sql = dto.getAsString("sql");
            if (!StringUtils.isNotBlank(sql)) {
                sql = "queryList";
            }
            List paramList = bizService.queryForList(dto.getAsString("t") + "." + sql, dto);
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI()+dto.getAsString("t"),e.getLocalizedMessage());

        }
        debugLog.send("QY","返回数据"+request.getRequestURI()+dto.getAsString("t"),result);

        return result;
    }

    /**
     * 查询对象 数据模糊
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryInfoRole")
    public BaseResult queryInfoRole(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            Dto info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + ".getInfo", dto);
            List<Dto> paramList = new ArrayList<Dto>();
            paramList.add(info);
            result.setData(paramList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryListRole")
    public BaseResult queryListRole(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            List paramList = null;
            if (StringUtils.isNotBlank(dto.getAsString("sql"))) {
                paramList = bizService.queryForList(dto.getAsString("t") + "." + dto.getAsString("sql"), dto);
            } else {
                paramList = bizService.queryForList(dto.getAsString("t") + ".queryList", dto);
            }
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 分页
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryPage")
    public Dto queryPage(HttpServletRequest request) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        debugLog.send("QY","接收参数"+request.getRequestURI()+dto.getAsString("t"),dto);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            /*if (null == member) {
                retDto.put("code", StatusConstant.CODE_4000);
                retDto.setMsg("请登录");
                return retDto;
            }*/
            if (null != member) {
                dto.put("userid", member.get("id"));
            }
            String sql = dto.getAsString("sql");
            if (!StringUtils.isNotBlank(sql)) {
                sql = "queryList";
            }
            if (dto.getAsString("sql").equals("queryDailyReport")) {
                Dto kefu = (BaseDto) bizService.queryForObject("sysUser.getInfo", new BaseDto("id", dto.getAsString("creator")));
                if (kefu.getAsString("roleid").equals("13") == false) {
                    dto.put("creator", "");
                }
                sql = "queryList";
            }
            List<Dto> paramList = bizService.queryForPageCenter(dto.getAsString("t") + "." + sql, dto);
            retDto.put("rows", paramList);
            String sqlCount = dto.getAsString("sqlCount");
            if (!StringUtils.isNotBlank(sqlCount)) {
                sqlCount = "queryListCount";
            }
            Dto totalCount = (BaseDto) bizService.queryForDto(dto.getAsString("t") + "." + sqlCount, dto);
            if (null != totalCount && totalCount.size() > 0) {
                retDto.put("total", totalCount.get("total"));
            } else {
                retDto.put("total", 0);
            }
            retDto.put("code", "0000");
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI()+dto.getAsString("t"),e.getLocalizedMessage());
        }
        debugLog.send("QY","返回数据"+request.getRequestURI()+dto.getAsString("t"),retDto);
        return retDto;
    }

    /**
     * 分页
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryPageRole")
    public Dto queryPageRole(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            List<Dto> paramList = bizService.queryForPage(dto.getAsString("t") + ".queryList", dto);
            System.out.println("分页查询：   " + paramList.size());
            retDto.put("rows", paramList);
            retDto.put("total", bizService.queryForList(dto.getAsString("t") + ".queryList", dto).size());
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return retDto;
    }

    /**
     * 分页
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryForPage")
    public Dto queryForPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            List<Dto> paramList = bizService.queryForPage(dto.getAsString("t") + ".queryList", dto);
            System.out.println("分页查询：   " + paramList.size());
            retDto.put("data", paramList);
//			retDto.put("total", bizService.queryForList(dto.getAsString("t") + ".queryList", dto).size());
            retDto.put("code", "0000");
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return retDto;
    }

    /**
     * 保存信息
     *
     * @param
     * @returnlIST
     */
    @ResponseBody
    @RequestMapping(value = "/editInfo")
    public BaseResult editInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (!StringUtils.isNotEmpty(inDto.getAsString("loginCode"))) {
                if (null == member) {
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }
            if (inDto.getAsLong("id") != null) {// 修改
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    inDto.put("method", "updateInfo");
                }
                inDto.put("updator", member == null ? "" : member.get("id"));
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    CommonController.getTxVideoPic(inDto.getAsString("fileid"));
                }
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    bizService.update(inDto);
                } else {
                    bizService.updateInfo(inDto.getAsString("tableName") + "." + inDto.getAsString("method"), inDto);
                }
                if (inDto.getAsString("t").equals("userDynamic") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "userDynamic");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "activity");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
            } else {// 新增
                if ("1".equals(inDto.getAsString("flag_no"))) {
                    inDto.put("number", CommonUtil.getCarrierNo());
                }
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    inDto.put("method", "saveInfo");
                }
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    CommonController.getTxVideoPic(inDto.getAsString("fileid"));
                }
                inDto.put("creator", member == null ? "" : member.get("id"));
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    bizService.save(inDto);
                } else {
                    bizService.saveInfo(inDto.getAsString("tableName") + "." + inDto.getAsString("method"), inDto);
                }
                if (inDto.getAsString("t").equals("userDynamic") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "userDynamic");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "activity");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
            }
            result.setData(inDto);
        } catch (Exception e) {
//            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 保存信息
     *
     * @param
     * @returnlIST
     */
    @ResponseBody
    @RequestMapping(value = "/editParmas")
    public BaseResult editParmas(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (!StringUtils.isNotEmpty(inDto.getAsString("loginCode"))) {
                if (null == member) {
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }
            if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                inDto.put("method", "updateInfo");
            }
            inDto.put("updator", member == null ? "" : member.get("id"));

            if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                bizService.update(inDto);
            } else {
                bizService.updateInfo(inDto.getAsString("tableName") + "." + inDto.getAsString("method"), inDto);
            }
            result.setData(inDto);
        } catch (Exception e) {
//            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }


    /**
     * 删除信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteInfo")
    public BaseResult deleteInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        if (!StringUtils.isNotEmpty(inDto.getAsString("sql"))) {
            inDto.put("method", "deleteInfo");
        } else {
            inDto.put("method", inDto.getAsString("sql"));
        }
        try {
            String[] checked = inDto.getAsString("ids").split(",");
            for (int i = 0; i < checked.length; i++) {
                inDto.put("id", checked[i]);
                bizService.delete(inDto);
            }
            result.setData(new BaseDto("msg", "数据操作成功"));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 发送短信验证码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendMsg")
    public BaseResult sendMsg(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto config = (Dto) bizService.queryForDto("sysConfig.getInfo", new BaseDto("id", 6));
            String phone = config.getAsString("val");
            Integer code = new Random().nextInt(999999) % (999999 - 100000 + 1) + 100000;
            HttpRequestClient.sendMsg(inDto.getAsString("telephone"), code + "");
            System.out.println("---------------" + code);
            redisService.setValue(inDto.getAsString("telephone") + "_IMAGE_CODE", code + "", 120l);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 发送短信验证码校验
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkCode")
    public BaseResult checkCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String code = redisService.getValue("SAFE_CODE_" + inDto.getAsString("token"));
            if (!code.equals(inDto.getAsString("msgcode"))) {
                throw new Exception("验证码不正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 修改安全手机新手机发送验证码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendCode")
    public BaseResult sendCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String newMobilePhoneNum = inDto.getAsString("newmobile");
            Integer code = new Random().nextInt(999999) % (999999 - 100000 + 1) + 100000;
            redisService.setValue("SAFE_MOBILE_NEW_" + inDto.getAsString("token"), newMobilePhoneNum, 120l);
            redisService.setValue("SAFE_CODE_NEW_" + inDto.getAsString("token"), String.valueOf(code), 120l);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVtoken")
    public BaseResult getVtoken(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            result.setData(UUID.randomUUID().toString());
        } catch (Exception e) {
            e.printStackTrace();
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 生成图形验证码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createImgCode")
    public void createImgCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getCodeImageParamAsDto(request);
        BaseResult result = new BaseResult();
        String vtoken = inDto.getAsString("vtoken");
        String verifyCode = null;
        try {
            // 获取app传过来的宽高 , 如果没有证明是pc
            String h = inDto.getAsString("h");
            String w = inDto.getAsString("w");
            if (StringUtils.isNotBlank(h) && StringUtils.isNotBlank(w)) {
                verifyCode = GraphicHelper.createImge(Integer.valueOf(w), Integer.valueOf(h), "png",
                        response.getOutputStream());
            } else {
                verifyCode = GraphicHelper.createImge(130, 36, "png", response.getOutputStream());
            }
            // 图片验证码存放到缓存
            redisService.delete(vtoken + "_IMAGE_CODE");
            redisService.setValue(vtoken + "_IMAGE_CODE", verifyCode);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            result = reduceErr("生成验证码失败");
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
    }


    /**
     * 上传图片
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public BaseResult imgUpload(HttpServletRequest request, Long bizid, HttpServletResponse response) throws IOException, ParseException {
        BaseResult result = new BaseResult();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            if (file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String imgUrl = FileUtils.storeImg(file.getInputStream(),
                    DateUtil.getStringFromDate(new Date(), "yyyyMMddhhmmss") + new Date().getTime() + type,
                    DateUtil.getStringFromDate(new Date(), "yyyyMM"));
            result.setData(new BaseDto("imgUrl", imgUrl));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 上传视频
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/videoUpload", method = RequestMethod.POST)
    public BaseResult videoUpload(HttpServletRequest request, Long bizid, HttpServletResponse response) throws IOException, ParseException {
        BaseResult result = new BaseResult();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            if (file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String imgUrl = FileUtils.storeFile(file.getInputStream(),
                    DateUtil.getStringFromDate(new Date(), "yyyyMMddhhmmss") + new Date().getTime() + type,
                    DateUtil.getStringFromDate(new Date(), "yyyyMM"));

            if (StringUtils.isNotEmpty(imgUrl)) {
                Map<String, Object> stringObjectMap = uploadFile(imgUrl);
                result.setData(stringObjectMap);
                return result;
            }
            result.setData(new BaseDto("imgUrl", imgUrl));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 视频上传
     */
    public static Map<String, Object> uploadFile(String path) {
        Map<String, Object> map = new HashMap<>();
        VodUploadClient client = new VodUploadClient("AKIDb7RcfbE0yufEe1PME0lOp7MsGrQykcTG", "pW5nRyNhuxtZCfXI3iolaCkAUikcLyUq");
        VodUploadRequest request = new VodUploadRequest();
        request.setMediaFilePath(path);
        try {
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            map.put("url", response.getMediaUrl());
            map.put("fileId", response.getFileId());
            CommonController.getTxVideo(response.getFileId());
            CommonController.getTxVideoPic(response.getFileId());
        } catch (Exception e) {
            // 业务方进行异常处理
        }
        return map;
    }


    /**
     * 查询用户登录记录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserLoginRecord")
    public BaseResult getUserLoginRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null != member) {
                dto.put("userId", member == null ? "" : member.get("id"));
                List<Dto> fieldsList = bizService.queryList("sysUser.getUserLoginRecord", dto);
                result.setData(fieldsList);
            } else {
                result.setCode("4000");
                result.setMsg("请登录后再试!");
            }
            // 返回条件名称
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 合计
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryTotal")
    public BaseResult queryMoneyTotal(HttpServletRequest request) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            String sqlTotal = dto.getAsString("sqlTotal");
            if (!StringUtils.isNotBlank(sqlTotal)) {
                sqlTotal = "queryTotal";
            }
            Dto info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + "." + sqlTotal, dto);
            //数值千分位划分
            info = CommonUtil.ThousandBit(info);
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 导出条件暂存
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/conditionsExport")
    public BaseResult conditionsExport(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            // 获取日期+4位随机数的条件名称
            String term = sdf.format(new Date()) + (int) (Math.random() * 9000) + 1000;
            // 将条件存入redis中
            redisService.setValue("EXCEL_CONDITION_KEY", JSONArray.toJSONString(dto), 180L);
            // 返回条件名称
            result.setData(term);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 异常管理订单Excel导出
     *
     * @author wenbin
     * @since 11点24分
     */
    @ResponseBody
    @RequestMapping(value = "/execExcel")
    public void orderExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dto parametersDto = new BaseDto();
        Dto dto = WebUtils.getParamAsDto(request);
        // 截取选中的列
        String[] exfields = dto.getAsString("exfields").split(",");
        Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
        parametersDto.put("jbr", member == null ? "" : member.get("username"));
        parametersDto.put("jbsj", G4Utils.getCurrentTime());
        parametersDto.put("reportTitle", "导出数据");
        // 获取查询的条件
        Dto condition = redisService.getObject("EXCEL_CONDITION_KEY", BaseDto.class);
        // 判断是导出还是导出全部,1为全部，2为指顶列
        if (StringUtils.isNotEmpty(dto.getAsString("ids"))) {
            // 加入查询条件中
            condition.put("ids", dto.getAsString("ids"));
        }
        String sql = condition.getAsString("sql");
        if (!StringUtils.isNotBlank(sql)) {
            sql = "queryList";
        }
        // 带条件查询所有数据
        List<Dto> fieldsList = bizService.queryForList(dto.getAsString("t") + "." + sql, condition);
        // 创建返回结果集合
        List<Dto> exfieldList = new ArrayList();
        // 遍历所有数据
        for (Dto fields : fieldsList) {
            // 判断要导出的列字段是否为空
            if (StringUtils.isNotEmpty(dto.getAsString("exfields"))) {
                // 声明要返回的结果
                Dto fielDto = new BaseDto();
                // 由于前端未传入id,所以拼接
                fielDto.put("id", fields.getAsString("id"));
                // 遍历要导出的列字段
                for (int i = 0; i < exfields.length; i++) {
                    // 添加到返回结果中
                    fielDto.put(exfields[i], fields.getAsString(exfields[i]));
                }
                // 添加到返回结果集中
                exfieldList.add(fielDto);
            }
        }
//        if(dto.getAsString("excelName").equals("orderList")){
        Dto sum = new BaseDto();
        //封装合计参数
        sum.put("order_no", "合计");
        sum.put("productnumber", numAddNum(fieldsList, "productnumber"));
        sum.put("productweight", numAddNum(fieldsList, "productweight"));
        sum.put("amount_cash_on_hand", numAddNum(fieldsList, "amount_cash_on_hand"));
        sum.put("noxianjin", numAddNum(fieldsList, "noxianjin"));
        sum.put("amount_info", numAddNum(fieldsList, "amount_info"));
        sum.put("goods_pro_num", numAddNum(fieldsList, "goods_pro_num"));
        sum.put("amount_cash_on_hand", numAddNum(fieldsList, "amount_cash_on_hand"));
        sum.put("amount_arrive", numAddNum(fieldsList, "amount_arrive"));
        sum.put("amount_under_payment", numAddNum(fieldsList, "amount_under_payment"));
        sum.put("receivable_amount", numAddNum(fieldsList, "receivable_amount"));
        sum.put("amount_pocket", numAddNum(fieldsList, "amount_pocket"));
        //添加到列表数据集合中
        fieldsList.add(sum);
//        }
        // 删除缓存中的查询条件
        redisService.delete(dto.getAsString("condition"));
        // 合计条数
        parametersDto.put("countXmid", new Integer(exfieldList.size() == 0 ? fieldsList.size() : exfieldList.size()));
        ExcelExporter excelExporter = new ExcelExporter();
        // 匹配模板路径
        excelExporter.setTemplatePath("/report/" + dto.getAsString("excelName") + "List.xls");
        // 添加数据
        excelExporter.setData(parametersDto, exfieldList.size() == 0 ? fieldsList : exfieldList);
        // 设置文件名称
        excelExporter.setFilename("derivedData.xls");
        excelExporter.export(request, response);
    }


    /**
     * 根据token获取用户登录信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/quertUserInfoByToken")
    public BaseResult quertUserInfoByToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            result.setData(member);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 数值相加
     */
    private double numAddNum(List<Dto> fieldsList, String string) {
        double num = 0;
        for (Dto dto : fieldsList) {
            //指定参数数值合计
            num += (null == dto.getAsDouble(string)) ? 0 : dto.getAsDouble(string);
        }
        return num;
    }

    /**
     * 根据token获取用户登录信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveUserLoginRecord")
    public BaseResult saveUserLoginRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            dto.put("tableName", "sysUser");
            dto.put("method", "saveUserLoginRecord");
            bizService.save(dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }


    /**
     * 查询列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryByShopIdList")
    public BaseResult queryByShopIdList(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        String t = request.getParameter("t");
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member == null) {
                result.setCode("4000");
                return result;
            }
            //根据所属店铺查询
            if (StringUtils.isEmpty(dto.getAsString("shopid")) && !"1".equals(member.getAsString("id"))) {
                dto.put("shopid", member.getAsString("shopid"));
            }
            List paramList = bizService.queryForPageCenter(dto.getAsString("t") + ".queryList", dto);
            result.setData(paramList);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/queryListTest")
    public BaseResult queryListTest(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null != member) {
                // dto.put("flag",member.getAsString("shopid"));
                dto.put("userid", member == null ? "" : member.get("id"));
                Dto condition = redisService.getObject("EXCEL_CONDITION_KEY", BaseDto.class);
                if (condition != null && StringUtils.isNotEmpty(dto.getAsString("wait_arrive_send_site"))) {
                    dto.putAll(condition);
                }
                //查询总网点
//                Dto dtoSite=(Dto)bizService.queryForDto("site.querySitesPidByid",new BaseDto("id",member.getAsLong("deptid")));
                String sql = dto.getAsString("sql");
                if (!StringUtils.isNotBlank(sql)) {
                    sql = "queryListTest";
                }
                List paramList = bizService.queryForList(dto.getAsString("t") + "." + sql, dto);
                result.setData(paramList);
            } else {
                result.setCode("4000");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/addRtmatch")
    public BaseResult addRtmatch(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        inDto.put("method", "saveInfo");
        try {
            String ids[] = inDto.getAsString("ids").split(",");
            String trs[] = inDto.getAsString("trs").split(",");
            inDto.put("req_id", inDto.getAsInteger("ids1"));
            inDto.put("req_type", inDto.getAsString("trs1"));
            for (int i = 0; i < ids.length; i++) {
                inDto.put("pub_id", ids[i]);
                inDto.put("pub_type", trs[i]);
                bizService.save(inDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 导出信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/exportMaketCoupon")
    public BaseResult exportMaketCoupon(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> list = bizService.queryList("marketCoupon.queryList", inDto);
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String fileName = "优惠卷配置信息导出.xlsx";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            //创建第一个Sheet页
            HSSFSheet sheet = hssfWorkbook.createSheet("优惠卷配置信息");
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            cell = row.createCell(0);
            cell.setCellValue("优惠券编号");
            cell = row.createCell(1);
            cell.setCellValue("所属商铺");
            cell = row.createCell(2);
            cell.setCellValue("等价金额");
            cell = row.createCell(3);
            cell.setCellValue("金额满减限制");
            cell = row.createCell(4);
            cell.setCellValue("商品数量满减");
            cell = row.createCell(5);
            cell.setCellValue("使用范围");
            cell = row.createCell(6);
            cell.setCellValue("有效开始时间");
            cell = row.createCell(7);
            cell.setCellValue("有效结束时间");
            cell = row.createCell(8);
            cell.setCellValue("状态");
            cell = row.createCell(9);
            cell.setCellValue("创建时间");
            cell = row.createCell(10);
            cell.setCellValue("创建人");
            if (null != list && list.size() > 0) {
                HSSFRow rows = null;
                HSSFCell cells = null;
                int index = 0;
                for (Dto dto : list) {
                    index++;
                    rows = sheet.createRow(index);
                    cells = rows.createCell(0);
                    cells.setCellValue(dto.getAsString("number"));
                    cells = rows.createCell(1);
                    cells.setCellValue(dto.getAsString("shopname"));
                    cells = rows.createCell(2);
                    cells.setCellValue(dto.getAsString("money"));
                    cells = rows.createCell(3);
                    cells.setCellValue(dto.getAsString("full_money"));
                    cells = rows.createCell(4);
                    cells.setCellValue(dto.getAsString("full_nums"));
                    cells = rows.createCell(5);
                    cells.setCellValue(dto.getAsString("theProducts"));
                    cells = rows.createCell(6);
                    cells.setCellValue(dto.getAsString("starttime"));
                    cells = rows.createCell(7);
                    cells.setCellValue(dto.getAsString("endtime"));
                    cells = rows.createCell(8);
                    cells.setCellValue(dto.getAsString("statusval"));
                    cells = rows.createCell(9);
                    cells.setCellValue(dto.getAsString("create_time"));
                    cells = rows.createCell(10);
                    cells.setCellValue(dto.getAsString("creatorname"));
                }
            }
            OutputStream out = response.getOutputStream();
            response.setContentType("application/msexcel;charset=GBK");
            hssfWorkbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 导出信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/exportCard")
    public BaseResult exportCard(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> list = bizService.queryList("chargeCard.queryList", inDto);
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String fileName = "充值卡信息导出.xlsx";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            //创建第一个Sheet页
            HSSFSheet sheet = hssfWorkbook.createSheet("充值卡信息");
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            cell = row.createCell(0);
            cell.setCellValue("充值卡编号");
            cell = row.createCell(1);
            cell.setCellValue("绑定手机号");
            cell = row.createCell(2);
            cell.setCellValue("充值卡金额");
            cell = row.createCell(3);
            cell.setCellValue("充值卡余额");
            cell = row.createCell(4);
            cell.setCellValue("创建时间");
            cell = row.createCell(5);
            cell.setCellValue("截止时间");
            cell = row.createCell(6);
            cell.setCellValue("使用时间");
            cell = row.createCell(7);
            cell.setCellValue("使用人");
            cell = row.createCell(8);
            cell.setCellValue("状态");
            if (null != list && list.size() > 0) {
                HSSFRow rows = null;
                HSSFCell cells = null;
                int index = 0;
                for (Dto dto : list) {
                    index++;
                    rows = sheet.createRow(index);
                    cells = rows.createCell(0);
                    cells.setCellValue(dto.getAsString("number"));
                    cells = rows.createCell(1);
                    cells.setCellValue(dto.getAsString("mobile"));
                    cells = rows.createCell(2);
                    cells.setCellValue(dto.getAsString("money"));
                    cells = rows.createCell(3);
                    cells.setCellValue(dto.getAsString("remains"));
                    cells = rows.createCell(4);
                    cells.setCellValue(dto.getAsString("createtime"));
                    cells = rows.createCell(5);
                    cells.setCellValue(dto.getAsString("endtime"));
                    cells = rows.createCell(6);
                    cells.setCellValue(dto.getAsString("usetime"));
                    cells = rows.createCell(7);
                    cells.setCellValue(dto.getAsString("nickname"));
                    cells = rows.createCell(8);
                    cells.setCellValue(dto.getAsString("val"));
                }
            }
            OutputStream out = response.getOutputStream();
            response.setContentType("application/msexcel;charset=GBK");
            hssfWorkbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/meditInfo")
    public BaseResult meditInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("clt") + "_login_" + inDto.getAsString("token"), BaseDto.class);
            if (!StringUtils.isNotEmpty(inDto.getAsString("loginCode"))) {
                if (null == member) {
                    result.setCode(StatusConstant.CODE_4000);
                    result.setMsg("请登录");
                    return result;
                }
            }
            if (inDto.getAsLong("id") != null) {// 修改
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    inDto.put("method", "updateInfo");
                }
                inDto.put("updator", member == null ? "" : member.get("id"));
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    CommonController.getTxVideoPic(inDto.getAsString("fileid"));
                }
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    bizService.update(inDto);
                } else {
                    bizService.updateInfo(inDto.getAsString("tableName") + "." + inDto.getAsString("method"), inDto);
                }
                if (inDto.getAsString("t").equals("userDynamic") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "userDynamic");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "activity");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
            } else {// 新增
                if (inDto.getAsString("t").equals("messageInfo")) {
                    List<Dto> listDto = bizService.queryForList("messageInfo.getInfo", new BaseDto("member_id", inDto.getAsString("member_id")));
                    if (null != listDto && listDto.size() > 0) {
                        result.setMsg("不能重复提交");
                        result.setCode("0001");
                        result.setData("不能重复提交");
                        return result;
                    }
                }
                if ("1".equals(inDto.getAsString("flag_no"))) {
                    inDto.put("number", CommonUtil.getCarrierNo());
                }
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    inDto.put("method", "saveInfo");
                }
                if (inDto.getAsString("t").equals("userDynamic") && StringUtils.isNotEmpty(inDto.getAsString("fileid")) && inDto.getAsString("fileid").split(",").length > 0) {
                    for (String fileid : inDto.getAsString("fileid").split(",")) {
                        CommonController.getTxVideoPic(inDto.getAsString("fileid"));
                    }
                }
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    CommonController.getTxVideoPic(inDto.getAsString("fileid"));
                }
                inDto.put("creator", member == null ? "" : member.get("id"));
                if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                    bizService.save(inDto);
                } else {
                    bizService.saveInfo(inDto.getAsString("tableName") + "." + inDto.getAsString("method"), inDto);
                }
                if (inDto.getAsString("t").equals("userDynamic") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "userDynamic");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
                if (inDto.getAsString("t").equals("activity") && StringUtils.isNotEmpty(inDto.getAsString("fileid"))) {
                    String task_id = CommonController.getTxVideo(inDto.getAsString("fileid"));
                    Dto param = new BaseDto();
                    param.put("tableName", "activity");
                    param.put("task_id", task_id);
                    param.put("id", inDto.getAsString("id"));
                    bizService.update(param);
                }
            }
            result.setData(inDto);
        } catch (Exception e) {
//            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }


    /**
     * 阿里云短信发送
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendMsgInfo")
    public BaseResult aLiCheckCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(inDto.getAsString("mobile"))) {
                Integer code = new Random().nextInt(999999) % (999999 - 100000 + 1) + 100000;
                DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4FvX9AZuPxAfQ6rGTrhG", "7mH14NjJoholHUoyaRakf8FsfpgnIr");
                IAcsClient client = new DefaultAcsClient(profile);
                CommonRequest requestParam = new CommonRequest();
                requestParam.setMethod(MethodType.POST);
                requestParam.setDomain("dysmsapi.aliyuncs.com");
                requestParam.setVersion("2017-05-25");
                requestParam.setAction("SendSms");
                requestParam.putQueryParameter("RegionId", "default");
                requestParam.putQueryParameter("PhoneNumbers", inDto.getAsString("mobile"));
                requestParam.putQueryParameter("SignName", "海宁家纺城");
                requestParam.putQueryParameter("TemplateCode", "SMS_183766925");
                requestParam.putQueryParameter("TemplateParam", "{code:" + code + "}");
                CommonResponse responseResult = client.getCommonResponse(requestParam);
                System.out.println(responseResult.getData());
                redisService.setValue(inDto.getAsString("mobile"), code.toString(), 300l);
            } else {
                throw new Exception("手机号不能为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    //生成优惠券分享码二码合一
    @ResponseBody
    @RequestMapping(value = "/qecodeAdd")
    public BaseResult qecodeAdd(HttpServletRequest request, HttpServletResponse responses) throws DocumentException, IOException, ParseException {
        BaseResult result = new BaseResult();
        Dto inDto = WebUtils.getParamAsDto(request);
        try {
            String content = QrCodeUtils.drawLogoQRCodePrint(null, "http://static.vip.cnhtol.net/shop?id=" + inDto.getAsString("id"), "");
            result.setData(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }


    //生成卡包码
    @ResponseBody
    @RequestMapping(value = "/pcCode")
    public BaseResult pcCode(HttpServletRequest request, HttpServletResponse responses) throws DocumentException, IOException, ParseException {
        BaseResult result = new BaseResult();
        Dto inDto = WebUtils.getParamAsDto(request);
        ClassPathResource pathResource = new ClassPathResource("loginUser.png");
        try {
            InputStream inputStream = pathResource.getInputStream();
            String content = QrCodeUtils.drawLogoQRCodePrint(inputStream, "http://static.zaintea.com/package?type=2&id=" + inDto.getAsString("id"), "斟茶记分享");
            result.setData(content);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 阿里云短信发送
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkMsgInfo")
    public BaseResult checkMsgInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(inDto.getAsString("mobile"))) {
                if (redisService.getValue(inDto.getAsString("mobile")).toString().equals(inDto.getAsString("code"))) {
                    result.setData(true);
                } else {
                    result.setData(false);
                }
            } else {
                throw new Exception("手机号不能为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 用户审核
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkShop")
    public BaseResult checkShop(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {

            inDto.put("tableName", inDto.getAsString("t"));

            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            Long id = inDto.getAsLong("id");
            if (id == null) {
                return result;
            }
            if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                inDto.put("method", "updateInfo");
            }
            inDto.put("updator", member == null ? "" : member.get("id"));

            //根据id查询
            Dto shopInfo = (Dto) bizService.queryForDto("shop.getInfo", new BaseDto("id", id));
            if (shopInfo != null && shopInfo.getAsString("status").equals("0")) {


                if (inDto.getAsString("status").equals("1")) {
                    bizService.updateInfo(inDto.getAsString("tableName") + "." + inDto.getAsString("method"), inDto);
                    Dto dtoParam = (Dto) bizService.queryForDto("member.getInfo", new BaseDto("id", shopInfo.getAsString("member_id")));
                    if (null != dtoParam) {
                        Integer code = new Random().nextInt(99999999) % (99999999 - 10000000 + 1) + 10000000;
                        Dto param = new BaseDto();
                        param.put("wopenid", dtoParam.getAsString("wopenid"));
                        param.put("username", dtoParam.getAsString("username"));
                        param.put("moile", dtoParam.getAsString("telephone"));
                        param.put("account", dtoParam.getAsString("telephone"));
                        param.put("password", code);
                        param.put("shop_id", id);
                        param.put("status", 1);
                        bizService.saveInfo("sysUser.saveUserInfo", param);

                        Dto paramRoid = new BaseDto();
                        paramRoid.put("roleid", "14");
                        paramRoid.put("userid", param.getAsString("id"));
                        bizService.saveInfo("sysRoleUser.saveInfo", paramRoid);

                        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4FvX9AZuPxAfQ6rGTrhG", "7mH14NjJoholHUoyaRakf8FsfpgnIr");
                        IAcsClient client = new DefaultAcsClient(profile);
                        CommonRequest requestParam = new CommonRequest();
                        requestParam.setMethod(MethodType.POST);
                        requestParam.setDomain("dysmsapi.aliyuncs.com");
                        requestParam.setVersion("2017-05-25");
                        requestParam.setAction("SendSms");
                        requestParam.putQueryParameter("RegionId", "default");
                        requestParam.putQueryParameter("PhoneNumbers", dtoParam.getAsString("telephone"));
                        requestParam.putQueryParameter("SignName", "海宁家纺城开通商户服务");
                        requestParam.putQueryParameter("TemplateCode", "SMS_185211546");
                        requestParam.putQueryParameter("TemplateParam", "{user:" + dtoParam.getAsString("telephone") + ",password:" + code + "}");
                        CommonResponse responseResult = client.getCommonResponse(requestParam);
                        System.out.println(responseResult.getData());
                        redisService.setValue(inDto.getAsString("mobile") + "_user", code.toString(), 300l);
                    }
                } else if (inDto.getAsString("status").equals("2")) {
                    inDto.put("is_delete", "Y");
                    bizService.updateInfo(inDto.getAsString("tableName") + "." + "updateInfo", inDto);
                    Dto dtoParam = (Dto) bizService.queryForDto("member.getInfo", new BaseDto("id", shopInfo.getAsString("member_id")));
                    if (null != dtoParam) {
                        sendErrorMsgInfo(dtoParam.getAsString("telephone"));
                    }
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * 店铺门牌号审核
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkShopDoorPlate")
    public BaseResult checkShopDoorPlate(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {

            inDto.put("tableName", inDto.getAsString("t"));

            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            Long id = inDto.getAsLong("id");
            if (id == null) {
                return result;
            }
            if (!StringUtils.isNotEmpty(inDto.getAsString("method"))) {
                inDto.put("method", "updateInfo");
            }
            inDto.put("updator", member == null ? "" : member.get("id"));

            //根据id查询
            Dto shopInfo = (Dto) bizService.queryForDto("shop.getInfo", new BaseDto("id", id));
            if (shopInfo != null && shopInfo.getAsString("number_status").equals("0")) {
                    bizService.updateInfo(inDto.getAsString("tableName") + "." + inDto.getAsString("method"), inDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }

    /**
     * @param message 类型 资讯、商品等
     * @param mobile  用户手机号
     * @param status  状态
     * @param title   标题
     */
    public static void sendErrorMsgInfo(String mobile) throws IOException, ParseException {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4FvX9AZuPxAfQ6rGTrhG", "7mH14NjJoholHUoyaRakf8FsfpgnIr");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest requestParam = new CommonRequest();
        requestParam.setMethod(MethodType.POST);
        requestParam.setDomain("dysmsapi.aliyuncs.com");
        requestParam.setVersion("2017-05-25");
        requestParam.setAction("SendSms");
        requestParam.putQueryParameter("RegionId", "default");
        requestParam.putQueryParameter("PhoneNumbers", mobile);
        requestParam.putQueryParameter("SignName", "海宁家纺城开通商户服务");
        requestParam.putQueryParameter("TemplateCode", "SMS_185560989");
        requestParam.putQueryParameter("TemplateParam", "{}");
        CommonResponse responseResult = null;
        try {
            responseResult = client.getCommonResponse(requestParam);
        } catch (ClientException e) {
            e.printStackTrace();
            debugLog.send("QY","程序报错"+"mobile",e.getLocalizedMessage());

        }
        System.out.println(responseResult.getData());
    }

    /**
     * 用户审核通过调用
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendUserInfo")
    public BaseResult sendUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(inDto.getAsString("id"))) {
                Dto dtoParam = (Dto) bizService.queryForDto("member.getInfo", new BaseDto("id", inDto.getAsString("id")));
                if (null != dtoParam) {
                    Integer code = new Random().nextInt(99999999) % (99999999 - 10000000 + 1) + 10000000;
                    Dto param = new BaseDto();
                    param.put("wopenid", dtoParam.getAsString("wopenid"));
                    param.put("username", dtoParam.getAsString("username"));
                    param.put("moile", dtoParam.getAsString("telephone"));
                    param.put("account", dtoParam.getAsString("telephone"));
                    param.put("password", code);
                    param.put("status", 1);
                    bizService.saveInfo("sysUser.saveUserInfo", param);
                    Dto paramRoid = new BaseDto();
                    paramRoid.put("roleid", "14");
                    paramRoid.put("userid", param.getAsString("id"));
                    bizService.saveInfo("sysRoleUser.saveInfo", paramRoid);
                    DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4FvX9AZuPxAfQ6rGTrhG", "7mH14NjJoholHUoyaRakf8FsfpgnIr");
                    IAcsClient client = new DefaultAcsClient(profile);
                    CommonRequest requestParam = new CommonRequest();
                    requestParam.setMethod(MethodType.POST);
                    requestParam.setDomain("dysmsapi.aliyuncs.com");
                    requestParam.setVersion("2017-05-25");
                    requestParam.setAction("SendSms");
                    requestParam.putQueryParameter("RegionId", "default");
                    requestParam.putQueryParameter("PhoneNumbers", dtoParam.getAsString("telephone"));
                    requestParam.putQueryParameter("SignName", "海宁家纺城开通商户服务");
                    requestParam.putQueryParameter("TemplateCode", "SMS_185211546");
                    requestParam.putQueryParameter("TemplateParam", "{user:" + dtoParam.getAsString("telephone") + ",password:" + code + "}");
                    CommonResponse responseResult = client.getCommonResponse(requestParam);
                    System.out.println(responseResult.getData());
                    redisService.setValue(inDto.getAsString("mobile") + "_user", code.toString(), 300l);
                }

            } else {
                throw new Exception("手机号不能为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+"mobile",e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 获取点播签名
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTxVideoSign")
    public BaseResult getTxVideoSign(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        BaseResult result = new BaseResult();
        try {
            Signature sign = new Signature();
            sign.setSecretId("AKIDb7RcfbE0yufEe1PME0lOp7MsGrQykcTG");
            sign.setSecretKey("pW5nRyNhuxtZCfXI3iolaCkAUikcLyUq");
            sign.setCurrentTime(System.currentTimeMillis() / 1000);
            sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
            sign.setSignValidDuration(3600 * 24 * 2);
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            result.setData(signature);
        } catch (Exception e) {
            result = reduceErr("获取签名失败");
            debugLog.send("QY","程序报错"+"mobile",e.getLocalizedMessage());
        }
        return result;
    }

    public static String getTxVideo(String fileid) {
        Credential cred = new Credential("AKIDb7RcfbE0yufEe1PME0lOp7MsGrQykcTG", "pW5nRyNhuxtZCfXI3iolaCkAUikcLyUq");

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        VodClient client = new VodClient(cred, "ap-guangzhou", clientProfile);

        String params = "{\"FileId\":\"" + fileid + "\"}";
        WeChatMiniProgramPublishRequest req = WeChatMiniProgramPublishRequest.fromJsonString(params, WeChatMiniProgramPublishRequest.class);

        WeChatMiniProgramPublishResponse resp = null;
        try {
            resp = client.WeChatMiniProgramPublish(req);
            System.out.println(WeChatMiniProgramPublishRequest.toJsonString(resp));
            return resp.getTaskId();
        } catch (TencentCloudSDKException e) {
        }
        return "";
    }

    /**
     * 视频截图
     *
     * @param fileid
     */
    public static String getTxVideoPic(String fileid) {
        Credential cred = new Credential("AKIDb7RcfbE0yufEe1PME0lOp7MsGrQykcTG", "pW5nRyNhuxtZCfXI3iolaCkAUikcLyUq");
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);

        String params = "{\"FileId\":\"" + fileid + "\",\"ProcedureName\":\"LongVideoPreset\"}";
        ProcessMediaByProcedureRequest req = ProcessMediaByProcedureRequest.fromJsonString(params, ProcessMediaByProcedureRequest.class);
        ProcessMediaByProcedureResponse resp = null;
        try {
            resp = client.ProcessMediaByProcedure(req);
            System.out.println(ProcessMediaByProcedureRequest.toJsonString(resp));
            return resp.getTaskId();
        } catch (TencentCloudSDKException e) {
        }
        return "";
    }

    /**
     * 获取任务信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTxVideoTaskInfo")
    public BaseResult getTxVideoTaskInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Credential cred = new Credential("AKIDb7RcfbE0yufEe1PME0lOp7MsGrQykcTG", "pW5nRyNhuxtZCfXI3iolaCkAUikcLyUq");
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            VodClient client = new VodClient(cred, "ap-shanghai", clientProfile);
            String params = "{\"TaskId\":\"" + inDto.getAsString("taskId") + "\"}";
            DescribeTaskDetailRequest req = DescribeTaskDetailRequest.fromJsonString(params, DescribeTaskDetailRequest.class);
            DescribeTaskDetailResponse resp = client.DescribeTaskDetail(req);
            System.out.println(DescribeTaskDetailRequest.toJsonString(resp));
            Map map = new HashMap();
            map.put("code", resp.getWechatMiniProgramPublishTask().getErrCode());
            map.put("message", resp.getWechatMiniProgramPublishTask().getMessage());
            map.put("status", resp.getWechatMiniProgramPublishTask().getStatus());
            result.setData(map);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return result;
    }


    /**
     * 分页
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDynamicList")
    public Dto getDynamicList(HttpServletRequest request) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null != member) {
                dto.put("userid", member.get("id"));
            }
            String sql = dto.getAsString("sql");
            if (!StringUtils.isNotBlank(sql)) {
                sql = "queryList";
            }
            List<Dto> paramList = bizService.queryForPageCenter(dto.getAsString("t") + "." + sql, dto);
            retDto.put("rows", paramList);
            String sqlCount = dto.getAsString("sqlCount");
            if (!StringUtils.isNotBlank(sqlCount)) {
                sqlCount = "queryListCount";
            }
            if(null!=paramList && paramList.size()>0){
                for(Dto param:paramList){
                    param.put("comm",bizService.queryForList("dynamicComment.queryList",new BaseDto("dynamic_id",param.getAsString("id"))));
                }
            }
            Dto totalCount = (BaseDto) bizService.queryForDto(dto.getAsString("t") + "." + sqlCount, dto);
            if (null != totalCount && totalCount.size() > 0) {
                retDto.put("total", totalCount.get("total"));
            } else {
                retDto.put("total", 0);
            }
            retDto.put("code", "0000");
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+"mobile",e.getLocalizedMessage());
        }
        return retDto;
    }

}
