package cn.trustway.nb.redislock.test;

import cn.trustway.nb.redislock.annotation.Lock;
import cn.trustway.nb.redislock.annotation.LockKey;
import org.springframework.stereotype.Service;

/**
 * Created by kl on 2017/12/29.
 */
@Service
public class TestService {

    @Lock(waitTime = Long.MAX_VALUE, leaseTime = 1)
    public String getValue(String param) throws Exception {
        if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
            Thread.sleep(1000 * 3);
        }
        return "success";
    }

    @Lock(keys = {"#userId"})
    public String getValue(String userId, @LockKey int id) throws Exception {
        Thread.sleep(60 * 1000);
        return "success";
    }

    @Lock(keys = {"#user.name", "#user.id"})
    public String getValue(User user) throws Exception {
        Thread.sleep(60 * 1000);
        return "success";
    }

}
