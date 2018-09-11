package com.jzfq.retail.common.exception.base;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lagon
 * @time 2016-6-8 下午11:40:39
 * @description 基础异常
 */
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = 6971026221011287689L;
	//所属域,默认为基础域
    private String realm=ExceptionRealm.BASIC.getDesc();
    //错误码
    private String code;
    //错误消息
    private String message="未知异常";
    //异常对象
    private Throwable cause;

    public BaseException(){

    }

    public BaseException(String realm, String code, String message, Throwable cause) {
        this.realm = realm;
        this.code = code;
        this.message = message;
        this.cause=cause;
    }

    public BaseException(String code, String message, Throwable cause) {
    	this(null, code, message, cause);
    }

    public BaseException(String message, Throwable cause) {
    	this(null, null, message, cause);
    }

    public BaseException(String code, String message) {
    	this(null, code, message, null);
    }

    public BaseException(String message) {
    	this(null, null, message, null);
    }

    @Override
    public String getMessage() {
    	StringBuilder bud=new StringBuilder();
    	bud.append("【");
    	bud.append("realm").append("=").append(realm);
    	if(!StringUtils.isEmpty(code)){
    		bud.append(",").append("code").append("=").append(code);
    		if(!StringUtils.isEmpty(message)){
    			bud.append(",").append("message").append("=").append(message);
    		}
    	}else{
    		if(!StringUtils.isEmpty(message)){
    			bud.append(",").append("message").append("=").append(message);
    		}
    	}
    	bud.append("】");
    	return bud.toString();
    }

	@Override
	public synchronized Throwable getCause() {
		return this.cause;
	}

    public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
    public String toString() {
        return this.getMessage();
    }

}
