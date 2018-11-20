package huivo.core.common.cache.spring;

import huivo.core.common.cache.spring.RedisCache;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author: quan.sun@yinyuetai.com
 * Date: 2015/1/16 10:52
 */
public class RedisCacheManager implements org.springframework.cache.CacheManager {
    private RedisTemplate redisTemplate;

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
    private final Collection<String> names = Collections.unmodifiableSet(caches.keySet());

    @Override
    public Cache getCache(String name) {
        int exp = 0;
        if(name.indexOf(",") > 0){
            String[] strs = name.split(",");
            name = strs[0];
            exp = Integer.valueOf(strs[1]);
        }
        name += ":"; //防止name以数字结尾，key也为数字，会造成cache的key混乱，所以默认加“_";
        Cache c = caches.get(name);
        if(c == null){
            c = new RedisCache(name, exp, redisTemplate);
            caches.put(name, c);
        }
        return c;
    }

    @Override
    public Collection<String> getCacheNames() {
        return names;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}