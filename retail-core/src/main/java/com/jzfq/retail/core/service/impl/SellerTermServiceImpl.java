package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.SellerTerm;
import com.jzfq.retail.bean.domain.SellerTermQuery;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.req.SellerTermSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerTermService;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.dao.SellerTermMapper;
import com.jzfq.retail.core.dao.manual.SellerTermManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:02
 * @Description: 商户分期接口实现
 */
@SuppressWarnings("ALL")
@Service
public class SellerTermServiceImpl implements SellerTermService {

    @Autowired
    private SellerTermMapper sellerTermMapper;

    @Autowired
    private SellerTermManualMapper sellerTermManualMapper;

    @Autowired
    private SysUserService sysUserService;


    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, SellerTermSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = sellerTermManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SellerTerm sellerTerm, String username) {
        //获取登录用户
        SysUser user = sysUserService.getByNickName(username);
        if (user == null) {
            throw new RuntimeException("用户为空");
        }
        sellerTerm.setCreateId(user.getId());
        sellerTerm.setStatus(1);
        sellerTerm.setCreateTime(new Date());
        sellerTerm.setCreateUser(user.getRealName());
        deleteBySellerIdAndTerm(sellerTerm.getSellerId(), sellerTerm.getTerm());
        sellerTermMapper.insert(sellerTerm);
    }

    private void deleteBySellerIdAndTerm(Integer sellerId, Integer term) {
        SellerTermQuery query = new SellerTermQuery();
        SellerTermQuery.Criteria criteria = query.createCriteria();
        criteria.andSellerIdEqualTo(sellerId);
        criteria.andTermEqualTo(term);
        sellerTermMapper.deleteByExample(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        sellerTermMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SellerTerm getSellerTermById(Integer id) {
        SellerTerm sellerTerm1 = sellerTermMapper.selectByPrimaryKey(id);
        if (sellerTerm1 == null) {
            throw new RuntimeException("要查询的商户分期数据不存在。");
        }
        return sellerTermMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SellerTerm> getListBySellerId(Integer sellerId) {
        return sellerTermManualMapper.findListBySellerId(sellerId);
    }

    @Override
    public SellerTerm getSellerTermByParams(Integer sellerId, Integer term) {
        SellerTermQuery sellerTermQuery = new SellerTermQuery();
        sellerTermQuery.createCriteria().andSellerIdEqualTo(sellerId).andTermEqualTo(term);
        List<SellerTerm> sellerTermList = sellerTermMapper.selectByExample(sellerTermQuery);
        if(sellerTermList == null || sellerTermList.size() == 0){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1356);
        }
        SellerTerm sellerTerm = sellerTermList.get(0);
        return sellerTerm;
    }
}
