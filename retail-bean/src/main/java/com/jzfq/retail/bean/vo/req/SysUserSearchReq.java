package com.jzfq.retail.bean.vo.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询参数对象
 *
 * @author Garen Gosling
 * @create 2018-04-21 14:11
 * @since v1.0
 */
@Data
public class SysUserSearchReq implements Serializable {
    private String code;
    private String nickName;
    private String realName;
    private String phone;
}
