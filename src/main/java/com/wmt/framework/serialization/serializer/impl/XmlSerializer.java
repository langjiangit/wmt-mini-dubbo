package com.wmt.framework.serialization.serializer.impl;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wmt.framework.serialization.serializer.ISerializer;

/**
 * @author liyebing created on 17/1/21.
 * @version $Id$
 */
public class XmlSerializer implements ISerializer {

    private static final XStream xStream = new XStream(new DomDriver());


    @Override
    public <T> byte[] serialize(T obj) {
        return xStream.toXML(obj).getBytes();
    }


    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        String xml = new String(data);
        return (T) xStream.fromXML(xml);
    }


}
