package com.hst.ii.im.dao;

import com.hst.ii.im.po.ChatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * UserRepository
 *
 * @author WangYH
 * @date 2020/8/21
 */
@Mapper
@Repository
public interface UserMapper {
    @Select({"select u.id, u.name, u.id_org orgid, o.name orgname",
            "  from sys_user u left join sys_orginfo o on u.id_org = o.id",
            " where u.id = #{userid}"})
    ChatUser getUserByUserid(String userid);

    @Select({"SELECT c.id, u.id_org org, o.name orgname, c.uid1 uid, u.name, c.dt, c.dt1 - c.dt t ",
            "FROM im_chat c, sys_user u, sys_orginfo o ",
            "WHERE c.uid1 = u.id and u.id_org = o.id and uid2 = #{uid} ",
            "UNION ",
            "SELECT c.id, u.id_org org, o.name orgname,  c.uid2, u.name, c.dt, c.dt2 - c.dt ",
            "FROM im_chat c, sys_user u, sys_orginfo o ",
            "WHERE c.uid2 = u.id and u.id_org = o.id and uid1 = #{uid} ",
            "order by dt"}
    )
    List<Map<String, Object>> getChats(String uid);
}
