package com.jzfq.retail.bean.vo.res;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 18:25
 * @Description: 返回接口格式
 */
public class ApiResponse {

    private static final String EMPTY = "";

    public static ResponseEntity<ResponseModel> successPage(Object result, int count) {
        return successPage(result, count, EMPTY);
    }

    public static ResponseEntity<ResponseModel> successPage(Object result, int count, String msg) {
        Map map = new HashMap();
        map.put("recordsTotal", count);
        map.put("recordsFiltered", count);
        map.put("data", result);
        map.put("code", HttpStatus.OK.value());
        return success(map, msg);
    }

    public static ResponseEntity<ResponseModel> success(Object data) {
        return success(data, EMPTY);
    }

    public static ResponseEntity<ResponseModel> success(String msg) {
        return success(EMPTY, msg);
    }

    public static ResponseEntity<ResponseModel> success(Object data, String msg) {
        ResponseModel successModel = successModel(data, msg);
        return new ResponseEntity<ResponseModel>(successModel, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseModel> failed(String msg) {
        ResponseModel successModel = successModelBase(null, msg, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<ResponseModel>(successModel, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseModel> failed400(String msg) {
        ResponseModel successModel = successModelBase(null, msg, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ResponseModel>(successModel, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseModel> failed(final Object data, String msg) {
        ResponseModel successModel = successModelBase(data, msg, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<ResponseModel>(successModel, HttpStatus.OK);
    }

    private static ResponseModel successModel(final Object data) {
        return successModel(data, EMPTY);
    }

    public static ResponseModel successModel(final Object data, final String message) {
        return successModelBase(data, message, HttpStatus.OK);
    }

    private static ResponseModel successModelBase(final Object data, final String message, HttpStatus httpStatus) {
        ResponseModel sm = new ResponseModel();
        sm.setCode(httpStatus.value());
        sm.setMessage(message);
        sm.setData(data);
        return sm;
    }

    public static ErrorModel errorModel(final Object data, final String message) {
        ErrorModel em = new ErrorModel();
        em.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        em.setMessage(message);
        em.setData(data);
        return em;
    }

}
