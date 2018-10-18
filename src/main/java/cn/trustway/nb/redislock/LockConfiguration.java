package cn.trustway.nb.redislock;

import cn.trustway.nb.redislock.core.BusinessKeyProvider;
import cn.trustway.nb.redislock.core.LockAspectHandler;
import cn.trustway.nb.redislock.core.LockInfoProvider;
import cn.trustway.nb.redislock.config.LockConfig;
import cn.trustway.nb.redislock.lock.LockFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by kl on 2017/12/29.
 * Content :适用于内部低版本spring mvc项目配置,redisson外化配置
 */
@Configuration
@Import({LockAspectHandler.class})
public class LockConfiguration {
    @Bean
    public LockInfoProvider lockInfoProvider() {
        return new LockInfoProvider();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider() {
        return new BusinessKeyProvider();
    }

    @Bean
    public LockFactory lockFactory() {
        return new LockFactory();
    }

    @Bean
    public LockConfig klockConfig() {
        return new LockConfig();
    }
}
