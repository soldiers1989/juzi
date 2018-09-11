package com.jzfq.retail.common.exception;


import com.jzfq.retail.common.enmu.RetCodeType;

public class CodeTypeException extends RuntimeException {

	private static final long serialVersionUID = 5533178912123528747L;

	private RetCodeType code;

	private String errorMsg;

	public CodeTypeException(RetCodeType code){
		super(code.getRetMsg());
		this.code = code;
		this.errorMsg = code.getRetMsg();
	}

	public CodeTypeException(RetCodeType code, String errorMsg){
		super(errorMsg);
		this.code = code;
		this.errorMsg = errorMsg;
	}

	public CodeTypeException(Exception e){
		super(e.getMessage());
		this.code = RetCodeType.ERROR;
		this.errorMsg = e.getMessage();
	}


	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public RetCodeType getCode() {
		return code;
	}

	public void setCode(RetCodeType code) {
		this.code = code;
	}
}
