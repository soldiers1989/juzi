package com.jzfq.retail.core.service.impl;

import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.service.member.IMemberShopService;
import com.jzfq.retail.core.api.IdemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: liuwei
 * @date: 2018/7/15 13:14
 * @version: 1.0
 */
@Service
public class DemoServiceImpl implements IdemoService {

    @Autowired
    private IMemberShopService iMemberShopService;


    @Override
    public void adminDemo() {
        System.out.println("admin out ...");

        //2、通过member_id 到用户中心获取身份证号码等其他信息
        Integer memberId = 210001690;
        ServiceResult<MemberInfo> result = iMemberShopService.getMemberById(memberId);
        MemberInfo member = result.getResult();
        System.out.println("---------------------------------------------------"+result.getMessage());

        String member_name = member.getRealName();
        String id_card = member.getIdcard();
        System.out.println(id_card);


    }

    @Override
    public void appDemo() {
        System.out.println("app out ...");
    }

    @Override
    public void touchDemo() {
        System.out.println("touch out ...");
    }
}
