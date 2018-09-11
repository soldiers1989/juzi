package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Description: 后台用户注册参数
 * @author liuwei
 * @version V1.0
 * 2018年7月5日上午
 */
@ToString
@Getter
@Setter
public class AdminRegisterReq implements Serializable {

	/**
	 * 登录名
	 */

	private String userName;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 联系电话
	 */
	private String tel;

}
