package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.Dictionary;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.req.DictionaryReq;
import com.jzfq.retail.bean.vo.req.DictionarySearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.service.DictionaryService;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.dao.DictionaryMapper;
import com.jzfq.retail.core.dao.manual.DictionaryManualMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月29日 15:56
 * @Description: 字典表接口实现
 */
@SuppressWarnings("ALL")
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private DictionaryManualMapper dictionaryManualMapper;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, DictionarySearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = dictionaryManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DictionaryReq req, String username) {
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        if(byNickName == null){
            throw new RuntimeException("用户为空");
        }
        Dictionary dictionary = new Dictionary();
        TransferUtil.transfer(dictionary, req);
        dictionary.setCreateId(byNickName.getId());
        dictionary.setCreateUser(byNickName.getRealName());
        dictionary.setCreateTime(new Date());
        dictionaryMapper.insert(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DictionaryReq req, String username) {
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        if(byNickName == null){
            throw new RuntimeException("用户为空");
        }
        Dictionary target = dictionaryMapper.selectByPrimaryKey(req.getId());
        if (target == null) {
            throw new RuntimeException("要更新的字典数据不存在。");
        }
        TransferUtil.transferIgnoreNull(req, target);
        target.setUpdateId(byNickName.getId());
        target.setUpdateUser(byNickName.getRealName());
        target.setUpdateTime(new Date());
        dictionaryMapper.updateByPrimaryKey(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        dictionaryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Dictionary getEntityById(Integer id) {
        return dictionaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Dictionary getEntityByName(String name) {
        return dictionaryManualMapper.findByName(name);
    }

    @Override
    public Dictionary getEntityByCode(String code) {
        return dictionaryManualMapper.findByCode(code);
    }
}
