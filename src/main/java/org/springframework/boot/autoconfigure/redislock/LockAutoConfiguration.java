package org.springframework.boot.autoconfigure.redislock;

import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.redislock.config.LockConfig;
import org.springframework.boot.autoconfigure.redislock.core.BusinessKeyProvider;
import org.springframework.boot.autoconfigure.redislock.core.LockAspectHandler;
import org.springframework.boot.autoconfigure.redislock.core.LockInfoProvider;
import org.springframework.boot.autoconfigure.redislock.lock.LockFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

/**
 * Created by kl on 2017/12/29.
 * Content :klock自动装配
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(LockConfig.class)
@Import({LockAspectHandler.class})
public class LockAutoConfiguration {

    @Autowired
    private LockConfig lockConfig;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redisson() throws Exception {
        Config config = new Config();
        if (lockConfig.getClusterServer() != null) {
            config.useClusterServers().setPassword(lockConfig.getPassword())
                    .addNodeAddress(lockConfig.getClusterServer().getNodeAddresses());
        } else {
            config.useSingleServer().setAddress(lockConfig.getAddress())
                    .setDatabase(lockConfig.getDatabase())
                    .setPassword(lockConfig.getPassword());
        }
        Codec codec = (Codec) ClassUtils.forName(lockConfig.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

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
}
