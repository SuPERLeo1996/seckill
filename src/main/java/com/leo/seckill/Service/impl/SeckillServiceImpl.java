package com.leo.seckill.Service.impl;

import com.leo.seckill.Service.SeckillService;
import com.leo.seckill.dao.SeckillDao;
import com.leo.seckill.dao.SuccessKilledDao;
import com.leo.seckill.dto.Exposer;
import com.leo.seckill.dto.SeckillExecution;
import com.leo.seckill.entity.Seckill;
import com.leo.seckill.entity.SuccessKilled;
import com.leo.seckill.enums.SeckillStateEnum;
import com.leo.seckill.exception.RepeatKillException;
import com.leo.seckill.exception.SeckillCloseException;
import com.leo.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @Auther: Leo
 * @Date: 2018/10/14
 * @Description:
 */
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    private final String salt = "sadadadasdasd12312@!#$!@$@#$@$#$%@asd";
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime()<startTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId+"/"+salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑:减库存，记录购买行为
        Date nowTime = new Date();
        int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
        try {
            if (updateCount <= 0){
                //没有更新到记录,秒杀结束
                throw new SeckillCloseException("seckill is closed");
            }else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                //唯一；seckillId,userPhone
                if (insertCount<=0){
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                }else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2){
            throw e2;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译期异常，转化为运行期异常
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }

    }
}
