package huivo.core.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * Created by langya on 2014/11/13.
 */
public class RedisUtil {


    public static int getCommonData(String key, Jedis jedis) {
        int i = 0;
        String value = jedis.get(key);
        if (value != null && !"".equals(value)) {
            i = Integer.parseInt(value);
        }
        return i;
    }

    public static void saveKeyMapData(String parentKey, String childKey, double value) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            jedis.hincrByFloat(parentKey, childKey, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static Map<String, String> getKeyMapData(String key) {
        Jedis jedis = null;
        Map<String, String> map = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            map = jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return map;
    }

    public static List<String> getListData(String key) {
        Jedis jedis = null;
        List<String> list = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            list = jedis.lrange(key, 0, jedis.llen(key));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return list;
    }

    public static long lpush(String key, String... data) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.lpush(key, data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public static long lpushx(String key, String... data) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.lpushx(key, data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public static long rpush(String key, String... data) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.rpush(key, data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public static boolean ltrim(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return "OK".equals(jedis.ltrim(key, start, end));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public static long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.llen(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


    public static List<String> lrange(final String key, final long start, final long end){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static long hset(final String key, final String field, final String value){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public static Map<String, String> hgetAll(final String key){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public static long zadd(final String key, final String member, final double score){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public static long zcount(final String key, final double min, final double max){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zcount(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }


    public static Set<String> zrangeByScore(String key, double max, double min, int offset, int count){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public static Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        //注意，这里取得是闭区间结果集
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrangeByScoreWithScores(key, min, max,offset, count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static long zrem(final String key, final String member){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrem(key, member);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }


    public static Set<String> zrange(String key, long start, long end){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public static Set<String> zrevrange(String key, long start, long end){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static Set<Tuple> zrevrangeWithScores(final String key, final long start,final long end) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.zrevrangeWithScores(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static List<String> hvals(final String key){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.hvals(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public static long hdel(final String key, final String field){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.hdel(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }

    public static String get(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            result = jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

//    public static long setnxKeyValue(String key, String value) {
//        Jedis jedis = null;
//        try {
//            jedis = RedisConnection.getInstance().getConnection();
//            return jedis.in (key, value);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//        return -1L;
//    }

    public static boolean set(String key, String value, long ttl) {
        // ttl in seconds
        Jedis jedis = null;
        boolean bRet = false;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            if ("OK".equals(jedis.set(key, value)) && 1 == jedis.expire(key, (int) ttl)) {
                bRet = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return bRet;
    }

    public static boolean incrKeyByValue(String key, long value) {
        // ttl in seconds
        Jedis jedis = null;
        boolean bret = false;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            jedis.incrBy(key, value);
            bret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return bret;
    }

    public static boolean setNoExistKey(String key, String value){
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.setnx(key, value) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public static boolean decrKeyByValue(String key, long value) {
        // ttl in seconds
        Jedis jedis = null;
        boolean bret = false;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            jedis.decrBy(key, value);
            bret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return bret;
    }

    public static Set<String> keys(String patten) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            return jedis.keys(patten);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static void expireKey(String key, long ttl) {
        // ttl in seconds
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            jedis.expire(key, (int) ttl);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public static void del(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getInstance().getConnection();
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
