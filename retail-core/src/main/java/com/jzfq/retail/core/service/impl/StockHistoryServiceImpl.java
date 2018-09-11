package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.StockHistory;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.StockHistoryService;
import com.jzfq.retail.core.dao.StockHistoryMapper;
import com.jzfq.retail.core.dao.manual.StockHistoryManualMapper;
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
public class StockHistoryServiceImpl implements StockHistoryService {

    @Autowired
    private StockHistoryMapper stockHistoryMapper;

    @Autowired
    private StockHistoryManualMapper stockHistoryManualMapper;

    @Override
    public List<StockHistory> getAllList(StockHistory search) {
        List<StockHistory> result = stockHistoryManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<StockHistory> getList(Integer page, Integer pageSize, StockHistory search) {
        PageHelper.startPage(page, pageSize);
        Page<StockHistory> listPage = stockHistoryManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(StockHistory entity) {
        stockHistoryMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockHistory entity) {
        StockHistory stockHistory = stockHistoryMapper.selectByPrimaryKey(entity.getId());
        if (stockHistory == null) {
            throw new RuntimeException("要更新的数据不存在。");
        }
        stockHistoryMapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        StockHistory stockHistory = stockHistoryMapper.selectByPrimaryKey(id);
        if (stockHistory == null) {
            throw new RuntimeException("要删除的数据不存在。");
        }
        stockHistoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public StockHistory getEntityById(Integer id) {
        StockHistory stockHistory = stockHistoryMapper.selectByPrimaryKey(id);
        if (stockHistory == null) {
            throw new RuntimeException("要查询的数据不存在。");
        }
        return stockHistoryMapper.selectByPrimaryKey(id);
    }

}
