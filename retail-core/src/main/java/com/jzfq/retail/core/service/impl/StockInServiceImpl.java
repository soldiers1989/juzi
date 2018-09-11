package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.PurchaseCollectGoodsReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.StockInItemRes;
import com.jzfq.retail.bean.vo.res.StockInRes;
import com.jzfq.retail.core.api.service.StockInService;
import com.jzfq.retail.core.dao.PurchaseCollectGoodsInfoMapper;
import com.jzfq.retail.core.dao.PurchaseCollectGoodsMapper;
import com.jzfq.retail.core.dao.PurchaseCollectMapper;
import com.jzfq.retail.core.dao.manual.StockInManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: 入库表接口实现
 */
@SuppressWarnings("ALL")
@Service
public class StockInServiceImpl implements StockInService {

    @Autowired
    private PurchaseCollectMapper purchaseCollectMapper;

    @Autowired
    private PurchaseCollectGoodsMapper purchaseCollectGoodsMapper;

    @Autowired
    private PurchaseCollectGoodsInfoMapper purchaseCollectGoodsInfoMapper;

    @Autowired
    private StockInManualMapper stockInManualMapper;

    @Override
    public List<StockInRes> getAllList(PurchaseCollectGoodsReq search) {
        List<StockInRes> result = new ArrayList<StockInRes>();
        PurchaseCollectQuery purchaseCollectQuery = new PurchaseCollectQuery();
        purchaseCollectQuery.createCriteria()
                .andIdEqualTo(search.getId())
                .andCollectStatusEqualTo(search.getCollectStatus());

        List<PurchaseCollect> stockInHeader = purchaseCollectMapper.selectByExample(purchaseCollectQuery);

        int size = stockInHeader.size();
        for (PurchaseCollect purchaseCollect : stockInHeader) {
            PurchaseCollectGoodsQuery purchaseCollectGoodsQuery = new PurchaseCollectGoodsQuery();
            purchaseCollectGoodsQuery.createCriteria()
                    .andPurchaseCollectIdEqualTo(purchaseCollect.getId());

            List<StockInItemRes> stockInItemResList = new ArrayList<>();
            List<PurchaseCollectGoods> stockInItems = purchaseCollectGoodsMapper.selectByExample(purchaseCollectGoodsQuery);
            for (PurchaseCollectGoods purchaseCollectGoods : stockInItems) {
                PurchaseCollectGoodsInfoQuery purchaseCollectGoodsInfoQuery = new PurchaseCollectGoodsInfoQuery();
                purchaseCollectGoodsInfoQuery.createCriteria()
                        .andPurchaseCollectGoodsIdEqualTo(purchaseCollectGoods.getId());

                List<PurchaseCollectGoodsInfo> stockInItemInfos = purchaseCollectGoodsInfoMapper.selectByExample(purchaseCollectGoodsInfoQuery);

                StockInItemRes stockInItemRes = new StockInItemRes();
                stockInItemRes.setStockInItems(purchaseCollectGoods);
                stockInItemRes.setStockInItemInfos(stockInItemInfos);

                stockInItemResList.add(stockInItemRes);
            }

            StockInRes stockInRes =  new StockInRes();
            stockInRes.setStockInHeader(purchaseCollect);
            stockInRes.setStockInItems(stockInItemResList);
            result.add(stockInRes);
        }

        return result;
    }

    @Override
    public ListResultRes<StockInRes> getList(Integer page, Integer pageSize, PurchaseCollectGoodsReq search) {
        PageHelper.startPage(page, pageSize);
        List<StockInRes> listPage = getAllList(search);
        int size = listPage.size();
        Long total = new Long((long)size);
        return ListResultRes.newListResult(listPage, total, listPage.size() / pageSize, pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PurchaseCollectGoods entity) {
        purchaseCollectGoodsMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseCollectGoods entity) {
        PurchaseCollectGoods purchaseCollectGoods = purchaseCollectGoodsMapper.selectByPrimaryKey(entity.getId());
        if (purchaseCollectGoods == null) {
            throw new RuntimeException("要更新的数据不存在。");
        }
        purchaseCollectGoodsMapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        PurchaseCollectGoods purchaseCollectGoods = purchaseCollectGoodsMapper.selectByPrimaryKey(id);
        if (purchaseCollectGoods == null) {
            throw new RuntimeException("要删除的数据不存在。");
        }
        purchaseCollectGoodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PurchaseCollectGoods getEntityById(Integer id) {
        PurchaseCollectGoods purchaseCollectGoods = purchaseCollectGoodsMapper.selectByPrimaryKey(id);
        if (purchaseCollectGoods == null) {
            throw new RuntimeException("要查询的数据不存在。");
        }
        return purchaseCollectGoodsMapper.selectByPrimaryKey(id);
    }

}
