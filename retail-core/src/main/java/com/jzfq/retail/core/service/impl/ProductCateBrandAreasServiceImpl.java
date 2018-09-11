package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SysUserService;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.ProductCateBrandAreas;
import com.jzfq.retail.bean.vo.req.ProductCateBrandAreasReq;
import com.jzfq.retail.bean.vo.req.ProductCateBrandAreasSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.ProductCateBrandAreasStatus;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.service.ProductCateBrandAreasService;
import com.jzfq.retail.core.dao.ProductCateBrandAreasMapper;
import com.jzfq.retail.core.dao.manual.ProductCateBrandAreasManualMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @Company: 北京桔子分期电子商务有限公司
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月28日 15:21
 * @Description: 类-品牌-城市-区间价：操作接口实现
 */
@Service
public class ProductCateBrandAreasServiceImpl implements ProductCateBrandAreasService {

    @Autowired
    private ProductCateBrandAreasMapper productCateBrandAreasMapper;

    @Autowired
    private ProductCateBrandAreasManualMapper productCateBrandAreasManualMapper;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public ProductCateBrandAreas getEntityById(Integer id) {
        if (id == null) {
            throw new RuntimeException("缺少参数");
        }
        ProductCateBrandAreas entity = productCateBrandAreasMapper.selectByPrimaryKey(id);
        return entity;
    }

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, ProductCateBrandAreasSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = productCateBrandAreasManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductCateBrandAreasReq entity, String username) {
        ProductCateBrandAreas target = new ProductCateBrandAreas();
        TransferUtil.transferIgnoreNull(entity, target);
        target.setStatus(ProductCateBrandAreasStatus.CREATE.getCode());
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        if(byNickName == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_401);
        }
        target.setCreateId(byNickName.getId());
        target.setCreater(byNickName.getRealName());
        target.setCreateTime(new Date());
        productCateBrandAreasMapper.insert(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProductCateBrandAreasReq entity) {
        ProductCateBrandAreas oldEntity = productCateBrandAreasMapper.selectByPrimaryKey(entity.getId());
        if (oldEntity == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0049);
        }
        if (oldEntity.getStatus() != ProductCateBrandAreasStatus.CREATE.getCode()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0050);
        }
        TransferUtil.transferIgnoreNull(entity, oldEntity);
        productCateBrandAreasMapper.updateByPrimaryKey(oldEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
       productCateBrandAreasMapper.deleteByPrimaryKey(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationAuditing(Integer id, String opt, String userName) {
        ProductCateBrandAreas entity = getEntityById(id);
        if (entity == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0049);
        }
        if (entity.getStatus() == ProductCateBrandAreasStatus.AUDITING_SUCCESS.getCode()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0051);
        }
        //获取当前登录人员
        SysUser byNickName = sysUserService.getByNickName(userName);
        if (opt.equalsIgnoreCase("success")) {
            //审核成功操作
            entity.setStatus(ProductCateBrandAreasStatus.AUDITING_SUCCESS.getCode());
        } else if (opt.equalsIgnoreCase("fail")) {
            //审核失败操作
            entity.setStatus(ProductCateBrandAreasStatus.AUDITING_FAIL.getCode());
        } else {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0052);
        }
        entity.setApprovalUser(byNickName.getRealName());
        entity.setApprovalUserId(byNickName.getId());
        entity.setApprovalTime(new Date());
        productCateBrandAreasMapper.updateByPrimaryKey(entity);
        //添加日志
        systemLogSupport.riskApprovalLog(entity.getId(), opt.equalsIgnoreCase("success") ? ProductCateBrandAreasStatus.AUDITING_SUCCESS : ProductCateBrandAreasStatus.AUDITING_FAIL, "风控城市区间价审核操作", byNickName.getRealName(), byNickName.getId());
    }
}
