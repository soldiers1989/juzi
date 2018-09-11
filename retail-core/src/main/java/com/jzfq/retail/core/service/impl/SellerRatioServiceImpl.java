package com.jzfq.retail.core.service.impl;

import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.SellerRatio;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.SellerRatioService;
import com.jzfq.retail.core.dao.SellerRatioMapper;
import com.jzfq.retail.core.dao.manual.SellerRatioManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:02
 * @Description: 商户分期接口实现
 */
@SuppressWarnings("ALL")
@Service
public class SellerRatioServiceImpl implements SellerRatioService {

    @Autowired
    private SellerRatioMapper sellerTermMapper;

    @Autowired
    private SellerRatioManualMapper sellerTermManualMapper;

    @Override
    public List<SellerRatio> getAllList(SellerRatio search) {
        List<SellerRatio> result = sellerTermManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<SellerRatio> getList(Integer page, Integer pageSize, SellerRatio search) {
        PageHelper.startPage(page, pageSize);
        Page<SellerRatio> listPage = sellerTermManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SellerRatio sellerRatio) {
        sellerTermMapper.insert(sellerRatio);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SellerRatio sellerRatio) {
        SellerRatio sellerTerm1 = sellerTermMapper.selectByPrimaryKey(sellerRatio.getId());
        if (sellerTerm1 == null) {
            throw new RuntimeException("要更新的商户数据不存在。");
        }
        sellerTermMapper.updateByPrimaryKey(sellerRatio);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        SellerRatio sellerRatio = sellerTermMapper.selectByPrimaryKey(id);
        if (sellerRatio == null) {
            throw new RuntimeException("要删除的商户数据不存在。");
        }
        sellerTermMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SellerRatio getEntityById(Integer id) {
        SellerRatio sellerRatio = sellerTermMapper.selectByPrimaryKey(id);
        if (sellerRatio == null) {
            throw new RuntimeException("要查询的商户数据不存在。");
        }
        return sellerTermMapper.selectByPrimaryKey(id);
    }

}
