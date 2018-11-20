package huivo.core.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by langya on 2014/11/10.
 */
public class RedisConnection {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConnection.class);

    private static RedisConnection redisConnection;

    private String host;
    private int port;

    private JedisPool pool;

    private RedisConnection() {
        Properties properties=new Properties();
        InputStream in = null;
        try {
            in = RedisConnection.class.getResourceAsStream("/redis.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != in){
                try {
                    in.close();
                }catch (IOException e){
                    LOG.error("fail to close input stream");
                }
            }
        }
        host = properties.get("redis.db.host").toString();
        port = Integer.valueOf(properties.get("redis.db.port").toString());

        LOG.error("host={}, port={}",host, port);
    }

    public static RedisConnection getInstance() {
        if (redisConnection == null) {
            redisConnection = new RedisConnection();
        }
        return redisConnection;
    }

    public Jedis getConnection() {
        if (pool == null) {
            pool = new JedisPool(new JedisPoolConfig(), host, port,
                    Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
        }
        return pool.getResource();
    }

    public Jedis getConnection(String host, int port) {
        if (pool == null) {
            pool = new JedisPool(new JedisPoolConfig(), host, port,
                    Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
        }
        return pool.getResource();
    }
}
