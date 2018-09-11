package com.jzfq.retail.core;


import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.lock.RedissLockUtil;
import com.jzfq.retail.core.api.service.JtaAtomikosService;
import com.jzfq.retail.core.config.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;


/**
 * 分布式锁(redission)测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class JtaAtomikosTest {

    @Autowired
    private JtaAtomikosService jtaAtomikosService;

    @Test
    public void lockTest() throws Exception{
        ProductCate cate = new ProductCate();
        cate.setScaling(BigDecimal.ZERO);
        cate.setCreateTime(DateUtil.getDate());
        cate.setUpdateTime(DateUtil.getDate());
        cate.setCreateId(1);
        cate.setPid(2);
        cate.setSort(3);
        cate.setType(4);
        cate.setUpdateId(5);
        cate.setVisible(6);
        cate.setImage("www.baidu.com");
        cate.setName("baidu");
        cate.setPath("http://www.baidu.com");
        cate.setProductTypeId(7);

        jtaAtomikosService.addProductCate(cate);
    }


}
