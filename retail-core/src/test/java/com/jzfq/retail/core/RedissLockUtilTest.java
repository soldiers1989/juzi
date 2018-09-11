package com.jzfq.retail.core;


import com.jzfq.retail.common.util.lock.RedissLockUtil;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 分布式锁(redission)测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class RedissLockUtilTest {

    @Test
    public void lockTest() throws Exception{
        RLock lock = RedissLockUtil.lock("123");
        System.out.println(lock.isLocked());
    }


}
