package com.jzfq.retail.bean.valid.annotation;

import com.jzfq.retail.bean.valid.validator.NotInlValidator;
import com.jzfq.retail.bean.valid.validator.NotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证不在其中
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { NotInlValidator.class })
public @interface NotIn {
    boolean required() default true;

    String message() default "不在其中";

    String params() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
