package com.hst.ii.common.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hst.core.MapBean;
import com.hst.core.ServiceException;
import com.hst.core.annotation.HttpCache;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {
    @Autowired
    ISQLDao sql;

    static List<String> SQL_DICTS = new ArrayList() {{
        add("districts");
    }};

    @HttpCache()
    @RequestMapping()
    public void dict(String name, Model m)
            throws ServiceException, JsonParseException, JsonMappingException, IOException {
        Map<String, Object> query = new MapBean<>();
        String[] names = name.split(",");
        for (String n : names) {
            if (SQL_DICTS.contains(n)) {
                m.addAttribute(n, sql.query("dicts." + n, query));
            } else {
                query.put("name", n);
                m.addAttribute(n, sql.query("dicts.dicts", query));
            }
        }
    }

    @HttpCache()
    @RequestMapping("/{name}")
    public void query(@PathVariable String name, @RequestBody(required = false) String data, Model m)
            throws ServiceException, JsonParseException, JsonMappingException, IOException {
        Map<String, Object> query;
        if (null == data || "".equals(data)) {
            query = new MapBean<>();
        } else
            query = JsonUtil.read(data, Map.class);
        String[] names = name.split(",");
        for (String n : names) {
            m.addAttribute(n, sql.query("dicts." + n, query));
        }
    }
}
