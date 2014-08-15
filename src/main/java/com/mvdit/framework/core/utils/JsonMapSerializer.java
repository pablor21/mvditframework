/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.core.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 */
public class JsonMapSerializer<K, T> extends JsonSerializer<Map<K,T>> {

    @Override
    public void serialize(Map<K,T> t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeStartArray();
        Iterator<K> it= t.keySet().iterator();
        while(it.hasNext()){
            K key= it.next();
            jg.writeObject(t.get(key));
        }
        /*for(T elem:t){
            jg.writeObject(elem);
        }*/
        jg.writeEndArray();
    } 
}
