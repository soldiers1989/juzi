package com.jzfq.retail.core.datasource;

import com.jzfq.retail.common.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 *
 *
 * Description: 拦截serviceImpl
 *
 * @author liuwei
 * @version V1.0
 */
@Slf4j
//@Aspect
//@Component
public class LogSaveComponent {



    @Autowired
    private TaskExecutor taskExecutor;

    @Pointcut("execution(* com.jzfq..retail.core.service.impl..*.*(..))")
	private void jerseyLog(){}

    @Before(value="jerseyLog()")
	public void beforeJersey(JoinPoint point){
		String args = point.getArgs().toString();
		String className = point.getStaticPart().getSignature().getDeclaringTypeName();
		String methodName = point.getStaticPart().getSignature().getName();
		System.out.println(className+"."+methodName+":["+args+"],result:["+new JsonMapper().toJson(point.getArgs())+"]");
	}

    @AfterReturning(value="jerseyLog()", returning="val")
    public void afterJerseyException(JoinPoint point, Object val) throws Exception{

        log.info("进入拦截");

    }
}
