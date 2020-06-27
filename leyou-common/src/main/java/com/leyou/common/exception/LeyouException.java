package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnum;

/**
 * 通用异常类
 * @author zc
 */
public class LeyouException extends RuntimeException{

    private ExceptionEnum em;

    public LeyouException(ExceptionEnum em) {
        this.em = em;
    }

    public ExceptionEnum getEm() {
        return em;
    }

}
