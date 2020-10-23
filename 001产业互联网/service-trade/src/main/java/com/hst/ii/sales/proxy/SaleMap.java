package com.hst.ii.sales.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.hst.core.meta.MetaField;

public class SaleMap implements RowMapper<Map<String, Object>> {
	List<MetaField> fields;

	public SaleMap(List<MetaField> fields) {
		super();
		this.fields = fields;
	}

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		Map<String, Object> data = new HashMap<>();
		for (int i = 0; i < fields.size(); i++)
			data.put(fields.get(i).getName(), rs.getObject(i + 1));

		return data;
	}
}