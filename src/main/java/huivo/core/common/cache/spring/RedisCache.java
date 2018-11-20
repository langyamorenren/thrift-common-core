package huivo.core.common.cache.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

/**
 * Author: quan.sun@yinyuetai.com
 * Date: 2015/1/16 10:56
 */
public class RedisCache implements Cache  {
    private static final Logger LOG = LoggerFactory.getLogger(RedisCache.class);

    private String name;
    private int exp;
    private RedisTemplate redisTemplate;

    protected RedisCache(String name, int exp, RedisTemplate redisTemplate) {
        this.name = name;
        this.exp = exp;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return redisTemplate;
    }

    @Override
    public Cache.ValueWrapper get(final Object key) {
        return (Cache.ValueWrapper) redisTemplate.execute(new RedisCallback<Cache.ValueWrapper>() {
            public Cache.ValueWrapper doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bs = connection.get(makeFinalKey(key).getBytes());
                return (bs == null ? null : new SimpleValueWrapper(redisTemplate.getValueSerializer().deserialize(bs)));
            }
        }, true);
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        return null;
    }

    @Override
    public void put(Object key, final Object value) {
        if(value == null)
            return;
        if(value instanceof java.util.Collection){
            Collection c = (Collection) value;
            if(c.size() == 0)
                return;
        }
        final byte[] k = makeFinalKey(key).getBytes();
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(k, redisTemplate.getValueSerializer().serialize(value));
                if(exp > 0)
                    connection.expire(k, exp);
                return null;
            }
        }, true);
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    @Override
    public void evict(Object key) {
        final byte[] k = makeFinalKey(key).getBytes();
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(k);
                return null;
            }
        }, true);
    }

    @Override
    public void clear() {

    }

    protected String makeFinalKey(Object key){
        String stripped = "";
        if(key != null)
            stripped = key.toString().replaceAll("\\s", ":");
        return name + stripped;
    }
}
