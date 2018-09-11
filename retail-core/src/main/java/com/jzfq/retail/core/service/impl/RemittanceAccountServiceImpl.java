package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.RemittanceAccount;
import com.jzfq.retail.bean.domain.RemittanceAccountQuery;
import com.jzfq.retail.bean.vo.req.RemittanceAccountReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.service.RemittanceAccountService;
import com.jzfq.retail.core.dao.RemittanceAccountMapper;
import com.jzfq.retail.core.dao.SellerAccountMapper;
import com.jzfq.retail.core.dao.manual.RemittanceAccountManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @Title: RemittanceAccountServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年07月18日 9:53
 * @Description: 打款账户表增删改
 */
@Slf4j
@Service
public class RemittanceAccountServiceImpl implements RemittanceAccountService {

    @Autowired
    private RemittanceAccountMapper remittanceAccountMapper;

    @Autowired
    private RemittanceAccountManualMapper remittanceAccountManualMapper;

    @Autowired
    private SellerAccountMapper sellerAccountMapper;

    @Override
    public int save(RemittanceAccountReq remittanceAccountReq) {
        int updateCount = 0;
        List<RemittanceAccount> remittanceAccounts = getRemittanceAccounts(remittanceAccountReq);
        // 库中不存在该商户对应的结算扣点，则新增；否则提示用户结算扣点已存在
        if(CollectionUtils.isEmpty(remittanceAccounts)) {
            RemittanceAccount remittanceAccount = new RemittanceAccount();
            TransferUtil.transfer(remittanceAccount, remittanceAccountReq);
            updateCount = remittanceAccountMapper.insert(remittanceAccount);
        } else {
            log.error("新增打款帐户异常，异常描述：打款帐户已经存在，打款帐户：{}，帐户编号：{}"
                    ,remittanceAccountReq.getAccountName(),remittanceAccountReq.getRemark1());
            throw new BusinessException("新增打款帐户异常，异常描述：打款帐户已经存在！");
        }
        return updateCount;
    }

    @Override
    public int update(RemittanceAccountReq remittanceAccountReq) {
        int updateCount = 0;
        List<RemittanceAccount> remittanceAccounts = getRemittanceAccounts(remittanceAccountReq);
        // 如果存在结算扣点且记录数为1，则为正常数据；否则提示用户数据异常
        if(!CollectionUtils.isEmpty(remittanceAccounts) && remittanceAccounts.size() == 1) {
            RemittanceAccount dbRemittanceAccount = remittanceAccounts.get(0);
            dbRemittanceAccount.setAccountName(remittanceAccountReq.getAccountName());
            dbRemittanceAccount.setRemark1(remittanceAccountReq.getRemark1());
            updateCount = remittanceAccountMapper.updateByPrimaryKey(dbRemittanceAccount);
        } else {
            if(CollectionUtils.isEmpty(remittanceAccounts)) {
                log.error("更新打款帐户异常，异常描述：打款帐户不存在，打款帐户：{}，帐户编号：{}"
                        ,remittanceAccountReq.getAccountName(),remittanceAccountReq.getRemark1());
                throw new BusinessException("更新商户结算扣点异常，异常描述：商户结算扣点不存在！");
            }
            if(!CollectionUtils.isEmpty(remittanceAccounts) && remittanceAccounts.size() != 1) {
                log.error("更新打款帐户异常，异常描述：打款帐户存在多条记录，记录数：{}，打款帐户：{}，帐户编号：{}"
                        ,remittanceAccounts.size(),remittanceAccountReq.getAccountName(),remittanceAccountReq.getRemark1());
                throw new BusinessException("更新商户结算扣点异常，异常描述：商户结算扣点存在多条记录！");
            }
        }
        return updateCount;
    }

    @Override
    public int delete(RemittanceAccountReq remittanceAccountReq) {
        int updateCount = 0;
        List<RemittanceAccount> remittanceAccounts = getRemittanceAccounts(remittanceAccountReq);
        // 如果存在结算扣点且记录数为1，则为正常数据；否则提示用户数据异常
        if(!CollectionUtils.isEmpty(remittanceAccounts) && remittanceAccounts.size() == 1) {
            RemittanceAccount dbRemittanceAccount = remittanceAccounts.get(0);
            updateCount = remittanceAccountMapper.deleteByPrimaryKey(dbRemittanceAccount.getId());
        } else {
            if(CollectionUtils.isEmpty(remittanceAccounts)) {
                log.error("删除打款帐户异常，异常描述：打款帐户不存在！");
                throw new BusinessException("删除打款帐户异常，异常描述：打款帐户不存在！");
            }
            if(!CollectionUtils.isEmpty(remittanceAccounts) && remittanceAccounts.size() != 1) {
                log.error("删除打款帐户异常，异常描述：打款帐户存在多条记录，记录数：{}，打款帐户：{}，帐户编号：{}"
                        ,remittanceAccounts.size(),remittanceAccountReq.getAccountName(),remittanceAccountReq.getRemark1());
                throw new BusinessException("删除打款帐户异常，异常描述：打款帐户存在多条记录！");
            }
        }
        return updateCount;
    }

    @Override
    public ListResultRes<HashMap<String,Object>> queryRemittanceAccountList(Integer page, Integer pageSize, String sellerName) {
        PageHelper.startPage(page, pageSize);
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sellerName",sellerName);
        Page<HashMap<String,Object>> listPage = remittanceAccountManualMapper.selectList(paramMap);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    /**
     * 根据打款账户、账户编号查询打款帐户
     * @param remittanceAccountReq
     * @return
     */
    private List<RemittanceAccount> getRemittanceAccounts(RemittanceAccountReq remittanceAccountReq) {
        // 查询该商户是否已经设置了结算扣点
        RemittanceAccountQuery query = new RemittanceAccountQuery();
        query.or().andAccountNameEqualTo(remittanceAccountReq.getAccountName())
                .andRemark1EqualTo(remittanceAccountReq.getRemark1());
        return remittanceAccountMapper.selectByExample(query);
    }
}
