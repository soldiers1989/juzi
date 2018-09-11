package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.OpenidRecord;
import com.jzfq.retail.bean.domain.OpenidRecordQuery;
import com.jzfq.retail.common.enmu.AuthStatus;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.OpenidRecordService;
import com.jzfq.retail.core.dao.OpenidRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月21日 19:42
 * @Description: openid记录接口操作实现
 */
@Service
public class OpenidRecordServiceImpl implements OpenidRecordService {

    @Autowired
    private OpenidRecordMapper openidRecordMapper;

    @Autowired
    private TaskExecutor taskExecutor;
    /**
     * 插入未绑定的openid
     * @param openId
     */
    @Override
    public void saveOpenidRecord(String openId, Integer type) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int flag = queryOpenId(openId);
                if(flag == 0) {//插入时判断库里没有数据才插入
                    OpenidRecord openidRecord = new OpenidRecord();
                    openidRecord.setCreateTime(DateUtil.getDate());
                    openidRecord.setOpenId(openId);
                    openidRecord.setType(type);
                    openidRecord.setStatus(AuthStatus.ORDER_SOURCE_0.getCode());//OPENID用户状态：0未认证，1认证审核中，2，认证审核成功，3认证审核失败
                    openidRecordMapper.insert(openidRecord);
                }
            }
        });
    }

    @Override
    public void saveOrUpdate(String openId, Integer memberId, Integer code) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                OpenidRecord openidRecord = queryOpenidRecordOpenId(openId);
                if(openidRecord != null) {//此处判断大于0插入，是因为大于0时，是用户之前没有绑定过，所有插入到数据库中，此表只保存从未认证过的用户数据
                    //更新第一条数据的memberId
                    openidRecord.setMemberId(String.valueOf(memberId));
                    openidRecord.setAuthTime(DateUtil.getDate());
                    openidRecord.setStatus(code);//OPENID用户状态：0未认证，1认证审核中，2，认证审核成功，3认证审核失败
                    openidRecordMapper.updateByPrimaryKey(openidRecord);
                }
            }
        });
    }

    /**
     * 查询openid
     * @param openId
     */
    @Override
    public int queryOpenId(String openId) {
        OpenidRecord openidRecord = queryOpenidRecordOpenId(openId);
        if (openidRecord != null){
            return 1;
        }
        return 0;
    }

    @Override
    public OpenidRecord queryOpenidRecordOpenId(String openId) {
        OpenidRecordQuery query = new OpenidRecordQuery();
        query.createCriteria().andOpenIdEqualTo(openId);
        List<OpenidRecord> openidRecords = openidRecordMapper.selectByExample(query);
        if (openidRecords != null && openidRecords.size() == 1){
            return openidRecords.get(0);
        }
        return null;
    }


    /**
     * 更新openId认证状态
     * @param openId
     */
    @Override
    public void updateOpenIdStatus(String openId) {
        OpenidRecord openidRecord = queryOpenidRecordOpenId(openId);
        openidRecord.setStatus(AuthStatus.ORDER_SOURCE_1.getCode());//OPENID用户状态：0未绑定，1已认证
        openidRecord.setAuthTime(DateUtil.getDate());
        openidRecordMapper.updateByPrimaryKey(openidRecord);

    }
}
