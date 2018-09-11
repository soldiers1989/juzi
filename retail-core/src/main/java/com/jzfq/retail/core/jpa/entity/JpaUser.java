package com.jzfq.retail.core.jpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 刘巍 on 2017/7/2.
 */
@Entity
@Getter
@Setter
@Table(name = "jpa_user")
public class JpaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column
    private String name;
    @Column
    private Integer age;
    @Column
    private BigDecimal balance;

}
