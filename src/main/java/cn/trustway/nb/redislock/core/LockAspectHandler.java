package cn.trustway.nb.redislock.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import cn.trustway.nb.redislock.lock.LockFactory;
import org.springframework.stereotype.Component;

/**
 * Created by kl on 2017/12/29.
 * Content :给添加@Lock切面加锁处理
 *
 * Modify by july on 2018/07/27
 * Content: 在waitTimeout超时后，抛出异常，而不是执行
 */
@Aspect
@Component
public class LockAspectHandler {

    @Autowired
    LockFactory lockFactory;

    @Around(value = "@annotation(klock)")
    public Object around(ProceedingJoinPoint joinPoint, cn.trustway.nb.redislock.annotation.Lock klock) throws Throwable {
        cn.trustway.nb.redislock.lock.Lock lock = lockFactory.getLock(joinPoint, klock);
        boolean currentThreadLock = false;
        try {
            currentThreadLock = lock.acquire();
            if (currentThreadLock) {
                return joinPoint.proceed();
            } else {
                String name = lock.getLockInfo().getName();
                throw new LockAcquireException("lock acquire failed because wait timeout, lock name: [" + name + "]");
            }
        } finally {
            if (currentThreadLock) {
                lock.release();
            }
        }
    }
}
