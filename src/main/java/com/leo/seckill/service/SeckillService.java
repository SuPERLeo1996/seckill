package com.leo.seckill.service;

import com.leo.seckill.dto.Exposer;
import com.leo.seckill.dto.SeckillExecution;
import com.leo.seckill.entity.Seckill;
import com.leo.seckill.exception.RepeatKillException;
import com.leo.seckill.exception.SeckillCloseException;
import com.leo.seckill.exception.SeckillException;

import java.util.List;

/**
 * @Auther: Leo
 * @Date: 2018/10/11
 * @Description:
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀地址，
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;


    /**
     * 执行秒杀操作 by 存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;


}
