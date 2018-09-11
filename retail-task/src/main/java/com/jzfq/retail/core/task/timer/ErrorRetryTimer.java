package com.jzfq.retail.core.task.timer;

import com.jzfq.retail.core.api.service.ErrorRetryTaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 失败重试任务
 * Date：2018/8/8
 * Time：17:47
 * @author LiuWei
 */
@Slf4j
public class ErrorRetryTimer extends QuartzJobBean {

    @Autowired
    ErrorRetryTaskService errorRetryTaskService;

    /**
     * 定时任务逻辑实现方法
     * 每当触发器触发时会执行该方法逻辑
     *
     * @param jobExecutionContext 任务执行上下文
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("HTTP请求失败后重试任务，每5分钟执行一次，任务时间：{}", new Date());
        errorRetryTaskService.retryTask();
    }
}
