package org.springframework.boot.autoconfigure.redislock.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.redislock.annotation.Lock;
import org.springframework.boot.autoconfigure.redislock.config.LockConfig;
import org.springframework.boot.autoconfigure.redislock.model.LockInfo;
import org.springframework.boot.autoconfigure.redislock.model.LockType;

/**
 * Created by kl on 2017/12/29.
 */
public class LockInfoProvider {

    public static final String LOCK_NAME_PREFIX = "lock";
    public static final String LOCK_NAME_SEPARATOR = ":";


    @Autowired
    private LockConfig lockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    public LockInfo get(ProceedingJoinPoint joinPoint, Lock lock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type = lock.lockType();
        String businessKeyName = businessKeyProvider.getKeyName(joinPoint, lock);
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(lock.name(), signature) + businessKeyName;
        long waitTime = getWaitTime(lock);
        long leaseTime = getLeaseTime(lock);
        return new LockInfo(type, lockName, waitTime, leaseTime);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s%s%s", signature.getDeclaringTypeName(), LOCK_NAME_SEPARATOR, signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(Lock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                lockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(Lock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                lockConfig.getLeaseTime() : lock.leaseTime();
    }
}
