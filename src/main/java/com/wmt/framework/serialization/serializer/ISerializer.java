package com.wmt.framework.serialization.serializer;

/**
 * Created by weimiantong on 18/11/11.
 */
public interface ISerializer {
    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     */
     <T> byte[] serialize(T obj);


    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
     <T> T deserialize(byte[] data, Class<T> clazz);
}
