package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.GpsCheckRule;
import com.jzfq.retail.bean.domain.GpsCheckRuleQuery;
import com.jzfq.retail.bean.vo.req.GpsCheckRuleReq;
import com.jzfq.retail.bean.vo.req.GpsCheckRuleSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.GpsCheckFlag;
import com.jzfq.retail.common.enmu.GpsScopeFlag;
import com.jzfq.retail.common.enmu.GpsSwitchStatus;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.service.GpsCheckRuleService;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.dao.GpsCheckRuleMapper;
import com.jzfq.retail.core.dao.manual.GpsCheckRuleManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: GPS校验规则接口实现
 */
@Slf4j
@SuppressWarnings("ALL")
@Service
public class GpsCheckRuleServiceImpl implements GpsCheckRuleService {

    @Autowired
    private GpsCheckRuleMapper gpsCheckRuleMapper;

    @Autowired
    private GpsCheckRuleManualMapper gpsCheckRuleManualMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, GpsCheckRuleSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = gpsCheckRuleManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(GpsCheckRuleReq req, String username) {
        GpsCheckRule dest = new GpsCheckRule();
        TransferUtil.transferIgnoreNull(req, dest);
        dest.setCreateTime(new Date());
        gpsCheckRuleMapper.insert(dest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GpsCheckRuleReq req, String username) {
        GpsCheckRule dest = gpsCheckRuleMapper.selectByPrimaryKey(req.getId());
        TransferUtil.transferIgnoreNull(req, dest);
        gpsCheckRuleMapper.updateByPrimaryKey(dest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        gpsCheckRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public GpsCheckRule getEntityById(Integer id) {
        return gpsCheckRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean isCheckGps(GpsCheckFlag gpsCheckFlag) {
        Boolean isCheck = false;
        GpsCheckRuleQuery gpsCheckRuleQuery = new GpsCheckRuleQuery();
        gpsCheckRuleQuery.or().andFlagEqualTo(gpsCheckFlag.getFlag());
        List<GpsCheckRule> gpsCheckRules = gpsCheckRuleMapper.selectByExample(gpsCheckRuleQuery);
        // 正常数据
        if (!CollectionUtils.isEmpty(gpsCheckRules) && gpsCheckRules.size() == 1) {
            GpsCheckRule gpsCheckRule = gpsCheckRules.get(0);
            if (GpsScopeFlag.GPS_YES_GLOBAL.getFlag() == gpsCheckRule.getIsGlobal()  // 全局
                    && GpsSwitchStatus.GPS_RULE_OPEN.getStatus() == gpsCheckRule.getIsOpen()) { // 打开
                isCheck = true;
            }
        } else {
            String exDesc = CollectionUtils.isEmpty(gpsCheckRules) ? "当前数据为空!" : "记录数不唯一，记录数：" + gpsCheckRules.size();
            log.error("当前数据存在异常，异常描述：{}", exDesc);
            throw new BusinessException("当前数据存在异常，" + exDesc);
        }
        return isCheck;
    }

    @Override
    public GpsCheckRule getEntityByFlag(String flag) {
        GpsCheckRuleQuery GCRQ = new GpsCheckRuleQuery();
        GCRQ.createCriteria().andFlagEqualTo(flag);
        List<GpsCheckRule> gpsCheckRules = gpsCheckRuleMapper.selectByExample(GCRQ);
        if (gpsCheckRules != null && gpsCheckRules.size() > 0) {
            return gpsCheckRules.get(0);
        }
        return null;
    }
}
