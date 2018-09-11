package com.jzfq.retail.core.dao;


import com.jzfq.retail.bean.AuthLog;

public interface AuthLogMapper {
    int insertSelective(AuthLog record);

    int deleteDataJob(String deleteTime);
}