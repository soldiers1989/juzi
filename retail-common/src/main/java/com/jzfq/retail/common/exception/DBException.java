package com.jzfq.retail.common.exception;

/**
 * @author liuwei
 * @version 1.0
 **/
public class DBException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DBException(String msg)
    {
        super(msg);
    }
}

