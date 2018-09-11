package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.StockAddress;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.StockAddressService;
import com.jzfq.retail.core.dao.StockAddressMapper;
import com.jzfq.retail.core.dao.manual.StockAddressManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: 库存地点接口实现
 */
@SuppressWarnings("ALL")
@Service
public class StockAddressServiceImpl implements StockAddressService {

    @Autowired
    private StockAddressMapper stockAddressMapper;

    @Autowired
    private StockAddressManualMapper stockAddressManualMapper;

    @Override
    public List<StockAddress> getAllList(StockAddress search) {
        List<StockAddress> result = stockAddressManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<StockAddress> getList(Integer page, Integer pageSize, StockAddress search) {
        PageHelper.startPage(page, pageSize);
        Page<StockAddress> listPage = stockAddressManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(StockAddress entity) {
        stockAddressMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockAddress entity) {
        StockAddress stockAddress = stockAddressMapper.selectByPrimaryKey(entity.getId());
        if (stockAddress == null) {
            throw new RuntimeException("要更新的数据不存在。");
        }
        stockAddressMapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        StockAddress stockAddress = stockAddressMapper.selectByPrimaryKey(id);
        if (stockAddress == null) {
            throw new RuntimeException("要删除的数据不存在。");
        }
        stockAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public StockAddress getEntityById(Integer id) {
        StockAddress stockAddress = stockAddressMapper.selectByPrimaryKey(id);
        if (stockAddress == null) {
            throw new RuntimeException("要查询的数据不存在。");
        }
        return stockAddressMapper.selectByPrimaryKey(id);
    }

}
