package com.jzfq.retail.core.config;


import com.jzfq.retail.bean.vo.res.ErrorModel;
import com.jzfq.retail.common.exception.BadRequestException;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


//@ControllerAdvice
public class GlobalExceptionHandler {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ErrorModel exceptionHandler(Exception e, HttpServletResponse response) {
        ErrorModel resp = new ErrorModel();
        LOGGER.error("Exception-{}", e.getMessage());
        if(e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException)e;
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String defaultMessage = objectError.getDefaultMessage();
            resp.setCode(HttpStatus.BAD_REQUEST.value());
            resp.setMessage(defaultMessage);
        }else{
            resp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("服务器异常，请联系管理员:" + e.getMessage());
        }
        return resp;
    }

    @ExceptionHandler(value = {BusinessException.class})
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public ErrorModel exceptionHandler(BusinessException e, HttpServletResponse response) {
        LOGGER.error("BusinessException - {}", e.getMessage());
        ErrorModel resp = new ErrorModel();
        resp.setCode(       e.getCode());
        resp.setMessage(    e.getMessage());
        return resp;
    }

    @ExceptionHandler(value = {DBException.class})
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public ErrorModel exceptionHandler(DBException e, HttpServletResponse response) {
        LOGGER.error("DBException - {}", e.getMessage());
        ErrorModel resp = new ErrorModel();
        resp.setCode(       HttpStatus.INTERNAL_SERVER_ERROR.value());
        resp.setMessage(    e.getMessage());
        return resp;
    }

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public ErrorModel exceptionHandler(BadRequestException e, HttpServletResponse response) {
        LOGGER.error("BadRequestException - {}", e.getMessage());
        ErrorModel resp = new ErrorModel();
        resp.setCode(       e.getCode());
        resp.setMessage(    e.getMessage());
        return resp;
    }

}
