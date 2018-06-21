package org.springframework.boot.autoconfigure.redislock;

import org.springframework.boot.autoconfigure.redislock.config.LockConfig;
import org.springframework.boot.autoconfigure.redislock.core.BusinessKeyProvider;
import org.springframework.boot.autoconfigure.redislock.core.LockAspectHandler;
import org.springframework.boot.autoconfigure.redislock.core.LockInfoProvider;
import org.springframework.boot.autoconfigure.redislock.lock.LockFactory;
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
