/**
 * 
 */
package com.hst.ii.common.web;

import java.io.IOException;
import java.util.Map;

import com.hst.core.ServiceContext;
import com.hst.core.auth.User;
import com.hst.core.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hst.core.MapBean;
import com.hst.core.ServiceException;
import com.hst.core.annotation.HttpCache;
import com.hst.core.annotation.WebAuth;
import com.hst.core.dao.ISQLDao;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyh
 *
 */
@Controller
@RequestMapping("/dict")
public class DictController {
	@Autowired
	ISQLDao sql;

	@RequestMapping("/{name}")
	public void query(@PathVariable String name, @RequestBody(required = false) String data, Model m)
			throws ServiceException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> query;
		if (null == data || "".equals(data)) {
			query = new MapBean<>();
		} else
			query = JsonUtil.read(data, Map.class);

		User user=ServiceContext.getInstance().getUser();
		if(user ==null){
			return;
		}
		String orgCode= user.getOrgcode();
		query.put("orgid",orgCode);

		String[] names = name.split(",");
		for (String n : names) {
			m.addAttribute(n, sql.query("dicts." + n, query));
		}
	}

	/**
	 * 数据字典
	 * @param name
	 * @param m
	 * @throws ServiceException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping()
	public void dict(String name, Model m)
			throws ServiceException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> query = new MapBean<>();

		String[] names = name.split(",");
		for (String n : names) {
			query.put("name", n);
			m.addAttribute(n, sql.query("dicts.dicts", query));
		}
	}

}
