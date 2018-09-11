package com.jzfq.retail.bean.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleVo implements Serializable {
    private Integer id;
    private String code;
    private String name;
    private String resourceIds;
    private String project;
}
