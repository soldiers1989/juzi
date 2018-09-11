package com.jzfq.retail.core.config.rabbit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lagon
 * @time 2017/10/18 9:44
 * @description rabbitmq基础配置类
 */
@Configuration
public class RabbitBaseConfig {

    @Bean("mqTaskExecutor")
    public ThreadPoolTaskExecutor mqTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor=new ThreadPoolTaskExecutor();
        //核心线程数，默认为1
        taskExecutor.setCorePoolSize(50);
        //最大线程数，默认为Integer.MAX_VALUE
        taskExecutor.setMaxPoolSize(150);
        //队列最大长度，一般需要设置值>=notifyScheduledMainExecutor.maxNum；默认为Integer.MAX_VALUE
        taskExecutor.setQueueCapacity(1000);
        //线程池维护线程所允许的空闲时间，默认为60s
        taskExecutor.setKeepAliveSeconds(300);
        /**
         线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
         AbortPolicy:直接抛出java.util.concurrent.RejectedExecutionException异常
         CallerRunsPolicy:主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度
         DiscardOldestPolicy:抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行
         DiscardPolicy:抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }

}
