package com.hst.ii.flow;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * FlowConfig
 *
 * @author WangYH
 * @date 2020/9/5
 */
@ConfigurationProperties(prefix = "ii.flow")
@Data
@NoArgsConstructor
public class FlowConfig {
    private List<String> noUserControl = new ArrayList<>();
}
