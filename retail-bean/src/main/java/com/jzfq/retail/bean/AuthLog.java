package com.jzfq.retail.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liuwei
 * @Description: 操作日志实体
 * @Version: 1.0
 * @Date: 2017/8/18 10:14
 */
public class AuthLog implements Serializable{
    private Integer id;

    //用户ID
    private Integer userId;

    //客户端IP
    private String clientIp;

    private String serverIp;

    //创建时间
    private Date createTime;

    //操作内容
    private String optContent;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent == null ? null : optContent.trim();
    }
}