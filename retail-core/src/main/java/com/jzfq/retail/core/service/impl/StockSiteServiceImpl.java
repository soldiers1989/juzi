package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.StockSite;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.StockSiteService;
import com.jzfq.retail.core.dao.StockSiteMapper;
import com.jzfq.retail.core.dao.manual.StockSiteManualMapper;
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
public class StockSiteServiceImpl implements StockSiteService {

    @Autowired
    private StockSiteMapper stockSiteMapper;

    @Autowired
    private StockSiteManualMapper stockSiteManualMapper;

    @Override
    public List<StockSite> getAllList(StockSite search) {
        List<StockSite> result = stockSiteManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<StockSite> getList(Integer page, Integer pageSize, StockSite search) {
        PageHelper.startPage(page, pageSize);
        Page<StockSite> listPage = stockSiteManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(StockSite entity) {
        stockSiteMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockSite entity) {
        StockSite stockSite = stockSiteMapper.selectByPrimaryKey(entity.getId());
        if (stockSite == null) {
            throw new RuntimeException("要更新的数据不存在。");
        }
        stockSiteMapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        StockSite stockSite = stockSiteMapper.selectByPrimaryKey(id);
        if (stockSite == null) {
            throw new RuntimeException("要删除的数据不存在。");
        }
        stockSiteMapper.deleteByPrimaryKey(id);
    }

    @Override
    public StockSite getEntityById(Integer id) {
        StockSite stockSite = stockSiteMapper.selectByPrimaryKey(id);
        if (stockSite == null) {
            throw new RuntimeException("要查询的数据不存在。");
        }
        return stockSiteMapper.selectByPrimaryKey(id);
    }

}
