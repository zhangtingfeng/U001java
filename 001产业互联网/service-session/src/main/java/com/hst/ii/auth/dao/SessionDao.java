package com.hst.ii.auth.dao;

import com.hst.ii.auth.po.Menu;
import com.hst.ii.auth.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SessionDao
 *
 * @author WangYH
 * @date 2020/7/1
 */
@Mapper
@Repository
public interface SessionDao {
    @Select({"SELECT t1.id, t1.name, t1.id_org org, t1.passwd, case when t1.locked =1 then 2  when t2.freeze =1 then 3 else 1 end state from sys_user t1 left join sys_orginfo t2 on t1.id_org= t2.id where t1.userid = #{userid} or t1.mobile = #{userid} "})
    User getByUserid(String userid);

    @Select({"select passwd from sys_user where id = #{id}"})
    String getPasswdById(String id);

    @Insert({"insert into sys_user_session (sid, id, ip, agent, status, dt1) ", " values (#{sid}, #{id}, #{ip}, #{agent}, 0, sysdate())"})
    void saveAuthLogin(String sid, String id, String ip, String agent);

    @Update({"update sys_user_session set ",
            " dt2 = sysdate(), status=0 ",
            " where sid = #{sid}"})
    int updateAuthLogof(String sid);

    @Update({"update sys_user set passwd = #{passwd} where id = #{id}"})
    int updateUserPasswd(String id, String passwd);

    List<Menu> getUserMenus(String userid);

    List<String> getUserPrivileges(String userid);
}
