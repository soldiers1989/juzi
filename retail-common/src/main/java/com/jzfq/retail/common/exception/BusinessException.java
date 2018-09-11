package com.jzfq.retail.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author liuwei
 * @version 1.0
 **/


public class BusinessException extends RuntimeException{

    private int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private String source;  // default UserManagement、crm

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(final int code, String message) {
        super(message);
        this.setCode(code);
    }
}
