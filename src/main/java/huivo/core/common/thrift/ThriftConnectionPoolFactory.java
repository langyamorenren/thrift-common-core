package huivo.core.common.thrift;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.TServiceClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by langya on 16/1/11.
 */
public class ThriftConnectionPoolFactory {

    private static final Map<Object, ObjectPool<ThriftConnection>> connectionPoolMap = new HashMap<>();

    public static <T extends TServiceClient> ObjectPool<ThriftConnection> getThriftConnectionPool(final Class<T> serviceClientClass,
                                                                                                  final String host, final int port){
        return getThriftConnectionPool(serviceClientClass, host, port, 32);
    }

    public static <T extends TServiceClient> ObjectPool<ThriftConnection> getThriftConnectionPool(final Class<T> serviceClientClass,
                                                                                                  final String host, final int port, int maxActive) {
        if(!connectionPoolMap.containsKey(serviceClientClass)) {
            synchronized (serviceClientClass) {
                if (!connectionPoolMap.containsKey(serviceClientClass)) {
                    connectionPoolMap.put(serviceClientClass, new GenericObjectPool(new BasePoolableObjectFactory<ThriftConnection>() {

                        @Override
                        public ThriftConnection makeObject() throws Exception {
                            return new ThriftConnection(serviceClientClass, host, port);
                        }

                        @Override
                        public void destroyObject(ThriftConnection o) throws Exception {
                            o.disconnect();
                        }

                        @Override
                        public boolean validateObject(ThriftConnection obj) {
                            return obj.isValidate();
                        }


                    }, maxActive, GenericObjectPool.WHEN_EXHAUSTED_BLOCK, 100, true, true));
                }
            }
        }
        return (ObjectPool<ThriftConnection>) connectionPoolMap.get(serviceClientClass);
    }

    private ThriftConnectionPoolFactory(){}


}
