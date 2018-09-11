package com.jzfq.retail.core;

import com.alibaba.fastjson.JSON;
import com.jzfq.retail.bean.vo.res.TakeCodeOrderRes;
import com.jzfq.retail.core.api.service.GoodsStockService;
import com.jzfq.retail.core.config.CoreApplication;
import com.jzfq.retail.core.dao.manual.GoodsStockManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @Title: GoodsStockServiceTest
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月03日 11:30
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class GoodsStockServiceTest {

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private GoodsStockManualMapper goodsStockManualMapper;


    @Test
    public void getOrderInfoTest(){
        TakeCodeOrderRes takeCodeOrderRes = goodsStockService.getOrderInfo("9996336",1233);
        log.info("返回对象：{}",takeCodeOrderRes);
    }

    @Test
    public void selectByProductId(){
        List<Map<String, Object>> maps = goodsStockManualMapper.selectByProductId(8213);
        System.out.println(JSON.toJSON(maps));
    }




}
