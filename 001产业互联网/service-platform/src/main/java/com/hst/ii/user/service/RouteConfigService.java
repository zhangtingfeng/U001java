package com.hst.ii.user.service;

import com.hst.core.dao.IORDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.sys.entity.TRouteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RouteConfigService
 *
 * @author WangYH
 * @date 2020/7/6
 */
@Component
public class RouteConfigService {
    public static final String ROUTE_KEY = "gateway-routes";

    @Autowired
    IORDao dao;

    @Autowired
    RedisTemplate<String, String> redis;

    public void submit() throws IOException {
        String json = JsonUtil.toString(dao.list(TRouteConfig.class));
        redis.opsForValue().set(ROUTE_KEY, json);
        redis.convertAndSend(ROUTE_KEY, "");
    }
}
