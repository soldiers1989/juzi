package com.jzfq.retail.core.config;


import com.jzfq.retail.bean.vo.res.TouchApiResponse;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseEntity<TouchResponseModel> exceptionHandler(Exception e, HttpServletResponse response) {
        LOGGER.error("Exception-{}", e.getMessage());
        if(e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException)e;
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String defaultMessage = objectError.getDefaultMessage();
            return TouchApiResponse.badRequest(defaultMessage);
        }else if(e instanceof TouchCodeException){
            TouchCodeException te = (TouchCodeException) e;
            return TouchApiResponse.failed(te.getTouchApiCode().getCode(), te.getTouchApiCode().getMsg() + te.getExtendMsg());
        }else{
            return TouchApiResponse.failed(e.getMessage());
        }
    }


}
