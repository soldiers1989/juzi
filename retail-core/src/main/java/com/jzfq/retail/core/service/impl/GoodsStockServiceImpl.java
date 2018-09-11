package com.jzfq.retail.core.service.impl;

import com.juzifenqi.core.StringUtil;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.res.TakeCodeOrderRes;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.OSSUnitUtil;
import com.jzfq.retail.common.util.SymmetricEncoder;
import com.jzfq.retail.common.util.UUIDGenerator;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.common.util.lock.RedissLockUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.GoodsStockService;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import com.jzfq.retail.core.api.service.OrdersProductService;
import com.jzfq.retail.core.api.service.StockHistoryService;
import com.jzfq.retail.core.dao.GoodsStockInfoMapper;
import com.jzfq.retail.core.dao.GoodsStockMapper;
import com.jzfq.retail.core.dao.OrderImageMapper;
import com.jzfq.retail.core.dao.OrdersBaseMapper;
import com.jzfq.retail.core.dao.manual.OrdersBaseManualMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Administrator on 2018/08/03.
 */
@Slf4j
@Service
public class GoodsStockServiceImpl implements GoodsStockService {

    @Autowired
    private OrdersBaseManualMapper ordersBaseManualMapper;

    @Autowired
    private OrdersBaseMapper ordersBaseMapper;

    @Autowired
    private OrdersBaseService ordersBaseService;

    @Autowired
    private GoodsStockInfoMapper goodsStockInfoMapper;

    @Autowired
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    private OrderImageMapper orderImageMapper;

    @Autowired
    private OSSUnitUtil ossUnitUtil;

    @Autowired
    private OrdersProductService ordersProductService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Value("${alibaba.oss.bucket}")
    private String ossBucket;

    @Value("${aes.encrypt.decrypt.secretKey}")
    private String aesSecretKey;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private StockHistoryService stockHistoryService;

    /**
     * 根据提货码查询订单详情
     * @param tokeCode 提货码
     * @return
     */
    @Override
    public TakeCodeOrderRes getOrderInfo(String tokeCode,Integer sellerId) {
        TakeCodeOrderRes takeCodeOrderRes = ordersBaseManualMapper.findOrderByTackCode(tokeCode, sellerId);
        if(takeCodeOrderRes == null) {
            // 这里的提示以后再加，易冲突
            log.error("输入的验证码有误，请核实后再验证");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0060);
        }
        return takeCodeOrderRes;
    }

    /**
     * 提货操作
     * @param seqNum
     * @param orderSn
     */
    @Transactional
    @Override
    public void pickUpGoods(String seqNum,String orderSn) {
        OrdersBase ordersBase = ordersBaseService.getByOrderSn(orderSn);
        OrdersProduct ordersProduct = ordersProductService.getByOrderSn(orderSn);
        // normal校验
        normalCheck(seqNum,ordersBase,ordersProduct);

        // 扣减库存时加锁
        boolean isLock = RedissLockUtil.tryLock(RedissonKeyCode.GOODS_STOCK_KEY.getCode(),3,10);
        if(isLock) {
            try {
                // 扣减库存
                deductStock(seqNum,ordersBase,ordersProduct);
            } finally {
                // 释放锁
                RedissLockUtil.unlock(RedissonKeyCode.GOODS_STOCK_KEY.getCode());
            }
        } else {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0070);
        }
        // 当数据为0时说明货已取完，更新订单状态：
        ordersBase.setOrderState(OrderStatus.ORDER_STATE_160.getCode());
        ordersBase.setCodconfirmTime(DateUtil.getDate());//确认收货时间
        ordersBase.setDeliverTime(DateUtil.getDate());//发货时间
        ordersBaseManualMapper.modifyOrderStateByOrderSn(ordersBase);

        //添加订单操作日志
        systemLogSupport.orderLogSave(null, null, ordersBase.getId(), ordersBase.getOrderSn(), "商户端输入取货码提货完成", ordersBase.getOrderState());

    }

    private void normalCheck(String seqNum,OrdersBase ordersBase,OrdersProduct ordersProduct) {

        if(ordersBase == null) {
            log.error("订单不存在!");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0043);
        }
        if(ordersProduct == null) {
            log.error("订单商品不存在!");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1007);
        }
        GoodsStock goodsStock = getGoodsStock(ordersProduct.getProductGoodsId());
        if(goodsStock == null) {
            log.error("库存记录不存在!");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1005);
        }
        // 查询库存详情
        GoodsStockInfoQuery goodsStockInfoQuery = new GoodsStockInfoQuery();
        goodsStockInfoQuery.or().andGoodsStockIdEqualTo(goodsStock.getId());
        List<GoodsStockInfo> goodsStockInfos = goodsStockInfoMapper.selectByExample(goodsStockInfoQuery);
        if(CollectionUtils.isEmpty(goodsStockInfos)) {
            log.error("商品{商品名称}的序列号和库中不一致，请核实！");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0069);
        }
        List<String> list = new ArrayList<String>();
        for(GoodsStockInfo info : goodsStockInfos) {
           list.add(info.getSn());
        }
        if(!list.contains(seqNum)) {
            log.error("商品{商品名称}的序列号和库中不一致，请核实！");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0069);
        }
    }

    /**
     * 扣减库存可以处理3c电子产品（sn序列号），或非3c产品（批次号）
     * @param seqNum
     * @param ordersBase
     * @param ordersProduct
     */
    private void deductStock(String seqNum,OrdersBase ordersBase,OrdersProduct ordersProduct) {
        // 根据序列号查询订单详情
        GoodsStockInfoQuery goodsStockInfoQuery = new GoodsStockInfoQuery();
        goodsStockInfoQuery.or().andSnEqualTo(seqNum);
        List<GoodsStockInfo> goodsStockInfos = goodsStockInfoMapper.selectByExample(goodsStockInfoQuery);
        // 提货数量
        Integer pickUpCount = goodsStockInfos.size();
        // 校验库存详情
        checkGoodsStockInfo(goodsStockInfos);
        // 查询库存
        GoodsStock goodsStock = goodsStockMapper.selectByPrimaryKey(goodsStockInfos.get(0).getGoodsStockId());
        // 库存前数量
        Integer totalCount = goodsStock.getTotalCount();
        // 校验库存记录
        checkGoodsStock(goodsStock, pickUpCount);
        // 扣减库存并更新
        deductAndUpate(goodsStock, pickUpCount);
        // 扣减库存详情并更新
        deductAndUpate(goodsStockInfos);

        // save history log
        saveStockHistoryLog(seqNum, ordersBase, ordersProduct, goodsStockInfos, totalCount);

    }

    private void saveStockHistoryLog(String seqNum, OrdersBase ordersBase, OrdersProduct ordersProduct, List<GoodsStockInfo> goodsStockInfos, Integer totalCount) {
        taskExecutor.execute(()->{
            //添加库存出库日志  stock_history
            StockHistory stockHistory = new StockHistory();
            stockHistory.setGoodsStockInfoId(goodsStockInfos.get(0).getGoodsStockId());
            stockHistory.setSn(seqNum);
            stockHistory.setIntoCode(ordersBase.getOrderSn());
            stockHistory.setType(ordersBase.getOrderType());
            stockHistory.setUpdateTime(DateUtil.getDate());
            stockHistory.setProductId(ordersProduct.getId());
            stockHistory.setProductName(ordersProduct.getProductName());
            stockHistory.setProductGoodsId(ordersProduct.getProductGoodsId());
            stockHistory.setSku(ordersProduct.getProductSku());
            stockHistory.setSkuName(ordersProduct.getProductSku());
            stockHistory.setStockStatus(goodsStockInfos.get(0).getSnStatus());
            stockHistory.setCount(goodsStockInfos.size());
            stockHistory.setBeforeCount(totalCount);
            stockHistory.setMemberName(ordersBase.getMemberName());
            stockHistory.setMemberName(ordersBase.getMemberName());
            stockHistory.setTakeFlag(0);// 取货方式
            stockHistory.setAvailabilityDate(DateUtil.getDate());
            stockHistory.setStockAddressCode(goodsStockInfos.get(0).getStockAddressId() + "");
            stockHistory.setStockAddressName(goodsStockInfos.get(0).getStockAddressName());
            stockHistory.setStockSiteId(goodsStockInfos.get(0).getStockSiteId());
            stockHistory.setStockSiteName(goodsStockInfos.get(0).getStockSiteName());
            stockHistoryService.create(stockHistory);
        });
    }

    public GoodsStock getGoodsStock(Integer productGoodsId) {
        GoodsStockQuery goodsStockQuery = new GoodsStockQuery();
        goodsStockQuery.or().andProductGoodsIdEqualTo(productGoodsId);
        List<GoodsStock> goodsStocks = goodsStockMapper.selectByExample(goodsStockQuery);
        if (!CollectionUtils.isEmpty(goodsStocks)) {
            return goodsStocks.get(0);
        }
        return null;
    }

    /**
     * 留存照片
     * @param byteArrayList
     */
    @Transactional
    @Override
    public void preservePhotograph(String orderSn,List<byte[]> byteArrayList,List<String> suffixs) {
        // 查询订单
        OrdersBase ordersBase = getOrderBase(orderSn);

        for(int i = 0; i < byteArrayList.size(); i++) {
            // 入库对象
            OrderImage image = new OrderImage();
            // 后缀
            String suffix = suffixs.get(i);
            // 文件原始字节数组
            byte[] fileBytes = byteArrayList.get(i);
            // 获取加密字节数组
            byte[] encryptFileBytes = SymmetricEncoder.AESEncode(aesSecretKey,fileBytes);
            // 上传文件并
            String url = uploanFile(encryptFileBytes, suffix);
            image.setCreateTime(DateUtil.getDate());
            image.setUrl(url);
            image.setOrderSn(orderSn);
            // 类型暂不清楚
            image.setType(0);
            image.setOrderId(ordersBase.getId());
            orderImageMapper.insertSelective(image);
        }
    }

    private String uploanFile(byte[] bytes,String suffix) {
        // 将字节数组转换为字节输入流
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        // 访问时的图片上层路径
        String diskName = "images/upload/";
        String url = null;
        String key = UUIDGenerator.getUUID() + "." + suffix;
        String eTag = ossUnitUtil.uploadObject2OSS3(is, ossBucket, key, diskName);
        if(StringUtils.isNotBlank(eTag)) {
            url = ossUnitUtil.getUrl(diskName + key, ossBucket);
        }
        if (StringUtils.isBlank(url)) {
            log.error("OSS上传图片失败，返回地址为空！");
            throw new RuntimeException("OSS上传图片失败，返回地址为空！");
        }
        log.info("OSS上传图片地址返回：{}", url);
        url = url.split("\\?")[0];
        return url;
    }

    private OrdersBase getOrderBase(String orderSn) {
        OrdersBaseQuery ordersBaseQuery = new OrdersBaseQuery();
        ordersBaseQuery.or().andOrderSnEqualTo(orderSn);
        List<OrdersBase> ordersBases = ordersBaseMapper.selectByExample(ordersBaseQuery);
        if(!CollectionUtils.isEmpty(ordersBases)) {
            return ordersBases.get(0);
        }
        return null;
    }

    /**
     * 出库扣减详情库存
     * @param goodsStockInfos
     */
    private void deductAndUpate(List<GoodsStockInfo> goodsStockInfos) {
        // 扣减库存详情
        for(GoodsStockInfo stockInfo : goodsStockInfos) {
            if(stockInfo.getCount() == 1){
                // 修改为出库
                stockInfo.setSnStatus(SnStatus.OUT_STORAGE.getStatus());//不改
                // 更新出库时间
                stockInfo.setOutTime(DateUtil.getDate());//不改
            }
            stockInfo.setUpdateTime(DateUtil.getDate());
            stockInfo.setCount(stockInfo.getCount() - 1);
            // 更新库存详情
            goodsStockInfoMapper.updateByPrimaryKeySelective(stockInfo);
        }
    }

    /**
     * 出库扣减总库存
     * @param goodsStock
     * @param pickUpCount
     */
    private void deductAndUpate(GoodsStock goodsStock, Integer pickUpCount) {
        // 扣减数量
        Integer totalCount = goodsStock.getTotalCount();
        Integer frozenCount = goodsStock.getFrozenCount();
        goodsStock.setFrozenCount(frozenCount - pickUpCount);
        // 扣减速当前库存(无需更新)
        // 扣减总库存数据
        goodsStock.setTotalCount(totalCount - pickUpCount);
        goodsStock.setUpdateTime(DateUtil.getDate());
        // 更新库存
        goodsStockMapper.updateByPrimaryKeySelective(goodsStock);
    }

    private void checkGoodsStock(GoodsStock goodsStock, Integer pickUpCount) {
        if(goodsStock == null) {
            log.error("当前序列号对应的库存记录不存在！");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0068);
        }
        // 当前库存数据小于等于0且当前库存数量小于提货数量，则库存数量不充足
        if(goodsStock.getFrozenCount() <= 0 || goodsStock.getFrozenCount() < pickUpCount) {
            log.error("冻结库存数量不足，冻结库存数量：{}，提货数量：{}",goodsStock.getFrozenCount(),pickUpCount);
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0067);
        }
    }

    private void checkGoodsStockInfo(List<GoodsStockInfo> goodsStockInfos) {
        if (CollectionUtils.isEmpty(goodsStockInfos)) {
            log.error("商品{商品名称}的序列号和库中不一致，请核实！");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0069);
        }
        // 当前批次的序列号必须在商品库存表中有对应的一条记录
        Set<Integer> goodsStockIds = new HashSet<Integer>();
        for(GoodsStockInfo stockInfo : goodsStockInfos) {
            // 校验该商品是否已卖出
            if(!SnStatus.ENTER_STORAGE.getStatus().equals(stockInfo.getSnStatus())) {
                log.error("当前序列号对应的商品不在入库状态，无法出库，序列/批次号：{}，当前订单状态：{}",stockInfo.getSn(),SnStatus.getEnum(stockInfo.getSnStatus()));
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0065);
            }
            if(stockInfo.getCount() <= 0){
                log.error("当前库存数量不足，无法出库，序列/批次号：{}，当前订单状态：{}",stockInfo.getSn(),SnStatus.getEnum(stockInfo.getSnStatus()));
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1006);
            }
            goodsStockIds.add(stockInfo.getGoodsStockId());
        }
        if(goodsStockIds.size() != 1) {
            log.error("当前批次序列号不属于同一库存记录，存在异常数据！");
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0066);
        }
    }

    /**
     * 信用支付成功后冻结库存
     */
    @Override
    public void forzenStock(OrdersBase ordersBase){
        if (ordersBase.getOrderType().equals(OrderType._2.getCode())) {
            OrdersProduct ordersProduct = ordersProductService.getByOrderSn(ordersBase.getOrderSn());

            // 查询库存一直接扣减库存加锁
            boolean isLock = RedissLockUtil.tryLock(RedissonKeyCode.GOODS_STOCK_KEY.getCode(), 3, 10);
            if (isLock) {
                try {
                    // 增加冻结库存，减少当前库存
                    deductAndUpate(ordersProduct.getProductGoodsId());
                } finally {
                    // 释放锁
                    RedissLockUtil.unlock(RedissonKeyCode.GOODS_STOCK_KEY.getCode());
                }
            } else {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0070);
            }
        }
    }

    /**
     * 信用支付成功后，冻结库存
     *
     * @param productGoodsId
     */
    @Override
    public void deductAndUpate(Integer productGoodsId) {
        GoodsStock goodsStock = getGoodsStock(productGoodsId);
        // 扣减数量
        Integer totalCount = goodsStock.getTotalCount();
        Integer frozenCount = goodsStock.getFrozenCount();
        Integer currentCount = goodsStock.getCurrentCount();
        // 如果当前库存小于等于0，则库存不足
        if (currentCount <= 0) {
            throw new BusinessException("当前库存不足");
        }
        // 冻结库存 + 1
        goodsStock.setFrozenCount(frozenCount + 1);
        // 当前库存 - 1
        goodsStock.setCurrentCount(currentCount - 1);

        // 总库存不用更新
        // goodsStock.setTotalCount(totalCount - 1);
        goodsStock.setUpdateTime(DateUtil.getDate());
        // 更新库存
        goodsStockMapper.updateByPrimaryKeySelective(goodsStock);
    }

    /**
     * 释放冻结库存
     */
    @Override
    public void unFrozenStock(OrdersBase ordersBase, OrdersProduct ordersProduct){
        if(ordersBase.getOrderType().equals(OrderType._2.getCode())){ //1、扫码店订单，2、便利店订单
            //0、将冻结的库存释放
            boolean isLock = RedissLockUtil.tryLock(RedissonKeyCode.GOODS_STOCK_KEY.getCode(), 3, 10);
            if (isLock) {
                try {
                    // 恢复冻结库存
                    recoveryAndUpate(ordersProduct.getProductGoodsId());
                } finally {
                    // 释放锁
                    RedissLockUtil.unlock(RedissonKeyCode.GOODS_STOCK_KEY.getCode());
                }
            } else {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0070);
            }
        }
    }

    /**
     * 恢复冻结库存
     * @param productGoodsId
     */
    public void recoveryAndUpate(Integer productGoodsId) {
        GoodsStock goodsStock = getGoodsStock(productGoodsId);
        // 扣减数量
        Integer totalCount = goodsStock.getTotalCount();
        Integer frozenCount = goodsStock.getFrozenCount();
        Integer currentCount = goodsStock.getCurrentCount();
        // 冻结库存 - 1
        goodsStock.setFrozenCount(frozenCount - 1);
        // 当前库存 + 1
        goodsStock.setCurrentCount(currentCount + 1);
        // 总库存不用更新
        // goodsStock.setTotalCount(totalCount - 1);
        goodsStock.setUpdateTime(DateUtil.getDate());
        // 更新库存
        goodsStockMapper.updateByPrimaryKeySelective(goodsStock);
    }

}
