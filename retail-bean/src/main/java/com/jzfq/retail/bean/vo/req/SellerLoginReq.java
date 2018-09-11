package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Setter
@Getter
@ToString
public class SellerLoginReq implements Serializable {
    @NotBlank(message = "商户登录账号不能为空")
    @Length(max = 20, message = "商户登录账号长度不可以超过20")
    private String sellerLogin;
    @NotBlank(message = "商户手机号不能为空")
    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "请输入正确手机号")
    private String sellerMobile;
    @NotBlank(message = "商户密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6-16位")
    private String password;
    @NotBlank(message = "微信CODE不能为空")
    private String wxCode;
    /**
     * 经度 lng
     */
    @NotNull(message = "登录经度不可以为空")
    private Double lng;

    /**
     * 纬度 lat
     */
    @NotNull(message = "登录纬度不可以为空")
    private Double lat;
}