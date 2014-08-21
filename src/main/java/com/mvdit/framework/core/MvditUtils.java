/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.core;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Pablo Ramírez
 */
public class MvditUtils {

    public static boolean stringEmpty(String str) {
        return (null == str || "".equals(str.trim()) || str.isEmpty());
    }

    public static Object getValue(String key, Map<String, Object> obj, Class type) {
        if (obj.containsKey(key)) {
            type.cast(obj);
        }

        return null;

    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static String encriptString(String method, String msg) {

        try {
            byte[] bytesOfMessage = msg.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance(method);
            byte[] thedigest = md.digest(bytesOfMessage);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < thedigest.length; i++) {
                sb.append(Integer.toString((thedigest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            throw new MvditRuntimeException(ex);
        }
    }

    public static String encirptString(String msg) {
        return MvditUtils.encriptString("MD5", msg);
    }

    public static BigDecimal round(BigDecimal value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        //BigDecimal bd = new BigDecimal(value);
        value = value.setScale(places, RoundingMode.HALF_UP);
        return value;
    }

    public static BigDecimal parseStringToBigDecimal(String strVal) {
        strVal = strVal.replaceAll("[^0-9.,]", "");
        BigDecimal bd = new BigDecimal(strVal);
        return bd;
    }

    public static BigInteger parseStringToBigInteger(String strVal) {
        strVal = strVal.replaceAll("[^0-9]", "");
        BigInteger bi = new BigInteger(strVal);
        return bi;
    }
    
    /**
     * Validador de las entidades
     *
     * @return
     */
    public static Validator getValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator;
    }
    
    
}
