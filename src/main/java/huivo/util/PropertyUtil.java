package huivo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xiongxj on 2015/12/1.
 */
public class PropertyUtil {

    private static Properties properties=new Properties();

    private PropertyUtil() {
    }

    static {
        InputStream in = PropertyUtil.class.getResourceAsStream("/FileCache.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从配置文件中读取设置的缓存的大小
     * @return String
     */
    public static String getFileCacheSize(){
        return properties.get("file_cache_size") ==null?"10":properties.get("file_cache_size").toString();
    }

    /**
     * 从配置文件中读取设置的缓存的大小
     * @return String
     */
    public static String getAccountRedisCacheTime(){
        return properties.get("account_redis_cache_time") ==null?"10":properties.get("account_redis_cache_time").toString();
    }

    /**
     * 根据key读取配置文件的值
     * @param key
     * @return
     */
    public static String getPropertyByKey(String key){
        return properties.get(key) ==null?"":properties.get(key).toString();
    }
}
