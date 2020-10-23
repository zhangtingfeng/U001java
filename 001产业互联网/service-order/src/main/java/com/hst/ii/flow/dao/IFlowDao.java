package com.hst.ii.flow.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * IFlowDao
 *
 * @author WangYH
 * @date 2020/9/3
 */
@Mapper
@Repository
public interface  IFlowDao {
    @Delete("delete from flow_node where id_flow = #{flowId}")
    public void delNodes(String flowId);

    @Delete("delete from flow_link where id_flow = #{flowId}")
    public void delLinks(String flowId);

    @Delete("delete from flow_node_prop where id_flow = #{flowId}")
    public void delNodeProps(String flowId);

    @Delete("delete from flow_prop where id_flow = #{flowId}")
    public void delProps(String flowId);

    @Delete("delete from flow_org where id_org = #{orgId} and type = #{type}")
    public void delOrgFlow(String orgId, String type);

    @Delete("delete from flow_org_user where id_org = #{orgId} and type = #{type}")
    public void delOrgFlowUser(String orgId, String type);
}
