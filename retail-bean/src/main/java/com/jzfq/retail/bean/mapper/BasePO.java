package com.jzfq.retail.bean.mapper;

/**
 * @author lagon
 * @time 2017/6/15 6:53
 * @description 基础PO
 */

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BasePO implements Serializable {

    protected Long id;//主键
    protected Timestamp dateCreated;//创建时间
    protected Timestamp lastModified;//最近更新时间
    protected Integer disabled;//是否禁用，0表正常，1表禁用
    protected Integer deleted;//是否删除，0表正常，1表删除
    protected String remark;//备注
    
}
