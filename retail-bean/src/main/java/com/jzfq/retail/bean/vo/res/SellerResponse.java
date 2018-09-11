package com.jzfq.retail.bean.vo.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @Title: SellerResponse
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月06日 15:00
 * @Description: 小程序商户端端返回
 */
@Getter
@Setter
@ToString
public class SellerResponse {
    /**
     * 返回成功或失败 1：成功，0：失败
     */
    private Integer result;
    /**
     * 描述信息
     */
    private String message;
    /**
     * 错误code
     */
    private String errorCode;
    /**
     * 返回数据
     */
    private Object data;

    public SellerResponse() {
    }

    public SellerResponse(Integer result, String message, String errorCode, Object data) {
        this.result = result;
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public static SellerResponse success(String message, Object data) {
        return new SellerResponse(1, message, HttpStatus.OK.toString(), data);
    }

    public static SellerResponse success(String message) {
        return new SellerResponse(1, message, HttpStatus.OK.toString(), null);
    }

    public static SellerResponse success(Object data) {
        return new SellerResponse(1, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.toString(), data);
    }


    public static SellerResponse error(String message, Object data) {
        return new SellerResponse(0, message, HttpStatus.INTERNAL_SERVER_ERROR.toString(), data);
    }

    public static SellerResponse error(String message, Object data, HttpStatus status) {
        return new SellerResponse(0, message, status.toString(), data);
    }

    public static SellerResponse error(String message) {
        return new SellerResponse(0, message, HttpStatus.INTERNAL_SERVER_ERROR.toString(), null);
    }

    public static SellerResponse error(String message, HttpStatus status) {
        return new SellerResponse(0, message, status.toString(), null);
    }

    public static SellerResponse error(Object data) {
        return new SellerResponse(0, HttpStatus.OK.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), data);
    }

    public static SellerResponse error(Object data, HttpStatus status) {
        return new SellerResponse(0, status.getReasonPhrase(), status.toString(), data);
    }
}
