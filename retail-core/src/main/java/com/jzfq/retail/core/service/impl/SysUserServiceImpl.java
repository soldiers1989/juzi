package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.domain.SysUserQuery;
import com.jzfq.retail.bean.vo.req.SysUserSearchReq;
import com.jzfq.retail.bean.vo.req.SysUserVo;
import com.jzfq.retail.bean.vo.res.ImportExcelResult;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.exception.BadRequestException;
import com.jzfq.retail.common.util.*;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.dao.SysUserMapper;
import com.jzfq.retail.core.dao.manual.SysUserManualMapper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends CommonMethods implements SysUserService {

    @Autowired
    POIHandler poiHandler;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysUserManualMapper sysUserManualMapper;

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, SysUserSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = sysUserManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    /**
     * 新增
     * @param sysUser
     * @return
     */
    @Override
    public boolean save(SysUserVo sysUser){
        saveValid(sysUser);
        // 对象转化
        SysUser dist = new SysUser();
        TransferUtil.transfer(dist, sysUser);
        // 特殊属性赋值
        dist.setCode("ID-" + UniqueCodeUtil.idCode());
        if(org.apache.commons.lang.StringUtils.isBlank(dist.getNickName())){
            dist.setNickName("N-" + UniqueCodeUtil.idCodeShort());
        }
        dist.setPassword(MD5Util.getMD5String("111"));
        // 持久化
        int i = sysUserMapper.insert(dist);
        // 返回结果
        if(i == 1){
            return true;
        }
        return false;
    }

    /**
     * 修改
     * @param sysUser
     * @return
     */
    @Override
    public boolean update(SysUserVo sysUser){
        updateValid(sysUser);
        // 对象转化
        SysUser target = getByCode(sysUser.getCode());
        TransferUtil.transferIgnoreNull(sysUser, target);
        // 持久化
        int i = sysUserMapper.updateByPrimaryKey(target);
        // 返回结果
        if(i == 1){
            return true;
        }
        return false;
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Override
    public String batchRemove(String ids){
        int count = 0;
        List<String> failList = new ArrayList<>();
        for(String id : ids.split(",")){
            int i = sysUserMapper.deleteByPrimaryKey(Integer.parseInt(id));
            if(i == 1){
                count ++;
            }else{
                SysUser byId = findById(Integer.parseInt(id));
                failList.add(byId.getNickName());
            }
        }
        return failMsg(count, ids, failList);
    }

    /**
     * 编码查询
     * @param code
     * @return
     */
    @Override
    public SysUser getByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        return getSingleByParamsOr(code, null, null, null, null, null, null);
    }

    /**
     * 昵称查询
     * @param nickName
     * @return
     */
    @Override
    public SysUser getByNickName(String nickName){
        if(StringUtils.isBlank(nickName)){
            return null;
        }
        return getSingleByParamsOr(null, nickName, null, null, null, null, null);
    }

    /**
     * 手机号查询
     * @param phone
     * @return
     */
    @Override
    public SysUser getByPhone(String phone){
        if(StringUtils.isBlank(phone)){
            return null;
        }
        return getSingleByParamsOr(null,null, phone, null, null, null, null);
    }

    /**
     * 身份证号查询
     * @param idNumber
     * @return
     */
    @Override
    public SysUser getByIdNumber(String idNumber){
        if(StringUtils.isBlank(idNumber)){
            return null;
        }
        return getSingleByParamsOr(null,null, null, idNumber, null, null, null);
    }

    /**
     * 微信号查询
     * @param wechat
     * @return
     */
    @Override
    public SysUser getByWechat(String wechat){
        if(StringUtils.isBlank(wechat)){
            return null;
        }
        return getSingleByParamsOr(null,null, null, null, wechat, null, null);
    }

    /**
     * qq号查询
     * @param qq
     * @return
     */
    @Override
    public SysUser getByQq(String qq){
        if(StringUtils.isBlank(qq)){
            return null;
        }
        return getSingleByParamsOr(null,null, null, null, null, qq, null);
    }

    /**
     * 邮箱号查询
     * @param email
     * @return
     */
    @Override
    public SysUser getByEmail(String email){
        if(StringUtils.isBlank(email)){
            return null;
        }
        return getSingleByParamsOr(null,null, null, null, null, email, null);
    }

    /**
     * 通过属性查询单个用户，组合查询方式“或”
     * @param nickName
     * @param phone
     * @param idNumber
     * @param wechat
     * @param qq
     * @param email
     * @return
     */
    @Override
    public SysUser getSingleByParamsOr(String code, String nickName, String phone, String idNumber, String wechat, String qq, String email){
        List<SysUser> listByParamsOr = getListByParamsOr(code, nickName, phone, idNumber, wechat, qq, email);
        if(!CollectionUtils.isEmpty(listByParamsOr) && listByParamsOr.size() > 0){
            return listByParamsOr.get(0);
        }
        return null;
    }

    /**
     * 通过属性查询多个用户，组合查询方式“或”
     * @param nickName
     * @param phone
     * @param idNumber
     * @param wechat
     * @param qq
     * @param email
     * @return
     */
    @Override
    public List<SysUser> getListByParamsOr(String code, String nickName, String phone, String idNumber, String wechat, String qq, String email){
        SysUserQuery query = new SysUserQuery();
        SysUserQuery.Criteria criteria = query.or();
        if(StringUtils.isNotBlank(code)){
            criteria.andCodeEqualTo(code);
        }
        if(StringUtils.isNotBlank(nickName)){
            criteria.andNickNameEqualTo(nickName);
        }
        if(StringUtils.isNotBlank(phone)){
            criteria.andPhoneEqualTo(phone);
        }
        if(StringUtils.isNotBlank(idNumber)){
            criteria.andIdNumberEqualTo(idNumber);
        }
        if(StringUtils.isNotBlank(wechat)){
            criteria.andWechatEqualTo(wechat);
        }
        if(StringUtils.isNotBlank(qq)){
            criteria.andQqEqualTo(qq);
        }
        if(StringUtils.isNotBlank(email)){
            criteria.andEmailEqualTo(email);
        }
        return sysUserMapper.selectByExample(query);
    }

    /**
     * 导入Excel格式的用户数据
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, List<ImportExcelResult>> importExcel(MultipartFile file){
        Map<String, List<ImportExcelResult>> result = new HashedMap();
        List<Map<Integer, String>> rows = poiHandler.readExcel(file);
        List<ImportExcelResult> successList = new ArrayList<>();
        List<ImportExcelResult> failList = new ArrayList<>();
        if(rows.size() == 0){
            throw new BadRequestException("操作失败，原因：Excel无数据");
        }
        for(int i=0;i<rows.size();i++){
            ImportExcelResult importExcelResponse = new ImportExcelResult();
            if("操作失败".equals(importExcelResponse.getRes())){
                failList.add(importExcelResponse);
            } else {
                Map<Integer, String> map = rows.get(i);
                SysUserVo sysUserVo = new SysUserVo();
                sysUserVo.setNickName(map.get(0));
                sysUserVo.setRealName(map.get(1));
                sysUserVo.setPhone(map.get(2));
                sysUserVo.setIdNumber(map.get(3));
                sysUserVo.setProvince(map.get(4));
                sysUserVo.setCity(map.get(5));
                sysUserVo.setWechat(map.get(6));
                sysUserVo.setQq(map.get(7));
                sysUserVo.setEmail(map.get(8));
                if(save(sysUserVo)){
                    importExcelResponse.setRes("操作成功");
                    importExcelResponse.setMessage("操作成功");
                    successList.add(importExcelResponse);
                } else {
                    importExcelResponse.setRes("操作失败");
                    importExcelResponse.setMessage("保存失败");
                    failList.add(importExcelResponse);
                }
            }
        }
        result.put("successList", successList);
        result.put("failList", failList);
        return result;
    }

    /**
     * 导出Excel
     * @param sysUserSearch
     * @param response
     * @return
     */
    @Override
    public void exportExcel(SysUserSearchReq sysUserSearch, HttpServletResponse response){
//        List<Map<String, Object>> maps = getByParams(sysUserSearch);
//        List<SysUserExport> list = new ArrayList<>();
//        for(Map<String, Object> map : maps){
//            SysUserExport sysUserExport = new SysUserExport();
//            sysUserExport.setNickName((String) map.get("nick_name"));
//            sysUserExport.setRealName((String) map.get("real_name"));
//            sysUserExport.setPhone((String) map.get("phone"));
//            sysUserExport.setIdNumber((String) map.get("id_number"));
//            sysUserExport.setProvince((String) map.get("province"));
//            sysUserExport.setCity((String) map.get("city"));
//            sysUserExport.setWechat((String) map.get("wechat"));
//            sysUserExport.setQq((String) map.get("qq"));
//            sysUserExport.setEmail((String) map.get("email"));
//            sysUserExport.setRoles((String) map.get("roles"));
//            list.add(sysUserExport);
//        }
//        String fileName = "用户信息";
//        String[] columnNames = {"用户名", "姓名（必填）", "手机号（必填）", "身份证号", "省份", "城市", "微信号", "QQ号", "邮箱", "角色"};
//        poiHandler.export(fileName, columnNames, list, response);
    }

    @Override
    public List<Map<String, Object>> getByParamsSearch(SysUserSearchReq sysUserSearch) {
        return null;
    }

//    @Override
//    public List<Map<String, Object>> getByParamsSearch(SysUserSearchReq sysUserSearch){
//        if(sysUserSearch.getStart() == null){
//            sysUserSearch.setStart(0);
//        }
//        if(sysUserSearch.getLength() == null){
//            sysUserSearch.setLength(5);
//        }
//        String sql = buildSqlSearch(sysUserSearch) + " order by id desc limit " + sysUserSearch.getStart() + "," + sysUserSearch.getLength();
//        return getService().findBySQL(sql);
//    }

    @Override
    public SysUser findById(Integer id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public void removeById(Integer id) {
        sysUserMapper.deleteByPrimaryKey(id);
    }

//    private List<Map<String, Object>> getByParams(SysUserSearchReq sysUserSearch){
//        if(sysUserSearch.getStart() == null){
//            sysUserSearch.setStart(0);
//        }
//        String sql = buildSql(sysUserSearch) + " order by id desc limit " + sysUserSearch.getStart() + ",10000";
////        return getService().findBySQL(sql);
//        return sysUserMapper.se
//    }

    private String buildSql(SysUserSearchReq sysUserSearch){
        StringBuilder sb = new StringBuilder();
        sb.append("select nick_name,real_name,phone,id_number,province,city,wechat,qq,email,roles from sys_user where 1=1 ");
        if(sysUserSearch != null){
            if(StringUtils.isNotBlank(sysUserSearch.getCode())){
                sb.append(" and code = '" + EsapiUtil.sql(sysUserSearch.getCode()) + "'");
            }
            if(StringUtils.isNotBlank(sysUserSearch.getNickName())){
                sb.append(" and nick_name = '" + EsapiUtil.sql(sysUserSearch.getNickName()) + "'");
            }
            if(StringUtils.isNotBlank(sysUserSearch.getRealName())){
                sb.append(" and real_name = '" + EsapiUtil.sql(sysUserSearch.getRealName()) + "'");
            }
            if(StringUtils.isNotBlank(sysUserSearch.getPhone())){
                sb.append(" and phone = '" + EsapiUtil.sql(sysUserSearch.getPhone()) + "'");
            }
        }
        return sb.toString();
    }


    /**
     * 验证：新增接口
     * @param sysUser
     */
    private void saveValid(SysUserVo sysUser){
        // 参数对象
        if(sysUser == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1100);
        }
        // 非空验证：姓名
        if(StringUtils.isBlank(sysUser.getRealName())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1101);
        }
        // 非空验证：手机号
        if(StringUtils.isBlank(sysUser.getPhone())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1102);
        }
        // 昵称
        if(StringUtils.isNotBlank(sysUser.getNickName())){
            SysUser byNickName = getByNickName(sysUser.getNickName());
            if(byNickName != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1103);
            }
        }
        // 手机号
        if(!PhoneValidUtil.isPhone(sysUser.getPhone())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1104);
        }
        SysUser byPhone = getByPhone(sysUser.getPhone());
        if(byPhone != null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1105);
        }
        // 身份证
        if(StringUtils.isNotBlank(sysUser.getIdNumber())){
            if(!IdNumValidUtil.validID(sysUser.getIdNumber(), false, false)){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1106);
            }
            SysUser byIdNumber = getByIdNumber(sysUser.getIdNumber());
            if(byIdNumber != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1107);
            }
        }
        // 微信
        if(StringUtils.isNotBlank(sysUser.getWechat())){
            SysUser byWechat = getByWechat(sysUser.getWechat());
            if(byWechat != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1108);
            }
        }
        // QQ
        if(StringUtils.isNotBlank(sysUser.getQq())){
            SysUser byQq = getByQq(sysUser.getQq());
            if(byQq != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1109);
            }
        }
        // email
        if(StringUtils.isNotBlank(sysUser.getEmail())){
            if(sysUser.getEmail().indexOf('@')<1 || sysUser.getEmail().split("@").length>2){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1110);
            }
            SysUser byEmail = getByEmail(sysUser.getEmail());
            if(byEmail != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1111);
            }
        }
    }

    /**
     * 验证：编辑接口
     * @param sysUser
     */
    private void updateValid(SysUserVo sysUser){
        // 参数对象
        if(sysUser == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1150);
        }
        // ID(code编码)
        if(StringUtils.isBlank(sysUser.getCode())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1151);
        }
        SysUser byCode = getByCode(sysUser.getCode());    // 原对象
        if(byCode == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1152);
        }
        // 昵称
        if(StringUtils.isNotBlank(sysUser.getNickName()) && !sysUser.getNickName().trim().equals(byCode.getNickName())){    // 不是空，不是原值
            SysUser byNickName = getByNickName(sysUser.getNickName());
            if(byNickName != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1153);
            }
        }
        // 手机号
        if(StringUtils.isNotBlank(sysUser.getPhone()) && !sysUser.getPhone().trim().equals(byCode.getPhone())){ // 不是空，不是原值
            if(!PhoneValidUtil.isPhone(sysUser.getPhone())){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1154);
            }
            SysUser byPhone = getByPhone(sysUser.getPhone());
            if(byPhone != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1155);
            }
        }
        // 身份证号
        if(StringUtils.isNotBlank(sysUser.getIdNumber()) && !sysUser.getIdNumber().trim().equals(byCode.getIdNumber())){ // 不是空，不是原值
            if(!IdNumValidUtil.validID(sysUser.getIdNumber(), false, false)){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1156);
            }
            SysUser byIdNumber = getByIdNumber(sysUser.getIdNumber());
            if(byIdNumber != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1157);
            }
        }
        // 微信
        if(StringUtils.isNotBlank(sysUser.getWechat()) && !sysUser.getWechat().trim().equals(byCode.getWechat())){  // 不是空，不是原值
            SysUser byWechat = getByWechat(sysUser.getWechat());
            if(byWechat != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1158);
            }
        }
        // QQ
        if(StringUtils.isNotBlank(sysUser.getQq()) && !sysUser.getQq().trim().equals(byCode.getQq())){  // 不是空，不是原值
            SysUser byQq = getByQq(sysUser.getQq());
            if(byQq != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1159);
            }
        }
        // email
        if(StringUtils.isNotBlank(sysUser.getEmail()) && !sysUser.getEmail().trim().equals(byCode.getEmail())){ // 不是空，不是原值
            if(sysUser.getEmail().indexOf('@')<1 || sysUser.getEmail().split("@").length>2){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1160);
            }
            SysUser byEmail = getByEmail(sysUser.getEmail());
            if(byEmail != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1161);
            }
        }
    }
}
