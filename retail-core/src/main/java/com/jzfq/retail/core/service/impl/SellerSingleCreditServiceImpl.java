package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.SellerSingleCredit;
import com.jzfq.retail.bean.domain.SellerSingleCreditQuery;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.SellerSingleCreditService;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.dao.SellerSingleCreditMapper;
import com.jzfq.retail.core.dao.manual.SellerSingleCreditManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:02
 * @Description: 商户单笔授信接口实现
 */
@SuppressWarnings("ALL")
@Service
public class SellerSingleCreditServiceImpl implements SellerSingleCreditService {

    @Autowired
    private SellerSingleCreditMapper sellerSingleCreditMapper;

    @Autowired
    private SellerSingleCreditManualMapper sellerSingleCreditManualMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SellerSingleCredit> getAllList(SellerSingleCredit search) {
        List<SellerSingleCredit> result = sellerSingleCreditManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<SellerSingleCredit> getList(Integer page, Integer pageSize, SellerSingleCredit search) {
        PageHelper.startPage(page, pageSize);
        Page<SellerSingleCredit> listPage = sellerSingleCreditManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SellerSingleCredit sellerSingleCredit) {
        if (getSellerSingleCreditOf(sellerSingleCredit.getSellerId()) != null) {
            throw new RuntimeException("该商户信息已存在，不能重复创建");
        }
        sellerSingleCreditMapper.insert(sellerSingleCredit);
    }

    private SellerSingleCredit getSellerSingleCreditOf(int sellerId) {
        if (sellerId <= 0) {
            throw new RuntimeException("商户ID要是正数。");
        }

        SellerSingleCreditQuery sellerSingleCreditQuery = new SellerSingleCreditQuery();
        sellerSingleCreditQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerSingleCredit> sellerSingleCreditList = sellerSingleCreditMapper.selectByExample(sellerSingleCreditQuery);
        if (sellerSingleCreditList != null && sellerSingleCreditList.size() == 1) {
            SellerSingleCredit credit = sellerSingleCreditList.get(0);
            return credit;
        }

        return  null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SellerSingleCredit sellerSingleCredit) {
        Integer creditId = sellerSingleCredit.getId();
        if (creditId == null) {
            Integer sellerId = sellerSingleCredit.getSellerId();
            SellerSingleCredit mapperSellerSingleCredit = getSellerSingleCreditOf(sellerId);
            if (mapperSellerSingleCredit == null) {
                //throw new RuntimeException("要更新的商户单笔授信数据不存在。");
                sellerSingleCreditMapper.insert(sellerSingleCredit);
                return;
            } else {
                Integer mapperSellerSingleSingleCreditId = mapperSellerSingleCredit.getId();
                sellerSingleCredit.setId(mapperSellerSingleSingleCreditId);
            }
        }
        sellerSingleCreditMapper.updateByPrimaryKeySelective(sellerSingleCredit);
    }

    @Override
    public void updateCredit(Integer id, int openCredit, Long creditValue, String editor) {
        SellerSingleCredit sellerSingleCredit = sellerSingleCreditMapper.selectByPrimaryKey(id);
        if (sellerSingleCredit == null) {
            throw new RuntimeException("要更新的商户单笔授信数据不存在。");
        }

        sellerSingleCredit.setIsOpen(openCredit);
        if (creditValue > 0) {
            sellerSingleCredit.setCreditLimit(creditValue);
        }
        sellerSingleCredit.setUpdateUser(editor);
        sellerSingleCredit.setUpdateTime(new Date());
        sellerSingleCreditMapper.updateByPrimaryKey(sellerSingleCredit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        SellerSingleCredit sellerSingleCredit = sellerSingleCreditMapper.selectByPrimaryKey(id);
        if (sellerSingleCredit == null) {
            throw new RuntimeException("要删除的商户单笔授信数据不存在。");
        }
        sellerSingleCreditMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SellerSingleCredit getEntityById(Integer id) {
        SellerSingleCredit sellerSingleCredit = sellerSingleCreditMapper.selectByPrimaryKey(id);
        if (sellerSingleCredit == null) {
            throw new RuntimeException("要查询的商户单笔授信数据不存在。");
        }
        return sellerSingleCreditMapper.selectByPrimaryKey(id);
    }

    @Override
    public SellerSingleCredit getEntityBySellerId(Integer sellerId) {
        return getSellerSingleCreditOf(sellerId);
    }

    @Override
    public void openCredit(Integer sellerId, Integer isOpen, String username) {
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        if(byNickName == null){
            throw new RuntimeException("用户为空");
        }
        SellerSingleCredit sellerSingleCredit = getEntityBySellerId(sellerId);
        if(sellerSingleCredit == null){
            sellerSingleCredit = new SellerSingleCredit();
            sellerSingleCredit.setSellerId(sellerId);
            sellerSingleCredit.setIsOpen(1);
            sellerSingleCredit.setCreateId(byNickName.getId());
            sellerSingleCredit.setCreateUser(username);
            sellerSingleCredit.setCreateTime(new Date());
            create(sellerSingleCredit);
        }
        sellerSingleCredit.setIsOpen(isOpen);
        sellerSingleCredit.setUpdateUser(username);
        sellerSingleCredit.setUpdateId(byNickName.getId());
        sellerSingleCredit.setUpdateTime(new Date());
        sellerSingleCreditMapper.updateByPrimaryKey(sellerSingleCredit);
    }
}