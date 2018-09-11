package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.ErrorRetryTask;
import com.jzfq.retail.bean.domain.ErrorRetryTaskWithBLOBs;

import java.util.List;

public interface ErrorRetryTaskManualMapper {

    List<ErrorRetryTaskWithBLOBs> findList(ErrorRetryTask errorRetryTask);

    void update(ErrorRetryTask errorRetryTask);

    void updateTask(ErrorRetryTaskWithBLOBs task);
}