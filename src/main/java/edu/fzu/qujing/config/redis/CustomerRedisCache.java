package edu.fzu.qujing.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class CustomerRedisCache extends RedisCache {

    private final String name;
    private final RedisCacheWriter cacheWriter;
    private final ConversionService conversionService;

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected CustomerRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.cacheWriter=cacheWriter;
        this.conversionService = cacheConfig.getConversionService();
    }

    @Override
    public void evict(Object key) {
        String wildcard = "*)";
        if(key instanceof  String){
            String keyString=key.toString();
            if(StringUtils.endsWith(keyString,wildcard)){
                evictLikeSuffix(keyString);
                return;
            }
            if(StringUtils.startsWith(keyString,wildcard)){
                evictLikePrefix(keyString);
                return;
            }
        }
        super.evict(key);
    }

    /**
     * 前缀匹配
     * @param key
     */
    public void evictLikePrefix(String key) {
        byte[] pattern = this.conversionService.convert(this.createCacheKey(key), byte[].class);
        this.cacheWriter.clean(this.name, pattern);
    }

    /**
     * 后缀匹配
     * @param key
     */
    public void evictLikeSuffix(String key) {
        byte[] pattern = (byte[])this.conversionService.convert(this.createCacheKey(key), byte[].class);
        this.cacheWriter.clean(this.name, pattern);
    }

}
