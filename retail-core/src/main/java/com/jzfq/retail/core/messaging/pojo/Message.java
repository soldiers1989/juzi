package com.jzfq.retail.core.messaging.pojo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author lagon
 * @time 2016/12/26 14:23
 * @description 公共消息
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message implements Serializable{

    private static final long serialVersionUID = 5326457353797538523L;

    private String code= MessageStatus.SUCCESS.getCode();    //0成功，-1表失败
    @JSONField(name="message")
    @JsonProperty("message")
    @XmlElement(name="message")
    private String remark= MessageStatus.SUCCESS.getMessage();  //消息备注
    @JSONField(name="result")
    @JsonProperty("result")
    @XmlElement(name="result")
    @JsonInclude(Include.NON_EMPTY)
    private Object data;  //消息数据

    public Message(String code, String remark){
        this.code=code;
        this.remark=remark;
    }

    public Message(String code, String remark, Object data){
        this.code=code;
        this.remark=remark;
        this.data=data;
    }

    public Message(MessageStatus messageStatus){
        this.code=messageStatus.getCode();
        this.remark=messageStatus.getMessage();
    }

    public Message(MessageStatus messageStatus, Object data){
        this.code=messageStatus.getCode();
        this.remark=messageStatus.getMessage();
        this.data=data;
    }

    public void setMessageStatus(MessageStatus messageStatus){
        this.code=messageStatus.getCode();
        this.remark=messageStatus.getMessage();
    }

    public String toJSONString(){
        return JSONObject.toJSONString(this);
    }

}
