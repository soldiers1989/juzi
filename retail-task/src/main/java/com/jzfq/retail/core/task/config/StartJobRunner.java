package com.jzfq.retail.core.task.config;

import com.jzfq.retail.core.task.timer.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 项目启动时执行一次，是为了首次上线时生成定时任务；
 * 当定时任务设定之后就不需要执行；
 * 那么第二次以后再启动时还会执行，但job的名称一致则不会插入到库中
 * 2018-8-8 LiuWei
 */
@Slf4j
@Component
public class StartJobRunner implements ApplicationRunner {

    /**
     * 注入任务调度器
     */
    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(ApplicationArguments var1) throws Exception {
        log.info("StartJobRunner class will be execute when the project was started!");
        startJob();
    }

// Cron表达式范例：
//    每隔5秒执行一次：*/5 * * * * ?
//    每隔1分钟执行一次：0 */1 * * * ?
//    每天23点执行一次：0 0 23 * * ?
//    每天凌晨1点执行一次：0 0 1 * * ?
//    每月1号凌晨1点执行一次：0 0 1 1 * ?
//    每月最后一天23点执行一次：0 0 23 L * ?
//    每周星期天凌晨1点实行一次：0 0 1 ? * L
//    在26分、29分、33分执行一次：0 26,29,33 * * * ?
//    每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?

    public void startJob() throws Exception {
        log.info("======================启动定时任务开始=====================");

        //http请求失败重试任务  每5分钟执行一次  (cron = "0 0/5 * * * ?")
//        buildErrorRetryTimer();
        //定时任务，将待收货状态自动变成已收货 155 -》 160  每天凌晨执行一次
//        buildChangeStatusTimer();
        //定时任务，将订单待支付状态更新为已取消 110 -》 200 每小时执行一次
//        buildOrderAwaitPayToCancleTimer();
        log.info("======================启动定时任务结束=====================");

        //构建创建商品定时任务
//        buildCreateGoodTimer();
        //构建商品库存定时任务
//        buildGoodStockTimer();
        //构建商品描述提醒定时任务
//        buildGoodSecKillRemindTimer(good.getId());
    }

    /**
     * 构建http请求失败重试任务
     */
    public void buildErrorRetryTimer() throws Exception {
        //设置开始时间为1分钟后
        long startAtTime = System.currentTimeMillis() + 1000 * 60;
        //任务名称
//        String name = UUID.randomUUID().toString();
        String name = "请求失败重试任务";//名称相同，则第一次添加后，第二次以后执行不会再次添加
        //任务所属分组
        String group = ErrorRetryTimer.class.getName();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */5 * * * ?");//每5分钟执行一次
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(ErrorRetryTimer.class).withIdentity(name, group).build();
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 构建变更状态任务
     */
    public void buildChangeStatusTimer() throws Exception {
        //设置开始时间为1分钟后
        long startAtTime = System.currentTimeMillis() + 1000 * 60;
        //任务名称
//        String name = UUID.randomUUID().toString();
        String name = "定时任务，将待收货状态自动变成已收货";//名称相同，则第一次添加后，第二次以后执行不会再次添加
        //任务所属分组
        String group = ChangeStatusTimer.class.getName();
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 2 * * ?");//每天凌晨2点执行一次
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */1 * * * ?");//每天凌晨1点执行一次
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0/2 * * ?");//每2小时执行一次

        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(ChangeStatusTimer.class).withIdentity(name, group).build();
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }






    /**
     * 构建创建商品定时任务
     */
    public void buildCreateGoodTimer() throws Exception {
        //设置开始时间为1分钟后
        long startAtTime = System.currentTimeMillis() + 1000 * 60;
        //任务名称
//        String name = UUID.randomUUID().toString();
        String name = "构建创建商品定时任务123";//名称相同，则第一次添加后，第二次以后执行不会再次添加
        //任务所属分组
        String group = GoodAddTimer.class.getName();
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(GoodAddTimer.class).withIdentity(name, group).build();
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(new Date(startAtTime)).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 构建商品库存定时任务
     *
     * @throws Exception
     */
    public void buildGoodStockTimer() throws Exception {
        //任务名称
//        String name = UUID.randomUUID().toString();
        String name = "构建商品库存定时任务";//名称相同，则第一次添加后，第二次以后执行不会再次添加
        //任务所属分组
        String group = GoodStockCheckTimer.class.getName();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/30 * * * * ?");
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(GoodStockCheckTimer.class).withIdentity(name, group).build();
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 构建商品秒杀提醒定时任务
     * 设置五分钟后执行
     *
     * @throws Exception
     */
    public void buildGoodSecKillRemindTimer(Long goodId) throws Exception {
        //任务名称
//        String name = UUID.randomUUID().toString();
        String name = "构建商品秒杀提醒定时任务";//名称相同，则第一次添加后，第二次以后执行不会再次添加
        //任务所属分组
        String group = GoodSecKillRemindTimer.class.getName();
        //秒杀开始时间
        long startTime = System.currentTimeMillis() + 1000 * 5 * 60;
        JobDetail jobDetail = JobBuilder
                .newJob(GoodSecKillRemindTimer.class)
                .withIdentity(name, group)
                .build();

        //设置任务传递商品编号参数
        jobDetail.getJobDataMap().put("goodId", goodId);

        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(new Date(startTime)).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 构建变更状态任务
     */
    public void buildOrderAwaitPayToCancleTimer() throws Exception {
        //任务名称
        String name = "构建待支付订单更新为已取消定时任务";//名称相同，则第一次添加后，第二次以后执行不会再次添加
        //任务所属分组
        String group = OrderAwaitPayToCancleTimer.class.getName();
        //CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?");//每小时执行一次
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */3 * * * ?");//每5分钟执行一次

        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(OrderAwaitPayToCancleTimer.class).withIdentity(name, group).build();
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }
}