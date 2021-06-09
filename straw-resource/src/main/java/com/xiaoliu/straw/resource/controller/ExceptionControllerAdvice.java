package com.xiaoliu.straw.resource.controller;


import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 * 对当前控制层所有方法进行前置后置的增强
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice extends Exception{

    // ExceptionHandler表示当控制器方法发生异常时匹配下面的异常处理
    @ExceptionHandler
    // 这个方法用于处理当控制器发生ServiceException时处理
    public R handerServiceException(ServiceException e){
        log.error("发生业务异常",e);
        return R.failed(e);
    }

    // 这个方法用于处理当控制器发生Exception时处理
    @ExceptionHandler
    public R handerException(Exception e){
        log.error("其他异常",e);
        return R.failed(e);
    }

    public ExceptionControllerAdvice() {
    }

    public ExceptionControllerAdvice(String message) {
        super(message);
    }

    public ExceptionControllerAdvice(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionControllerAdvice(Throwable cause) {
        super(cause);
    }

    public ExceptionControllerAdvice(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
