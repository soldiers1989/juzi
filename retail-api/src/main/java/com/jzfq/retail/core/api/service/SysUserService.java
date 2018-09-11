package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.req.DictionarySearchReq;
import com.jzfq.retail.bean.vo.req.SysUserSearchReq;
import com.jzfq.retail.bean.vo.req.SysUserVo;
import com.jzfq.retail.bean.vo.res.ImportExcelResult;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface SysUserService {

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, SysUserSearchReq search);

    boolean save(SysUserVo sysUser);
    boolean update(SysUserVo sysUser);
    String batchRemove(String ids);
    SysUser getByCode(String code);
    SysUser getByNickName(String nickName);
    SysUser getByPhone(String phone);
    SysUser getByIdNumber(String idNumber);
    SysUser getByWechat(String wechat);
    SysUser getByQq(String qq);
    SysUser getByEmail(String email);
    SysUser getSingleByParamsOr(String code, String nickName, String phone, String idNumber, String wechat, String qq, String email);
    List<SysUser> getListByParamsOr(String code, String nickName, String phone, String idNumber, String wechat, String qq, String email);
    Map<String, List<ImportExcelResult>> importExcel(MultipartFile file);
    void exportExcel(SysUserSearchReq sysUserSearch, HttpServletResponse response);
    List<Map<String, Object>> getByParamsSearch(SysUserSearchReq sysUserSearch);
    SysUser findById(Integer id);
    void removeById(Integer id);

}
