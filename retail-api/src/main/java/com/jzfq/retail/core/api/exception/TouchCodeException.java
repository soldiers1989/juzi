package com.jzfq.retail.core.api.exception;

import com.jzfq.retail.common.enmu.TouchApiCode;

import java.io.Serializable;

/**
 * @Title: TouchCodeException
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月20日 15:09
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class TouchCodeException extends RuntimeException implements Serializable {

    private TouchApiCode touchApiCode;

    private String extendMsg = "";

    public TouchCodeException() {
        super();
    }

    public TouchCodeException(String message) {
        super(message);
    }

    public TouchCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TouchCodeException(Throwable cause) {
        super(cause);
    }

    protected TouchCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TouchCodeException(TouchApiCode touchApiCode) {
        super(touchApiCode.getMsg());
        this.touchApiCode = touchApiCode;
    }

    public TouchCodeException(TouchApiCode touchApiCode, String extendMsg) {
        super(touchApiCode.getMsg()+extendMsg);
        this.touchApiCode = touchApiCode;
        this.extendMsg = extendMsg;
    }

    public TouchCodeException(TouchApiCode touchApiCode, String extendMsg, boolean isExtend) {
        super(isExtend ? touchApiCode.getMsg() + extendMsg : extendMsg);
        this.touchApiCode = touchApiCode;
        this.extendMsg = extendMsg;
    }

    public TouchApiCode getTouchApiCode() {
        return touchApiCode;
    }

    public String getExtendMsg() {
        return extendMsg;
    }


}
