/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.core;

import java.io.File;
import java.io.StringWriter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.WriterAppender;

/**
 *
 * @author Pablo Ramírez
 */
public class MvditLogger {

    private Logger logger;
    private StringWriter stringWriter;

    /**
     * Singleton reference of MvditLogger.
     */
    private static MvditLogger instance;

    /**
     * Constructs singleton instance of MvditLogger.
     */
    private MvditLogger() {
        PropertiesRepository props = PropertiesRepository.getInstance();
        String configUrl = MvditApp.getInstance().getBasePath() + props.getProperty("mvdit.logConfigFilename");
        File configFile = new File(configUrl);
        if (configFile.exists()) {
            System.out.println("Initializing log4j with: " + configUrl);
            PropertyConfigurator.configure(configUrl);
        } else {
            System.err.println("*** " + configUrl + " file not found, so initializing log4j with BasicConfigurator");
            BasicConfigurator.configure();
        }

        this.logger = Logger.getLogger(props.getProperty("mvdit.logName"));
        if (MvditApp.getInstance().isDeveloperMode() && props.getProperty("mvdit.logToScreen").equalsIgnoreCase("true")) {
            try {
                Layout layout = (Layout) Class.forName(props.getProperty("mvdit.logToScreenLayoutClass")).newInstance();
                stringWriter = new StringWriter();
                WriterAppender writerAppender = new WriterAppender(layout, stringWriter);
                logger.addAppender(writerAppender);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {

            }

        }
    }

    /**
     * Provides reference to singleton object of MvditLogger.
     *
     * @return Singleton instance of MvditLogger.
     */
    public static final MvditLogger getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (MvditLogger.class) {
                if (instance == null) {
                    instance = new MvditLogger();
                }
            }
        }
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getScreenLog() {
        return stringWriter != null ? stringWriter.toString() : "";
    }

}
