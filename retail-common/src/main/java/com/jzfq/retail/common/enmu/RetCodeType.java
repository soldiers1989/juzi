package com.jzfq.retail.common.enmu;

/**
 * response 返回状态码
 * @author luyankun
 * @version V1.0
 * @date 2016年12月06日 9:17
 */
public enum RetCodeType {

    SUCCESS("0000","处理成功"),
    ERROR("0001","处理失败"),
    JSON_PARS_EERROR("0002", "JSON解析错误"),
    PARAM_ERROR("0003", "参数错误"),
    PARAM_PARSE_ERROR("0004", "参数解析错误"),
    VALUE_ISNULL_ERROR("0005", "[%s]参数为空!"),
    VALUE_ERROR("0006", "[%s]没有获取到正确数据"),
    UPDATE_SELECT_DATA_NOT_FOUND("0007", "没有获取到数据，修改失败"),
    ;


    private String retCode;
    private String retMsg;

    RetCodeType(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public String getRetMsg(Object... params){
        return String.format(retMsg, params);
    }

}
