package huivo.core.common.thrift;


import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Created by langya on 2015/01/31.
 */
public class ThriftConnection {
    final Logger LOG = LoggerFactory.getLogger(ThriftConnection.class);
    private TTransport transport = null;
    private TSocket socket = null;
    private TServiceClient client = null;

    public <T extends TServiceClient> ThriftConnection(Class<T> serviceClientClass, String host, int port) {
        try {
            socket = new TSocket(host, port, 30000);
            transport = new TFramedTransport(socket);
            // 协议要和服务端一致
            TProtocol protocol = new TCompactProtocol(transport);
            Constructor<T> constructor = serviceClientClass.getConstructor(TProtocol.class);
            client = constructor.newInstance(protocol);
            transport.open();
        } catch (Exception e) {
            throw new RuntimeException(String.format("create thrift connection error host:%s, post:%d", host, port), e);
        }
    }

    public void connect() {
        try {
            transport.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        transport.close();
    }

    public boolean isValidate() {
        boolean flag = true;
        try {
            socket.getSocket().sendUrgentData(0xFF);
        } catch (IOException e) {
            //e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public TServiceClient getServiceClient() {
        return client;
    }

}
