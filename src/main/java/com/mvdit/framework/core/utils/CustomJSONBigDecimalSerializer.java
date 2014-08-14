/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.core.utils;

import com.mvdit.framework.core.PropertiesRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author pramirez
 */
public class CustomJSONBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider arg2) throws
            IOException, JsonProcessingException {

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator(PropertiesRepository.getInstance().getProperty("mvdit.decimal.separator").charAt(0));
        otherSymbols.setGroupingSeparator(PropertiesRepository.getInstance().getProperty("mvdit.decimal.grouping").charAt(0));
        DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00", otherSymbols);
        df2.format(value);
        value.toString();

        gen.writeString(df2.format(value));

    }

}
