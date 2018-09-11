package com.jzfq.retail.core.swagger.api.manage;

import com.jzfq.retail.bean.vo.req.AdminRegisterReq;
import com.jzfq.retail.bean.vo.res.LoginInfo;
import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.AdminLoginService;
import com.jzfq.retail.core.api.service.RedisService;
import com.jzfq.retail.core.config.SessionManage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;
@Slf4j
@RestController
public class AdminLoginController{

    private static final String REGISTER = "/register"; // 后台用户注册

    @Autowired
    RedisService redisService;

    @Autowired
    AdminLoginService adminLoginService;

    @GetMapping("/login")
    String toLogin() {
        return "login";
    }

    @GetMapping("/register")
    String toRegister() {
        return "register";
    }

    /**
     * 1、校验图形验证码是否为空
     * 2、校验图形验证码是否正确
     * 3、查询用户是否存在
     * 4、生成token，保存到Redis
     * 5、查询菜单权限
     * 6、查询角色
     * 7、封装token、用户信息、菜单权限、角色，传到前端
     * @param userName
     * @param password
     * @param checkCode

     * @return
     */
    @ApiOperation(value = "登录", notes = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TouchResponseModel> login(@ApiParam(value = "用户名") @RequestParam(value = "userName") String userName,
                                             @ApiParam(value = "密码") @RequestParam(value = "password") String password,
                                             @ApiParam(value = "图形验证码") @RequestParam(value = "checkCode") String checkCode,
                                             @ApiParam(value = "UUID") @RequestParam(value = "uuid") String uuid) {

        String randCheckCode = redisService.queryStringByKey(uuid);
        if(checkCode == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0053);
        }
        log.info("实际图形验证码：{},用户输入图形验证码：{}",randCheckCode, checkCode);
        if(!checkCode.equalsIgnoreCase(randCheckCode)){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0054);
        }
        try {
            LoginInfo login = adminLoginService.login(userName, password);
            return TouchApiResponse.success(login,"操作成功");
        } catch (TouchCodeException te) {
            log.error("请求返回异常:{}", te.getMessage());
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("运营中心后台系统登录:{}", e.getMessage());
            return TouchApiResponse.failed(e.getMessage());
        }
    }


}
