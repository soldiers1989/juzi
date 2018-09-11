package com.jzfq.retail.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author liuwei
 * @version 1.0
 **/
public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

}
