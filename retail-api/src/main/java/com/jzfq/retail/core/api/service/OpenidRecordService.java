package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.OpenidRecord;
import com.jzfq.retail.bean.mapper.AccessLog;

import java.util.List;

public interface OpenidRecordService {

    void saveOpenidRecord(String openId, Integer type);

    void saveOrUpdate(String openId, Integer memberId, Integer code);

    int queryOpenId(String openId);

    OpenidRecord queryOpenidRecordOpenId(String openId);

    void updateOpenIdStatus(String openId);
}
