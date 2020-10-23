package com.hst.ii.sales.proxy;

import com.hst.core.Page;
import com.hst.core.meta.DataProxy;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.MetaField;
import com.hst.core.meta.MetaRepository;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.sales.service.SaleMetaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/7/13 14:53
 */
public class SaleProxy extends DataProxy {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SaleMetaService service;

    @Autowired
    MetaRepository repo;

    @Autowired
    JdbcTemplate jdbc;

    static class Query {
        private String sql;
        private List<Object> querys;

        public Query(String sql, List<Object> querys) {
            super();
            this.sql = sql;
            this.querys = querys;
        }

        public String getSql() {
            return sql;
        }

        public List<Object> getQuerys() {
            return querys;
        }

        @Override
        public String toString() {
            return "Query [sql=" + sql + ", querys=" + StringUtils.join(querys.toArray()) + "]";
        }

    }

    protected Query parseQuery(Map<String, ?> query) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        int i = 0;
        for (MetaField mf : service.getListFields()) {
            if (i++ > 0)
                sb.append(",");

            sb.append(mf.getName());
        }
        sb.append(" from oms_incident where 1 = 1");

        List<Object> args = new ArrayList<>();
        for (MetaField mf : service.getQueryFields()) {
            FieldQuery fq = mf.getQuery();
            if (fq == null || FieldQuery.None == fq)
                continue;
            if (fq == FieldQuery.Between) {
                Object v1 = query.get(mf.getName() + "_1");
                Object v2 = query.get(mf.getName() + "_2");

                if (v1 != null) {
                    sb.append(" and ");
                    sb.append(mf.getName());
                    sb.append(" >= ?");
                    args.add(v1);
                }
                if (v2 != null) {
                    sb.append(" and ");
                    sb.append(mf.getName());
                    sb.append(" <= ?");
                    args.add(v2);
                }
            } else {
                String op = fq.toOp();
                if (null != op) {
                    Object v = query.get(mf.getName());
                    if (v != null) {
                        sb.append(" and ");
                        sb.append(mf.getName());
                        sb.append(" ");
                        sb.append(op);
                        sb.append(" ?");
                        args.add(fq == FieldQuery.LIKE ? "%" + v + "%" : v);
                    }
                }
            }
        }

        StringBuilder sbOrder = new StringBuilder();
        for (MetaField mf : repo.getMetaInfo(SaleMetaService.NAME).getFields()) {
            String s = mf.getSort();
            if (null == s || "".equals(s))
                continue;
            if (sbOrder.length() == 0)
                sbOrder.append(" order by ");
            else
                sbOrder.append(", ");
            sbOrder.append(mf.getName());
            sbOrder.append(" ");
            sbOrder.append(s);
        }

        sb.append(sbOrder.toString());
        return new Query(sb.toString(), args);
    }




    @Override
    public List<?> list(Map<String, ?> query) {
        Query q = parseQuery(query);
        return jdbc.query(q.getSql(), q.getQuerys().toArray(), new SaleMap(service.getListFields()));
    }

    @Override
    public Object save(MetaData data) throws Exception {
        return null;
    }

    @Override
    public Page<?> list(Map<String, ?> query, int pageIdx) {
        Query q = parseQuery(query);
        List<Object> args = q.getQuerys();

        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from (");
        sb.append(q.getSql());
        sb.append(") t");
        Integer total = jdbc.queryForObject(sb.toString(), args.toArray(), Integer.class);

        Page<Map<String, Object>> page = new Page<>(pageIdx);
        page.setTotal(total);

        sb = new StringBuilder();
        sb.append(q.getSql());
        sb.append(" limit ?,?");

        args.add((page.getIdx() - 1) * page.getSize());
        args.add(page.getSize());
        page.setData(jdbc.query(sb.toString(), args.toArray(), new SaleMap(service.getListFields())));

        logger.info(q.toString());
        return page;
    }

    @Override
    public Object del(MetaData data) throws Exception {
        return null;
    }
}
