package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.mapper.AccessLog;
import com.jzfq.retail.core.api.service.AccessLogService;
import com.jzfq.retail.core.dao.AccessLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Administrator on 2016/12/1.
 */
@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Override
    public List<AccessLog> findAllList() {
        return accessLogMapper.findAllList();
    }

    @Override
    public AccessLog findById(Long id) {
        return accessLogMapper.findById(id);
    }


}
