package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.SellerCateBrandRel;
import com.jzfq.retail.bean.domain.SellerCateBrandRelQuery;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelReq;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerCateBrandRelService;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.dao.SellerCateBrandRelMapper;
import com.jzfq.retail.core.dao.manual.SellerCateBrandRelManualMapper;
import com.jzfq.retail.core.service.SessionManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @title: CorsConfig
 * @description:
 * @company: 北京桔子分期电子商务有限公司
 * @author: zhangjianwei@juzifenqi.com
 * @date: now$
 */
@Service
public class SellerCateBrandRelServiceImpl implements SellerCateBrandRelService {


    @Autowired
    private SellerCateBrandRelMapper sellerCateBrandRelMapper;

    @Autowired
    private SellerCateBrandRelManualMapper sellerCateBrandRelManualMapper;

    @Autowired
    private SessionManage sessionManage;

    @Autowired
    private SysUserService sysUserService;

    private void validCreate(SellerCateBrandRelReq req){
        SellerCateBrandRelQuery query = new SellerCateBrandRelQuery();
        SellerCateBrandRelQuery.Criteria criteria = query.createCriteria();
        criteria.andSellerIdEqualTo(req.getSellerId());
        criteria.andCateIdEqualTo(req.getCateId());
        criteria.andBrandIdEqualTo(req.getBrandId());
        List<SellerCateBrandRel> sellerCateBrandRelList = sellerCateBrandRelMapper.selectByExample(query);
        if(!CollectionUtils.isEmpty(sellerCateBrandRelList)){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1357);
        }
    }

    @Override
    public void create(SellerCateBrandRelReq req, String username) {
        validCreate(req);
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        if(byNickName == null){
            throw new RuntimeException("用户为空");
        }
        SellerCateBrandRel dest = new SellerCateBrandRel();
        TransferUtil.transfer(dest, req);
        dest.setCreateTime(new Date());
        dest.setCreateId(byNickName.getId());
        dest.setState(2);
        sellerCateBrandRelMapper.insert(dest);
    }

    @Override
    public void update(SellerCateBrandRelReq req, String username) {
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        if(byNickName == null){
            throw new RuntimeException("用户为空");
        }
        SellerCateBrandRel target = getEntityById(req.getId());
        TransferUtil.transferIgnoreNull(req, target);
        sellerCateBrandRelMapper.updateByPrimaryKey(target);
    }

    @Override
    public void delete(Integer id) {
        sellerCateBrandRelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SellerCateBrandRel getEntityById(Integer id) {
        return sellerCateBrandRelMapper.selectByPrimaryKey(id);
    }

    private static final int OPT_SUCCESS = 2;
    private static final int OPT_FAIL = 3;
    private static final int OPT_STOP = 4;

    @Override
    public void optRelation(Integer id, Integer state) {
        SellerCateBrandRel sellerCateBrandRel = sellerCateBrandRelMapper.selectByPrimaryKey(id);
        if (id == null) {
            throw new RuntimeException("找不到要更新的对象。");
        }

        //String userName = sessionManage.get
        Date date = new Date();
        sellerCateBrandRel.setState(state);
        if (state == OPT_STOP) {
            //sellerCateBrandRel.setStopId();
            sellerCateBrandRel.setStopTime(date);
        } else {
            //sellerCateBrandRel.setOptId();
            sellerCateBrandRel.setOptTime(date);
        }

    }

    @Override
    public List<Map<String, Object>> findOptList(SellerCateBrandRel search) {
        return sellerCateBrandRelManualMapper.findOptList(search);
    }


    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, SellerCateBrandRelSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> pages = sellerCateBrandRelManualMapper.getSellerCateBrandRelList(search);
        return ListResultRes.newListResult(pages.getResult(), pages.getTotal(), pages.getPageNum(), pages.getPageSize());
    }

}
