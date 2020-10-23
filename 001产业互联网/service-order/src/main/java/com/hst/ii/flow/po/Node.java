package com.hst.ii.flow.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Node
 * 流程节点
 * @author WangYH
 * @date 2020/8/24
 */
@Data
@ToString(exclude = {"links","props"})
public class Node {
    /**
     * 所属流程
     */
    @JsonIgnore
    private Flow flow;

    private String id;
    private String name;
    private String node;
    private String type;

    private int x;
    private int y;

    /**
     * 出口连接线
     */
    private List<Link> links = new ArrayList<>();

    /**
     * 节点属性
     */
    private Map<String, String> props = new HashMap<>();
}
