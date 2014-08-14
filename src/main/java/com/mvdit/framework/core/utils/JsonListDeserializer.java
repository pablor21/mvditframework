/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.core.utils;

import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 */
public class JsonListDeserializer<T> extends JsonDeserializer<List<T>> {

    @Override
    public List<T> deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        List<T> items = jp.readValueAs(new TypeReference<List<T>>() { });      
        return items;
    }
    
}
