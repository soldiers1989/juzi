package com.jzfq.retail.common.exception;

/**
 * @author liuwei
 * @version 1.0
 **/


public class BadLoanException extends Exception{

    public BadLoanException(String message) {
        super(message);
    }

    public BadLoanException(final int code, String message) {
        super(message);
    }
}
