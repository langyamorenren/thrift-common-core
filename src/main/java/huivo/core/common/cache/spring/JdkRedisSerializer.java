package huivo.core.common.cache.spring;


import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;
import java.util.Date;

/**
 * Created by langya on 16/2/1.
 */
public class JdkRedisSerializer implements RedisSerializer<Object> {
    public static final byte SPECIAL_STRING = 0;
    public static final byte SPECIAL_BOOLEAN = 1;
    public static final byte SPECIAL_INT = 2;
    public static final byte SPECIAL_LONG = 3;
    public static final byte SPECIAL_DATE = 4;
    public static final byte SPECIAL_BYTE = 5;
    public static final byte SPECIAL_FLOAT = 6;
    public static final byte SPECIAL_DOUBLE = 7;
    public static final byte SPECIAL_BYTE_ARRAY = 8;
    public static final byte SPECIAL_OBJECT = 127;

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        byte[] b;
        byte flags;
        if (o instanceof String) {
            b = encodeString((String) o);
            flags = SPECIAL_STRING;
        } else if (o instanceof Long) {
            b = encodeString(o.toString());
            flags = SPECIAL_LONG;
        } else if (o instanceof Integer) {
            b = encodeString(o.toString());
            flags = SPECIAL_INT;
        } else if (o instanceof Boolean) {
            b = new byte[]{(byte) (((Boolean) o) ? 1 : 0)};
            flags = SPECIAL_BOOLEAN;
        } else if (o instanceof Date) {
            b = encodeString(String.valueOf(((Date) o).getTime()));
            flags = SPECIAL_DATE;
        } else if (o instanceof Byte) {
            b = new byte[]{(Byte) o};
            flags = SPECIAL_BYTE;
        } else if (o instanceof Float) {
            b = encodeString(o.toString());
            flags = SPECIAL_FLOAT;
        } else if (o instanceof Double) {
            b = encodeString(o.toString());
            flags = SPECIAL_DOUBLE;
        } else if (o instanceof byte[]) {
            b = (byte[]) o;
            flags = SPECIAL_BYTE_ARRAY;
        } else {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(o);
                os.close();
                bos.close();
                b = bos.toByteArray();
            } catch (IOException e) {
                throw new IllegalArgumentException("Non-serializable object", e);
            }
            flags = SPECIAL_OBJECT;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(b.length + 1);
        try {
            baos.write(new byte[]{flags});
            baos.write(b);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        byte flat = bytes[0];
        switch (flat){
            case SPECIAL_STRING:
                return decodeString(bytes);
            case SPECIAL_LONG:
                return Long.valueOf(decodeString(bytes));
            case SPECIAL_INT:
                return Integer.valueOf(decodeString(bytes));
            case SPECIAL_BOOLEAN:
                return bytes[1] == 1;
            case SPECIAL_DATE:
                return new Date(Long.valueOf(decodeString(bytes)));
            case SPECIAL_BYTE:
                return bytes[1];
            case SPECIAL_FLOAT:
                return Float.valueOf(decodeString(bytes));
            case SPECIAL_DOUBLE:
                return Double.valueOf(decodeString(bytes));
            case SPECIAL_BYTE_ARRAY:
                byte[] bytes1 = new byte[bytes.length - 1];
                System.arraycopy(bytes, 1, bytes1, 0, bytes1.length);
                return bytes1;
            case SPECIAL_OBJECT:
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes, 1, bytes.length);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    try {
                        return ois.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

        }
        return null;
    }

    protected byte[] encodeString(String in) {
        byte[] rv;
        try {
            rv = in.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return rv;
    }

    protected String decodeString(byte[] bytes) {
        try {
            return new String(bytes, 1, bytes.length - 1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        JdkRedisSerializer serializer = new JdkRedisSerializer();
        byte[] bytes = serializer.serialize("字符串测试");
        String value = (String)serializer.deserialize(bytes);
        System.out.println(value);

        bytes = serializer.serialize(99999L);
        Long l = (Long)serializer.deserialize(bytes);
        System.out.println(l);

        bytes = serializer.serialize(9999);
        Integer i = (Integer)serializer.deserialize(bytes);
        System.out.println(i);

        bytes = serializer.serialize(true);
        Boolean b = (Boolean)serializer.deserialize(bytes);
        System.out.println(b);

        bytes = serializer.serialize(new Date());
        Date d = (Date) serializer.deserialize(bytes);
        System.out.println(d);

        bytes = serializer.serialize((byte)10);
        Byte bvalue = (Byte) serializer.deserialize(bytes);
        System.out.println(bvalue);

        bytes = serializer.serialize(10.1F);
        Float f = (Float) serializer.deserialize(bytes);
        System.out.println(f);

        bytes = serializer.serialize(10.12D);
        Double dvalue = (Double) serializer.deserialize(bytes);
        System.out.println(dvalue);

        byte[] a = new byte[]{1,2,3};
        bytes = serializer.serialize(a);
        byte[] byteArray = (byte[])serializer.deserialize(bytes);
        System.out.println(byteArray[0]);

        class ClassA implements java.io.Serializable{
            private String a = "111111";

            public String getA() {
                return a;
            }

            public void setA(String a) {
                this.a = a;
            }
        }

        bytes = serializer.serialize(new ClassA());
        ClassA classA = (ClassA)serializer.deserialize(bytes);
        System.out.println(classA.getA());
    }
}
