package com.wmt.framework.serialization.engine;

import avro.shaded.com.google.common.collect.Maps;
import com.wmt.framework.serialization.common.SerializeType;
import com.wmt.framework.serialization.serializer.ISerializer;
import com.wmt.framework.serialization.serializer.impl.*;

import java.util.Map;

/**
 * Created by weimiantong on 18/11/11.
 */
public class SerializerEngine {
    public static final Map<SerializeType, ISerializer> serializerMap = Maps.newConcurrentMap();

    static {
        serializerMap.put(SerializeType.DefaultJavaSerializer, new DefaultJavaSerializer());
        serializerMap.put(SerializeType.HessianSerializer, new HessianSerializer());
        serializerMap.put(SerializeType.JSONSerializer, new JSONSerializer());
        serializerMap.put(SerializeType.XmlSerializer, new XmlSerializer());
        serializerMap.put(SerializeType.ProtoStuffSerializer, new ProtoStuffSerializer());
        serializerMap.put(SerializeType.MarshallingSerializer, new MarshallingSerializer());

        //以下三类不能使用普通的java bean
        serializerMap.put(SerializeType.AvroSerializer, new AvroSerializer());
        serializerMap.put(SerializeType.ThriftSerializer, new ThriftSerializer());
        serializerMap.put(SerializeType.ProtocolBufferSerializer, new ProtocolBufferSerializer());
    }

    public static <T> byte[] serialize(T obj, String serializeType) {
        ISerializer serializer = getISerializer(serializeType);
        try {
            return serializer.serialize(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static  <T> T deserialize(byte[] data, Class<T> clazz, String serializeType) {
        ISerializer serializer = getISerializer(serializeType);
        try {
            return serializer.deserialize(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ISerializer getISerializer(String serializeType) {
        SerializeType serialize = SerializeType.queryByType(serializeType);
        if (serialize == null) {
            throw new RuntimeException("serialize is null");
        }

        ISerializer serializer = serializerMap.get(serialize);
        if (serialize == null) {
            throw new RuntimeException("target serializeType is unknow!");
        }
        return serializer;
    }

}
