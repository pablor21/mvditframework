/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Pablo Ramírez
 */
public class PropertiesRepository {

    public static final String DEFAULT_FILE_NAME = "mvdit";
    private Map<String, Properties> resources;

    /**
     * Singleton reference of PropertiesRepository.
     */
    private static PropertiesRepository instance;

    /**
     * Constructs singleton instance of PropertiesRepository.
     */
    private PropertiesRepository() {
        resources = new ConcurrentHashMap<>();
    }

    /**
     * Provides reference to singleton object of PropertiesRepository.
     *
     * @return Singleton instance of PropertiesRepository.
     */
    public static final PropertiesRepository getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (PropertiesRepository.class) {
                if (instance == null) {
                    instance = new PropertiesRepository();
                }
            }
        }
        return instance;
    }

    public String getProperty(String key, String source) {
        if (!this.resources.containsKey(source)) {
            this.loadResource(source);
        }
        String ret = "";
        if (this.resources.containsKey(source)) {
            Properties p = this.resources.get(source);
            if (p.containsKey(key)) {
                ret = p.getProperty(key);
            }
        }
        return ret;
    }

    public String getProperty(String key) {
        return this.getProperty(key, PropertiesRepository.DEFAULT_FILE_NAME);
    }

    /**
     * 
     * @param name 
     */
    public void loadResource(String name) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/" + name + ".properties");
        if (inputStream != null) {

            try {
                Properties prop= new Properties();
                prop.load(inputStream);
                this.resources.put(name, prop);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }else{
            System.out.println("No se ha encontrado el archivo " +"/" + name + ".properties" );
        }
    }
}
