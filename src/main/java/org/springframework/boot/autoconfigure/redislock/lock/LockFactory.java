package org.springframework.boot.autoconfigure.redislock.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.redislock.annotation.Lock;
import org.springframework.boot.autoconfigure.redislock.core.LockInfoProvider;
import org.springframework.boot.autoconfigure.redislock.model.LockInfo;
import org.springframework.boot.autoconfigure.redislock.model.LockType;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kl on 2017/12/29.
 * Content :
 */
public class LockFactory {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LockInfoProvider lockInfoProvider;

    private static final Map<LockType, org.springframework.boot.autoconfigure.redislock.lock.Lock> lockMap = new HashMap<>();

    @PostConstruct
    public void init() {
        lockMap.put(LockType.Reentrant, new ReentrantLock(redissonClient));
        lockMap.put(LockType.Fair, new FairLock(redissonClient));
        lockMap.put(LockType.Read, new ReadLock(redissonClient));
        lockMap.put(LockType.Write, new WriteLock(redissonClient));
        logger.info("Lock Initialization Successful");
    }

    public org.springframework.boot.autoconfigure.redislock.lock.Lock getLock(ProceedingJoinPoint joinPoint, Lock lock) {
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, lock);
        return lockMap.get(lockInfo.getType()).setLockInfo(lockInfo);
    }

}
