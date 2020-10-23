package com.hst.ii;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.flow.ui.Flow;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SimpleTest
 *
 * @author WangYH
 * @date 2020/9/3
 */
public class SimpleTest {
    public static void main(String[] args) throws Exception {
        Object o = AviatorEvaluator.execute("a == \"1\"", new HashMap<String, Object>(){{
            put("a", "1");
            put("b", "1");
        }});
        System.out.println(o);
    }
}
