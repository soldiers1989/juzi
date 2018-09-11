package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.mapper.AccessLog;

import java.util.List;

public interface AccessLogService {

    List<AccessLog> findAllList();

    AccessLog findById(Long id);
}
