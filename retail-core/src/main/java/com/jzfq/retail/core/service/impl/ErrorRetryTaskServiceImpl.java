package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.ErrorRetryTask;
import com.jzfq.retail.bean.domain.ErrorRetryTaskWithBLOBs;
import com.jzfq.retail.common.enmu.ErrorRetryTaskStatus;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.http.RetryHttpUtil;
import com.jzfq.retail.core.api.service.ErrorRetryTaskService;
import com.jzfq.retail.core.dao.ErrorRetryTaskMapper;
import com.jzfq.retail.core.dao.manual.ErrorRetryTaskManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月3日 19:02
 * @Description: 失败重试操作接口
 */
@Slf4j
@Service
public class ErrorRetryTaskServiceImpl implements ErrorRetryTaskService {

    @Autowired
    private ErrorRetryTaskMapper errorRetryTaskMapper;

    @Autowired
    private ErrorRetryTaskManualMapper errorRetryTaskManualMapper;
    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public void add(int type, String url, String requestParam, String responseParam, int retryCount, int currentCount, int status) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ErrorRetryTaskWithBLOBs errorRetryTask = new ErrorRetryTaskWithBLOBs();
                errorRetryTask.setType(type);
                errorRetryTask.setRequestUrl(url);
                errorRetryTask.setRequestParam(requestParam);
                errorRetryTask.setResponseParam(responseParam);
                errorRetryTask.setRetryCount(retryCount);
                errorRetryTask.setCurrentCount(currentCount);
                errorRetryTask.setStatus(status);
                errorRetryTask.setCreateTime(DateUtil.getDate());
                errorRetryTaskMapper.insert(errorRetryTask);
                log.info("异步存储请求失败重试任务成功");
            }
        });


    }

    /**
     * http请求失败重试执行
     */
    @Override
    public void retryTask() {
        //查询 error_retry_task 表status=510 ，且current_count < retry_count 的数据进行重试
        ErrorRetryTask errorRetryTask = new ErrorRetryTask();
        errorRetryTask.setStatus(ErrorRetryTaskStatus.STATUS_ERROR.getCode());
        List<ErrorRetryTaskWithBLOBs> list = errorRetryTaskManualMapper.findList(errorRetryTask);
        if(list != null && list.size() > 0){
            //1、改状态为520执行中
            errorRetryTaskManualMapper.update(errorRetryTask);
            //2、for循环执行
            for(ErrorRetryTaskWithBLOBs task : list){
                try {
                    RetryHttpUtil.postStrWithJSON(task.getRequestUrl(), task.getRequestParam());
                    log.info("执行成功后改数据状态，error_retry_task current_count 加1 & status为500");
                    task.setStatus(500);
                    task.setRetryResult(300);
                } catch (Exception e) {
                    log.info("重试5次后修改数据状态，error_retry_task current_count 加1");
                    task.setStatus(510);
                    task.setRetryResult(310);log.error("重试5次后修改数据状态异常: {}", e);
                    //e.printStackTrace();
                }
                errorRetryTaskManualMapper.updateTask(task);
            }
        }
    }
}
