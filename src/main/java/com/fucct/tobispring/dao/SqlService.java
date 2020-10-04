package com.fucct.tobispring.dao;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
