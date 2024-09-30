package luckyvicky.petharmony.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Arrays;

@Configuration
public class CacheAdapter {

    @Bean
    public CacheManager compositeCacheManager(RedisCacheManager redisCacheManager, ConcurrentMapCacheManager concurrentMapCacheManager) {
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        cacheManager.setCacheManagers(Arrays.asList(redisCacheManager, concurrentMapCacheManager));
        cacheManager.setFallbackToNoOpCache(true);
        return cacheManager;
    }
}