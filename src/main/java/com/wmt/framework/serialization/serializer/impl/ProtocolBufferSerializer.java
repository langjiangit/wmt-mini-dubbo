package com.wmt.framework.serialization.serializer.impl;

import com.wmt.framework.serialization.serializer.ISerializer;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * Created by weimiantong on 18/12/1.
 */
public class ProtocolBufferSerializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) {
        try {
            return (byte[]) MethodUtils.invokeMethod(obj, "toByteArray");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            Object o = MethodUtils.invokeStaticMethod(cls, "getDefaultInstance");
            return (T) MethodUtils.invokeMethod(o, "parseFrom", new Object[]{data});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
