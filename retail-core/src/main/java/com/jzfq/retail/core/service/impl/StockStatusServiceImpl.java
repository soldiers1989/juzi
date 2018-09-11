package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.StockStatus;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.StockStatusService;
import com.jzfq.retail.core.dao.StockStatusMapper;
import com.jzfq.retail.core.dao.manual.StockStatusManualMapper;
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
public class StockStatusServiceImpl implements StockStatusService {

    @Autowired
    private StockStatusMapper stockStatusMapper;

    @Autowired
    private StockStatusManualMapper stockStatusManualMapper;

    @Override
    public List<StockStatus> getAllList(StockStatus search) {
        List<StockStatus> result = stockStatusManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<StockStatus> getList(Integer page, Integer pageSize, StockStatus search) {
        PageHelper.startPage(page, pageSize);
        Page<StockStatus> listPage = stockStatusManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(StockStatus entity) {
        stockStatusMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockStatus entity) {
        StockStatus stockStatus = stockStatusMapper.selectByPrimaryKey(entity.getId());
        if (stockStatus == null) {
            throw new RuntimeException("要更新的数据不存在。");
        }
        stockStatusMapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        StockStatus stockStatus = stockStatusMapper.selectByPrimaryKey(id);
        if (stockStatus == null) {
            throw new RuntimeException("要删除的数据不存在。");
        }
        stockStatusMapper.deleteByPrimaryKey(id);
    }

    @Override
    public StockStatus getEntityById(Integer id) {
        StockStatus stockStatus = stockStatusMapper.selectByPrimaryKey(id);
        if (stockStatus == null) {
            throw new RuntimeException("要查询的数据不存在。");
        }
        return stockStatusMapper.selectByPrimaryKey(id);
    }

}
