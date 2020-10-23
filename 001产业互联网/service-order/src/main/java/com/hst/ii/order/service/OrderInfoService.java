package com.hst.ii.order.service;

import com.hst.core.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OrderInfoService
 * 获取订单信息
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Service
public class OrderInfoService {
    Map<String, IOrderSubInfo> OrderSubInfos;

    @Autowired
    public void setOrderSubInfos(List<IOrderSubInfo> subInfos) {
        OrderSubInfos = subInfos.stream().collect(Collectors.toMap(
                o -> this.getOrderSubInfoName(o),
                o -> o));
    }

    private String getOrderSubInfoName(IOrderSubInfo subInfo) throws IllegalArgumentException {
        String name;
        Qualifier q = subInfo.getClass().getAnnotation(Qualifier.class);
        if (q == null) {
            throw new IllegalArgumentException(StringUtils.join(subInfo.getClass(), " is not defined name."));
        } else
            name = q.value();

        return name;
    }

    public Map<String, Object> info(String id, String[] subs) {
        Map<String, Object> infos = new HashMap<>();
        Arrays.stream(subs).forEach(n->{
            if (OrderSubInfos.containsKey(n))
                infos.put(n, OrderSubInfos.get(n).get(id));
        });

        return infos;
    }
}
