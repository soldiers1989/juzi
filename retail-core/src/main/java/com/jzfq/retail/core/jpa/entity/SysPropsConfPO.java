package com.jzfq.retail.core.jpa.entity;

/**
 * Created by 刘巍 on 2017/12/29.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author lagon
 * @time 2017/6/27 16:44
 * @description 系统属性配置实体类
 */
//@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_props_conf")
public class SysPropsConfPO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "env_key", columnDefinition="VARCHAR(128) COMMENT '环境key'")
    private String envKey;

    @Column(name = "dev_env_value", columnDefinition="VARCHAR(512) COMMENT '开发环境value'")
    private String devEnvValue;

    @Column(name = "test_env_value", columnDefinition="VARCHAR(512) COMMENT '测试环境value'")
    private String testEnvValue;

    @Column(name = "uat_env_value", columnDefinition="VARCHAR(512) COMMENT 'UAT环境value'")
    private String uatEnvValue;

    @Column(name = "prod_env_value", columnDefinition="VARCHAR(512) COMMENT '生产环境value'")
    private String prodEnvValue;

}
