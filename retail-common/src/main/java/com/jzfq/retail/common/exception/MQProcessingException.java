package com.jzfq.retail.common.exception;


import com.jzfq.retail.common.exception.base.BaseException;
import com.jzfq.retail.common.exception.base.ExceptionRealm;

/**
 * @author lagon
 * @time 2016-6-9 下午10:49:21
 * @description MQ处理异常
 */
public class MQProcessingException extends BaseException {

	public MQProcessingException(String code, String message , Throwable e) {
        super(ExceptionRealm.MQ_PROCESSING.getDesc(),code,message,e);
    }

	public MQProcessingException(String code, String message) {
        super(ExceptionRealm.MQ_PROCESSING.getDesc(),code,message,null);
    }

    public MQProcessingException(String message, Throwable e) {
    	super(ExceptionRealm.MQ_PROCESSING.getDesc(),null,message,e);
    }

    public MQProcessingException(String message) {
    	super(ExceptionRealm.MQ_PROCESSING.getDesc(),null,message,null);
    }

    public MQProcessingException(Throwable e) {
    	super(ExceptionRealm.MQ_PROCESSING.getDesc(),null,null,e);
    }

}
