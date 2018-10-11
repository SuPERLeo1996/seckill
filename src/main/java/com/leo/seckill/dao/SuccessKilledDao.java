package com.leo.seckill.dao;

import com.leo.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: Leo
 * @Date: 2018/10/8
 * @Description:
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);


    /**
     * 根据id查询SuccessKilled并携带秒杀对象实体
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);


}
