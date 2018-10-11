package com.leo.seckill.exception;

/**
 * @Auther: Leo
 * @Date: 2018/10/11
 * @Description: 秒杀关闭异常
 */
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
