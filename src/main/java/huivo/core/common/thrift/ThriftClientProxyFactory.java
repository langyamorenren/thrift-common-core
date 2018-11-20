package huivo.core.common.thrift;

import org.apache.commons.pool.ObjectPool;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by langya on 16/1/11.
 */
public class ThriftClientProxyFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ThriftConnection.class);
    private static Map<Object, Object> proxyMap = new HashMap<>();

    private ThriftClientProxyFactory(){}

    public static  <T extends TServiceClient, I> I getProxy(final Class<T> serviceClientClass, final Class<I> ifaceClass,
                                                            final String host, final int port){
        return getProxy(serviceClientClass, ifaceClass, host, port, 8);
    }

    public static  <T extends TServiceClient, I> I getProxy(final Class<T> serviceClientClass, final Class<I> ifaceClass,
                                                            final String host, final int port, final int maxActive){
        if(!proxyMap.containsKey(ifaceClass)){
            synchronized (ifaceClass){
                if(!proxyMap.containsKey(ifaceClass)) {
                    Object proxy = Proxy.newProxyInstance(ThriftClientProxyFactory.class.getClassLoader(), new Class[]{ifaceClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            ObjectPool<ThriftConnection> connectionPool = ThriftConnectionPoolFactory.getThriftConnectionPool(serviceClientClass, host, port, maxActive);
                            Object returnObj = null;
                            long l = System.currentTimeMillis();
                            //当出现TTransportException时重试,默认不重试
                            int retryCount = 0;
                            for(int i = 0; i < retryCount + 1; i++) {
                                ThriftConnection connection = connectionPool.borrowObject();
                                try {
                                    TServiceClient client = connection.getServiceClient();
                                    returnObj = method.invoke(client, args);
                                    break;
                                } catch (InvocationTargetException ex) {
                                    Throwable t = ex.getTargetException();
                                    if (t instanceof TTransportException) {
                                        if(i < retryCount)
                                        continue;
                                        throw t;
                                    }
                                    if (t instanceof TApplicationException) {
                                        TApplicationException applicationException = (TApplicationException) t;
                                        if (applicationException.getType() == TApplicationException.MISSING_RESULT)
                                            return null;
                                        throw t;
                                    }
                                    throw t;
                                } catch (Throwable t) {
                                    throw t;
                                } finally {
                                    connectionPool.returnObject(connection);
                                }
                            }
                            LOG.info(String.format("call %s.%s span %d", ifaceClass,method.getName(),System.currentTimeMillis() - l));
                            return returnObj;
                        }
                    });
                    proxyMap.put(ifaceClass, proxy);
                }
            }
        }
        return (I)proxyMap.get(ifaceClass);
    }
}
