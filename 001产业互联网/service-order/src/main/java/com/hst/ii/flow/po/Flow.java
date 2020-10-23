package com.hst.ii.flow.po;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

/**
 * Flow
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Data
@ToString(exclude="nodes")
public class Flow {
    private String id;
    private String name;
    private String type;
    private boolean userControl;
    private Map<String, Node> nodes;
    private List<String> userNodes;
}
