package com.jzfq.retail.bean.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserVo implements Serializable {
    private Long id;
    private String code;
    private String nickName;
    private String realName;
    private String password;
    private String phone;
    private String idNumber;
    private String province;
    private String city;
    private String wechat;
    private String qq;
    private String email;
    private String roles;
}
