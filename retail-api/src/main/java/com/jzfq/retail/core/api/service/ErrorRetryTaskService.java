package com.jzfq.retail.core.api.service;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月3日 19:02
 * @Description: 失败重试操作接口
 */
public interface ErrorRetryTaskService {

    void add(int type, String url, String requestParam, String responseParam, int retryCount, int currentCount, int status);

    void retryTask();
}
