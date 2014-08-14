/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.core.utils;

import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.core.PropertiesRepository;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 *
 * @author pramirez
 */
public class CustomJSONDateTimeDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat(PropertiesRepository.getInstance().getProperty("mvdit.datetime.format"));
        String date = jp.getText();
        try {
            return formatter.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        
    }
}
