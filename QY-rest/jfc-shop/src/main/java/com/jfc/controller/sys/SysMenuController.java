package com.jfc.controller.sys;

import com.jfc.base.BaseController;
import com.jfc.base.debugLog;
import com.jfc.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * 菜单控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("menu")
public class SysMenuController extends BaseController {


    @RequestMapping(value = "/findNavTree")
    public BaseResult findNavTree(HttpServletRequest request) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode("4000");
                result.setMsg("请登录");
                return result;
            }

            Long userid = member.getAsLong("id");

            List<Dto> resultList = findByUser(userid);

            resultList.sort((o1, o2) -> o1.getAsInteger("order_num").compareTo(o2.getAsInteger("order_num")));

            result.setData(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }


    @RequestMapping(value = "/editUser")
    public BaseResult editUser(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode("4000");
                result.setMsg("请登录");
                return result;
            }


            String id = dto.getAsString("id");
            dto.put("tableName", "sysUser");
            if (StringUtils.isNotEmpty(id)) {
                //更新
                // 修改
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo("sysUser.updateInfo",dto);
            } else {
                //插入
                dto.put("creator", member == null ? "" : member.get("id"));
                dto.put("updator", member == null ? "" : member.get("id"));

                bizService.saveInfo("sysUser.saveInfo",dto);
            }


            if (StringUtils.isNotEmpty(dto.getAsString("id"))) {
                Dto delete = new BaseDto();
                delete.put("tableName", "sysRoleUser");
                delete.put("method", "deleteInfo");
                delete.put("userid", dto.getAsString("id"));
                bizService.delete(delete);

                String userRoles = dto.getAsString("userRoles");
                if (StringUtils.isNotEmpty(userRoles)) {
                    for (String roleId : userRoles.split(",")) {
                        if (StringUtils.isNotEmpty(roleId)) {

                            Dto insert = new BaseDto();
                            insert.put("roleid", roleId);
                            insert.put("userid", dto.getAsString("id"));
                            insert.put("tableName", "sysRoleUser");
                            bizService.saveInfo("sysRoleUser.saveInfo",insert);
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    @RequestMapping(value = "/findDeptTree")
    public BaseResult findDeptTree(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {
            List<Dto> sysDepts = new ArrayList<>();

            List<Dto> depts = bizService.queryForList("sysDept.queryList", dto);
            for (Dto dept : depts) {
                if (dept.getAsInteger("pid") == null || dept.getAsInteger("pid") == 0) {
                    dept.put("level", 0);
                    sysDepts.add(dept);
                }
            }
            findChildren(sysDepts, depts);

            depts.sort((o1, o2) -> o1.getAsInteger("sort").compareTo(o2.getAsInteger("sort")));

            result.setData(sysDepts);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    @RequestMapping(value = "/findProductTypeTree")
    public BaseResult findProductTypeTree(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {
            List<Dto> sysDepts = new ArrayList<>();

            List<Dto> depts = bizService.queryForList("productType.queryList", dto);
            for (Dto dept : depts) {
                if (dept.getAsInteger("pid") == null || dept.getAsInteger("pid") == 0) {
                    dept.put("level", 0);
                    sysDepts.add(dept);
                }
            }
            findProductChildren(sysDepts, depts);

            depts.sort((o1, o2) -> o1.getAsInteger("id").compareTo(o2.getAsInteger("id")));

            result.setData(sysDepts);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    @RequestMapping(value = "/selectDeptTree")
    public BaseResult selectDeptTree(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {
            if (!StringUtils.isNotEmpty(dto.getAsString("dept_name"))) {
                dto.put("dept_name", "");
            }
            List<Dto> depts = bizService.queryForList("sysDept.queryDeptList", dto);
            Set<String> parentList = new HashSet<>();
            depts.forEach(element -> {
                String parent = element.getAsString("parentList");
                if (StringUtils.isNotEmpty(parent)) {
                    parentList.addAll(Arrays.asList(parent.split(",")));
                }
            });
            result.setData(parentList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    private void findProductChildren(List<Dto> sysDepts, List<Dto> depts) {
        for (Dto sysDept : sysDepts) {
            List<Dto> children = new ArrayList<>();
            for (Dto dept : depts) {
                if (sysDept.getAsInteger("id") != null && sysDept.getAsInteger("id").equals(dept.getAsInteger("pid"))) {
                    dept.put("parentName", sysDept.getAsString("name"));
                    dept.put("level", sysDept.getAsInteger("level") + 1);
                    children.add(dept);
                }
            }
            sysDept.put("children", children);
            children.sort((o1, o2) -> o1.getAsInteger("id").compareTo(o2.getAsInteger("id")));
            findProductChildren(children, depts);
        }
    }

    private void findChildren(List<Dto> sysDepts, List<Dto> depts) {
        for (Dto sysDept : sysDepts) {
            List<Dto> children = new ArrayList<>();
            for (Dto dept : depts) {
                if (sysDept.getAsInteger("id") != null && sysDept.getAsInteger("id").equals(dept.getAsInteger("pid"))) {
                    dept.put("parentName", sysDept.getAsString("dept_name"));
                    dept.put("level", sysDept.getAsInteger("level") + 1);
                    children.add(dept);
                }
            }
            sysDept.put("children", children);
            children.sort((o1, o2) -> o1.getAsInteger("order_num").compareTo(o2.getAsInteger("order_num")));
            findChildren(children, depts);
        }
    }


    /**
     * 获取按钮信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/findPermissions")
    public BaseResult findPermissions(HttpServletRequest request) throws IOException, ParseException {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode("4000");
                result.setMsg("请登录");
                return result;
            }

            String userid = member.getAsString("id");

            BaseDto baseDto = new BaseDto("userid", userid);
            baseDto.put("roleid", 1);
            List<Dto> roleList = bizService.queryForList("sysRoleUser.queryList", baseDto);

            if (roleList.isEmpty()) {
                roleList = bizService.queryForList("sysMenu.getListByRole", new BaseDto("userid", userid));
            } else {
                roleList = bizService.queryForList("sysMenu.queryList", new BaseDto());
            }

            if (roleList.isEmpty()) {
                result.setCode("9999");
                result.setMsg("请联系管理员配置权限");
                return result;
            }


            Set<String> perms = new HashSet<>();

            for (Dto sysMenu : roleList) {
                if (StringUtils.isNotEmpty(sysMenu.getAsString("perms"))) {
                    String[] permsList = sysMenu.getAsString("perms").split(",");
                    perms.addAll(Arrays.asList(permsList));
                }
            }

            result.setData(perms);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
            debugLog.send("QY","程序报错"+request.getRequestURI(),e.getLocalizedMessage());

        }
        return result;
    }


    @RequestMapping(value = "/findMenuTree")
    public BaseResult findMenuTree(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode("4000");
                result.setMsg("请登录");
                return result;
            }

//            根据用户id查询菜单id
            String userid = member.getAsString("id");
            dto.put("id", userid);


            BaseDto baseDto = new BaseDto("userid", userid);
            baseDto.put("roleid", 1);
            List<Dto> roleList = bizService.queryForList("sysRoleUser.queryList", baseDto);

            List<Dto> menus = new ArrayList<>();
            if (roleList.isEmpty()) {
                menus = bizService.queryForList("sysMenu.queryMenuListByUserid", dto);
            } else {
                menus = bizService.queryForList("sysMenu.queryList", new BaseDto());
            }

            List<Dto> resultList = new ArrayList<>();
            for (Dto menu : menus) {
                if (menu.getAsInteger("pid") == null || menu.getAsInteger("pid") == 0) {
                    menu.put("level", 0);
                    if (!exists(resultList, menu) && menu.getAsString("is_delete").equals("N")) {
                        resultList.add(menu);
                    }
                }
            }
            findChildren(resultList, menus);
            resultList.sort((o1, o2) -> o1.getAsInteger("order_num").compareTo(o2.getAsInteger("order_num")));

            result.setData(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 根据角色id获取菜单列表
     *
     * @param userId
     * @return
     */
    public List<Dto> findByUser(Long userId) {

        BaseDto baseDto = new BaseDto("userid", userId);
        baseDto.put("roleid", 1);
        List<Dto> roleList = bizService.queryForList("sysRoleUser.queryList", baseDto);

        List<Dto> menus = new ArrayList<>();
        if (roleList.isEmpty()) {
            menus = bizService.queryForList("sysMenu.queryMenuListByUserid", new BaseDto("id", userId));
        } else {
            menus = bizService.queryForList("sysMenu.queryList", new BaseDto());
        }


        List<Dto> sysMenus = new ArrayList<>();

        for (Dto menu : menus) {
            //遍历集合
            if (menu.getAsInteger("pid") == null || menu.getAsInteger("pid") == 0) {
                menu.put("level", 0);
                if (!exists(sysMenus, menu) && menu.getAsString("is_delete").equals("N")) {
                    sysMenus.add(menu);
                }
            }
        }

        findChildren(sysMenus, menus, 1);

        return sysMenus;
    }

    private void findChildren(List<Dto> SysMenus, List<Dto> menus, int menuType) {
        for (Dto SysMenu : SysMenus) {
            List<Dto> children = new ArrayList<>();
            for (Dto menu : menus) {
                if (menuType == 1 && menu.getAsInteger("type") == 2) {
                    // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
                    continue;
                }
                if (SysMenu.getAsInteger("id") != null && SysMenu.getAsInteger("id").equals(menu.getAsInteger("pid"))) {
                    menu.put("parentName", SysMenu.getAsString("name"));
                    menu.put("level", SysMenu.getAsInteger("level") + 1);
                    if (!exists(children, menu)) {
                        children.add(menu);
                    }
                }
            }
            SysMenu.put("children", children);
            children.sort((o1, o2) -> o1.getAsInteger("order_num").compareTo(o2.getAsInteger("order_num")));
            findChildren(children, menus, menuType);
        }
    }

    /**
     * 判断当前权限是否添加过
     *
     * @param sysMenus
     * @param sysMenu
     * @return
     */
    private boolean exists(List<Dto> sysMenus, Dto sysMenu) {
        boolean exist = false;
        for (Dto menu : sysMenus) {
            if (menu.getAsString("id").equals(sysMenu.getAsString("id"))) {
                exist = true;
            }
        }
        return exist;
    }

    @RequestMapping(value = "/editRole")
    public BaseResult editRole(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode("4000");
                result.setMsg("请登录");
                return result;
            }


            String id = dto.getAsString("id");
            dto.put("tableName", "sysRole");


            if (StringUtils.isNotEmpty(id)) {
                //更新
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo("sysRole.updateInfo",dto);
            } else {
                //插入
                dto.put("creator", member == null ? "" : member.get("id"));
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.saveInfo("sysRole.saveInfo",dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
