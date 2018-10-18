package cn.trustway.nb.redislock.lock;

import cn.trustway.nb.redislock.model.LockInfo;

/**
 * Created by kl on 2017/12/29.
 */
public interface Lock {
    Lock setLockInfo(LockInfo lockInfo);

    LockInfo getLockInfo();

    boolean acquire();

    void release();
}
