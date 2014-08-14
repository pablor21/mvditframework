/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.core;

import org.apache.log4j.Logger;


/**
 *
 * @author Pablo Ramírez
 */
public class MvditApp {
    private MvditLogger logger;
    private boolean developerMode;
    private String basePath;

    /**
     * Singleton reference of App.
     */
    private static MvditApp instance;

    /**
     * Constructs singleton instance of App.
     */
    private MvditApp() {
        
    }
    
    public void init(String basePath){
        this.basePath= basePath;
        PropertiesRepository props = PropertiesRepository.getInstance();
        this.developerMode=props.getProperty("mvdit.appMode").equalsIgnoreCase("developer");
        logger= MvditLogger.getInstance();
        
    }

    /**
     * Provides reference to singleton object of App.
     *
     * @return Singleton instance of App.
     */
    public static final MvditApp getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (MvditApp.class) {
                if (instance == null) {
                    instance = new MvditApp();
                }
            }
        }
        return instance;
    }

    public Logger getLogger() {
        return logger.getLogger();
    }

    public boolean isDeveloperMode() {
        return developerMode;
    }

    public void setDeveloperMode(boolean developerMode) {
        this.developerMode = developerMode;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    
    public String getStringLog(){
        return this.logger.getScreenLog();
    }
   
    
}
