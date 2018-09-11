package com.jzfq.retail.bean.valid.validator;


import com.jzfq.retail.bean.valid.annotation.NotNull;
import com.jzfq.retail.bean.valid.util.NotNullUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NotNullValidator implements ConstraintValidator<NotNull, Object> {
    private boolean required = false;
    /**
     * 初始化
     */
    @Override
    public void initialize(NotNull constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    /**
     * 校验
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (required) {
            return NotNullUtil.valid(value);
        }
        return true;
    }

}
