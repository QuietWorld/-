package com.leyou.item.advice;

import com.leyou.item.exception.LeyouException;
import com.leyou.item.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * 通用异常处理类，
 * 通常情况下，service层因为有事需要回滚，所有一般dao层和service层的异常一般都是直接抛出给controller。
 * 切入点表达式匹配controller中的每个方法，如果方法出现异常，就执行after-throwing中的代码，如果
 * 正常执行则执行after-returning中的代码，这是aop的思想，但是这样写通知类很麻烦，我们只需要使用@ControllerAdvice来声明
 * 当前类是controller配置的通知类，它就会去监控所有controller中的所有方法的执行。
 * @author zc
 */
@ControllerAdvice
public class CommonExceptionAdvice {

    /**
     * 使用@ExceptionHandler来声明当前方法是一个异常处理方法，通过@ExceptionHander的属性来指定如果controller中
     * 目标方法出现什么异常则执行该方法。
     * @param e    目标方法抛出的异常
     * @return
     */
    @ExceptionHandler(LeyouException.class)
    public ResponseEntity<ExceptionResult> handleException(LeyouException e){
        return ResponseEntity.status(e.getEm().getStatusCode()).body(new ExceptionResult(e.getEm()));
    }
}
