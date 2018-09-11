package com.jzfq.retail.bean.valid.validator;


import com.jzfq.retail.bean.valid.annotation.NotIn;
import com.jzfq.retail.bean.valid.util.NotInlUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NotInlValidator implements ConstraintValidator<NotIn, Object> {
    private boolean required = false;
    private String params = "";
    /**
     * 初始化
     */
    @Override
    public void initialize(NotIn constraintAnnotation) {
        required = constraintAnnotation.required();
        params = constraintAnnotation.params();
    }

    /**
     * 校验
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (required) {
            return NotInlUtil.valid(params, value);
        }
        return true;
    }

}
