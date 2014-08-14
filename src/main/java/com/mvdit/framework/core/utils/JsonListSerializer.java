/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.core.utils;

import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 */
public class JsonListSerializer<T> extends JsonSerializer<List<T>> {

    @Override
    public void serialize(List<T> t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeStartArray();
        for(T elem:t){
            jg.writeObject(elem);
        }
        jg.writeEndArray();
    } 
}
