package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.vo.req.WithdrawRecordSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.WithdrawRecordService;
import com.jzfq.retail.core.dao.manual.WithdrawRecordManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class WithdrawRecordServiceImpl implements WithdrawRecordService {

    @Autowired
    WithdrawRecordManualMapper withdrawRecordManualMapper;

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, WithdrawRecordSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = withdrawRecordManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }
}
