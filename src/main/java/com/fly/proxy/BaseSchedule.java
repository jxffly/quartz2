package com.fly.proxy;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by bupengwen on 2016/4/22.
 * 用于处理定时任务
 */
public abstract class BaseSchedule {
    
    protected final static int PAGE_SIZE = 30;
    
    protected final static int MAX_COUNT_PER_TASK = 1000;
    
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected final AtomicBoolean not_running_mutex = new AtomicBoolean(true);
    
    
    private void addContextOperator() {
        
    }
    
    
    
    
    //    @HasProblem(value = "如果这个锁被锁定了,就无法释放.需要实现一个锁过期的策略", level = ProblemLevel.MINOR)
    public void doSchedule(JobExecutionContext context) {
        if (isScheduleOpen()) {
            long start = System.currentTimeMillis();
            logger.info("{} schedule start:{}", getScheduleName(), start);
            if (not_running_mutex.compareAndSet(true, false)) {
                try {
                    doTask(context);
                } catch (Throwable e) {
                    logger.error("{} execute error:", getScheduleName(), e);
                } finally {
                    not_running_mutex.set(true);
                }
                long end = System.currentTimeMillis();
                logger.info("{} schedule  execute costs:{} ms", getScheduleName(), (end - start));
            }else {
                logger.warn("schedule Task({})re-enter,please analysis task execute time or whether have fence obstruction ", getScheduleName());
            }
            logger.info("{} schedule  end", getScheduleName());
        }else {
            logger.warn("schedule Task[{}] have dynamic config or restrictions Active Suspend", getScheduleName());
        }
    }
    
    
    /**
     * 需要支持动态配置或者其条件->暂时停止这个定时任务
     */
    public abstract boolean isScheduleOpen();
    
    
    public String getScheduleName() {
        return getClass().getSimpleName();
    }
    
    
    /**
     * 执行任务,子类实现
     */
    public abstract void doTask(JobExecutionContext jobExecutionContext) throws Throwable;
    
}

