package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.core.api.service.RedisService;
import com.jzfq.retail.core.api.service.SysUserService;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.SellerStatus;
import com.jzfq.retail.common.util.MD5Util;
import com.jzfq.retail.core.api.service.SellerLoginPermissionService;
import com.jzfq.retail.core.dao.SellerLoginOpenIDRelMapper;
import com.jzfq.retail.core.dao.SellerLoginPermissionMapper;
import com.jzfq.retail.core.dao.SellerMapper;
import com.jzfq.retail.core.dao.manual.SellerLoginPermissionManualMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: SellerLoginPermissionServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 11:18
 * @Description: 户登录权限操作接口实现
 */
@Service
public class SellerLoginPermissionServiceImpl implements SellerLoginPermissionService {

    @Autowired
    private SellerLoginPermissionMapper sellerLoginPermissionMapper;

    @Autowired
    private SellerLoginPermissionManualMapper sellerLoginPermissionManualMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private SellerLoginOpenIDRelMapper sellerLoginOpenIDRelMapper;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisService redisService;

    @Override
    public List<SellerLoginPermission> getAllList(SellerLoginPermission search) {
        List<SellerLoginPermission> result = sellerLoginPermissionManualMapper.findList(search).getResult();
        return result;
    }

    @Override
    public ListResultRes<SellerLoginPermission> getList(Integer page, Integer pageSize, SellerLoginPermission search) {
        PageHelper.startPage(page, pageSize);
        Page<SellerLoginPermission> list = sellerLoginPermissionManualMapper.findList(search);
        return ListResultRes.newListResult(list.getResult(), list.getTotal(), list.getPageNum(), list.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SellerLoginPermission entity, String createUserName) {
        if (entity == null || entity.getSellerId() == null) {
            throw new RuntimeException("缺少参数");
        }
        Seller seller = sellerMapper.selectByPrimaryKey(entity.getSellerId());
        if (seller == null) {
            throw new RuntimeException("添加账号的商户存在");
        }
        //判断是否已经存在该账号 店铺编号+店铺预留手机号
        SellerLoginPermissionQuery example = new SellerLoginPermissionQuery();
        example.createCriteria().andSellerLoginEqualTo(entity.getSellerLogin()).andSellerMobileEqualTo(entity.getSellerMobile());
        List<SellerLoginPermission> checkList = sellerLoginPermissionMapper.selectByExample(example);
        if (checkList != null && checkList.size()>0) {
            throw new RuntimeException("该账号和手机号已存在");
        }
        SysUser byNickName = sysUserService.getByNickName(createUserName);
        if (byNickName == null) {
            throw new RuntimeException("操作管理员用户不存在");
        }
        if (seller.getAuditStatus() == SellerStatus.OPEN_ACCOUNT_SUCCESS.getCode() || seller.getAuditStatus() == SellerStatus.ACCOUNT_FROZEN.getCode()) {
            entity.setSellerPassword(MD5Util.getMD5String(entity.getSellerPassword()));
            entity.setCreateTime(new Date());
            entity.setCreateId(byNickName.getId());
            entity.setCreateUser(byNickName.getRealName());
            sellerLoginPermissionMapper.insert(entity);
            //添加日志
            systemLogSupport.sellerPasswordChangeRecord(entity.getSellerId(), entity.getSellerName(), null, null, entity.getSellerMobile(), entity.getSellerPassword(), byNickName.getId(), byNickName.getRealName());
        } else {
            throw new RuntimeException("添加账号失败，商户未审核或审核失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SellerLoginPermission entity, String updateUserName) {
        throw new RuntimeException("需要实现该接口");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Integer id, String oldPassword, String newPassword, String changeUserName) {
        if (id == null || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            throw new RuntimeException("缺少参数");
        }
        SellerLoginPermission entity = sellerLoginPermissionMapper.selectByPrimaryKey(id);
        if (entity == null) {
            throw new RuntimeException("密码修改错误，修改账号不存在");
        }
        String oldPasswordMd5 = entity.getSellerPassword();
        if (!MD5Util.checkPassword(oldPassword, oldPasswordMd5)) {
            throw new RuntimeException("原密码不正确");
        }
        SysUser byNickName = sysUserService.getByNickName(changeUserName);
        if (byNickName == null) {
            throw new RuntimeException("操作管理员用户不存在");
        }
        entity.setSellerPassword(MD5Util.getMD5String(newPassword));
        sellerLoginPermissionMapper.updateByPrimaryKey(entity);
        //获取所有的key值
        SellerLoginOpenIDRelQuery openIDRelQuery = new SellerLoginOpenIDRelQuery();
        openIDRelQuery.createCriteria().andLoginPermissionIdEqualTo(id);
        List<SellerLoginOpenIDRel> delRedisTokenList = sellerLoginOpenIDRelMapper.selectByExample(openIDRelQuery);
        delRedisTokenList.stream().forEach(x -> {
            redisService.deleteByKey(x.getOpenid());
        });
        //添加日志
        systemLogSupport.sellerPasswordChangeRecord(entity.getSellerId(), entity.getSellerName(), entity.getSellerMobile(), oldPasswordMd5, entity.getSellerMobile(), entity.getSellerPassword(), byNickName.getId(), byNickName.getRealName());
    }

    /**
     * 根据token校验用户openID是否存在
     *
     * @param tokenStr
     * @return Boolean
     */
    @Override
    public Boolean checkSellerLogin(String tokenStr) {
        //tokenStr = (sellerMobile).append("-").append(sellerCode).append("-").append(openID);
        if (StringUtils.isNotBlank(tokenStr)) {
            String[] str = tokenStr.split("-");
            if (str.length != 3) {
                return false;
            }
            Map<String, String> map = new HashMap<>();
            map.put("sellerMobile", str[0]);
            map.put("sellerCode", str[1]);
            map.put("openID", str[2]);
            Long num = sellerLoginPermissionManualMapper.checkSellerLogin(map);
            if (num == 1) {
                return true;
            }
        }
        return false;
    }
}
