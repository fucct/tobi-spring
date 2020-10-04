package com.fucct.tobispring.dao;

import java.util.Map;

public class SimpleSqlService implements SqlService{
    private Map<String, String> sqlMap;

    public SimpleSqlService(final Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(final String key) throws SqlRetrievalFailureException {
        final String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다");
        }
        return sql;
    }
}
