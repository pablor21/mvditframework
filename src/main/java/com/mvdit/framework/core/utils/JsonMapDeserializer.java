/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.core.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
public class JsonMapDeserializer<K,T> extends JsonDeserializer<Map<K,T>> {

    @Override
    public Map<K,T> deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        Map<K,T> items = jp.readValueAs(new TypeReference<Map<K,T>>() { });      
        return items;
    }
    
}
