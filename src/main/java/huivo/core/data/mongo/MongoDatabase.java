package huivo.core.data.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by langya on 2014/11/21.
 */
public class MongoDatabase {

    // 这部分代码没有加锁 有问题 @wuzhixin

    private static final Logger LOG = LoggerFactory.getLogger(MongoDatabase.class);

    private static MongoClient mongoClient;

    private static String database;

    public static DB getDB() {

        if (mongoClient == null) {
            try {

                Properties properties=new Properties();
                InputStream in = null;
                try {
                    in = MongoDatabase.class.getResourceAsStream("/mongo.properties");
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

                database = properties.get("mongos.db.defalut").toString();

                String address = properties.get("mongos.address").toString(); // host:port,host:port
                String[] addresses = address.split(",");

                String poolSize = properties.get("mongos.pool.size").toString();
                String blockSize = properties.get("mongos.block.size").toString();


                MongoClientOptions options = MongoClientOptions.builder()
                        .connectionsPerHost(Integer.valueOf(poolSize))
                        .threadsAllowedToBlockForConnectionMultiplier(Integer.valueOf(blockSize)).build();

                if (addresses.length > 1) {
                    List<ServerAddress> list = new ArrayList<>();
                    for (int i = 0; i < addresses.length; i++) {
                        String[] array = addresses[i].split(":");
                        list.add(new ServerAddress(array[0], Integer.parseInt(array[1])));
                    }
                    mongoClient = new MongoClient(list, options);
                } else {
                    String[] array = addresses[0].split(":");
                    mongoClient = new MongoClient(new ServerAddress(array[0], Integer.parseInt(array[1])), options);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return mongoClient.getDB(database);
    }

}
