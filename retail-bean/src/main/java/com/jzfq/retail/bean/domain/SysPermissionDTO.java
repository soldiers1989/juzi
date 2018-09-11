package com.jzfq.retail.bean.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysPermissionDTO implements Serializable {
    private Integer id;
    private Integer parentId;
    private String label;
    private String type;
    private String url;
    private Boolean available;
    private String icon;
    private List<SysPermissionDTO> children;
}