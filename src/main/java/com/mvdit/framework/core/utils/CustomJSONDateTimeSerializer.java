/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.core.utils;

import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.core.PropertiesRepository;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author pramirez
 */
public class CustomJSONDateTimeSerializer  extends JsonSerializer<Date>{
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws 
        IOException, JsonProcessingException {      

        SimpleDateFormat formatter = new SimpleDateFormat(PropertiesRepository.getInstance().getProperty("mvdit.datetime.format"));
        String formattedDate = formatter.format(value);

        gen.writeString(formattedDate);

    }

 
}
