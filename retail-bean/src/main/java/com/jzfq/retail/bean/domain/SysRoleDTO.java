package com.jzfq.retail.bean.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleDTO implements Serializable {
    private Integer id;
    private String code;
    private String name;
    private String resourceIds;
    private String resourceNames;
    private String project;
}