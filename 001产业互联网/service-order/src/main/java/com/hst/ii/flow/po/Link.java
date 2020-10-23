package com.hst.ii.flow.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

/**
 * Link
 * 流程连线
 * @author WangYH
 * @date 2020/8/24
 */
@Data
@ToString(exclude = {"node1","node2"})
public class Link {
    @JsonIgnore
    private Node node1;
    @JsonIgnore
    private Node node2;

    private String name;
    private String type;
    private String condition;

    public String getNodeId1(){
        return node1 == null ? null : node1.getId();
    }
    public String getNodeId2(){
        return node2 == null ? null : node2.getId();
    }
}
