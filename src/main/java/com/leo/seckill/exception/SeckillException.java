package com.leo.seckill.exception;

/**
 * @Auther: Leo
 * @Date: 2018/10/11
 * @Description: 秒杀相关业务异常
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
