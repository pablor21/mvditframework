/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.database;

import com.mvdit.framework.core.MvditApp;
import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.core.PropertiesRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author pramirez
 */
public class DbBackup {

    public static final String PROPS_FILE = "mvdit_db_backup";

    public static String backupDatabase(String dbId) {
        dbId=(!MvditUtils.stringEmpty(dbId))?dbId:"default";
        PropertiesRepository props = PropertiesRepository.getInstance();
        String execPath = props.getProperty("execPath", PROPS_FILE);
        String outputPath = props.getProperty("outputPath", PROPS_FILE);
        String host = props.getProperty(dbId + ".host", PROPS_FILE);
        String user = props.getProperty(dbId + ".user", PROPS_FILE);
        String password = props.getProperty(dbId + ".password", PROPS_FILE);
        String port = props.getProperty(dbId + ".port", PROPS_FILE);
        String database = props.getProperty(dbId + ".database", PROPS_FILE);
        host = (!MvditUtils.stringEmpty(host)) ? host : "localhost:3306";
        user = (!MvditUtils.stringEmpty(user)) ? user : "root";
        port = (!MvditUtils.stringEmpty(port)) ? port : "3306";
        Calendar calendar = Calendar.getInstance();
        String filename = "db_backup_" + new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
        Map<String, String> opts = new HashMap<>();
        opts.put("user", user);
        opts.put("password", password);
        opts.put("port", port);
        opts.put("database", database);
        opts.put("host", host);
        opts.put("filename", filename);

        return doBackup(outputPath, execPath, opts).replace(outputPath, "");
    }
    
    public static File getBackup(String fileName){
        PropertiesRepository props = PropertiesRepository.getInstance();
        String basePath = props.getProperty("outputPath", PROPS_FILE);
        File f=new File(basePath +"/" + fileName);
        if(f.exists()){
            return f;
        }else{
            throw new MvditRuntimeException("El archivo " + fileName + " no existe, es posible que se haya eliminado.");
        }
    }

    private static String doBackup(String outputPath, String execPath, Map<String, String> opts){

        String filename = outputPath + "/" + opts.get("filename");
        List<String> args = new ArrayList<>();
        args.add(execPath);

        args.add(" -u");
        args.add(opts.get("user"));
        args.add(" -h");
        args.add(opts.get("host"));
        args.add(" --port=");
        args.add(opts.get("port"));
        args.add(" -p");
        args.add(opts.get("password"));
        args.add(" --add-drop-database");
        args.add(" --default-character-set=utf8");
        args.add(" --databases ");
        args.add(opts.get("database"));

        args.add(" -r");
        args.add("\"" + filename + ".sql\"");

        String execStr = "";
        for (String str : args) {
            execStr += str;
        }
        
        

        try {
            ProcessBuilder pb =null;
            if (MvditUtils.isWindows()) {
                pb = new ProcessBuilder(new String[]{"cmd.exe", "/c", execStr});
            } else {
                pb = new ProcessBuilder(new String[]{"cmd.exe", "/c", execStr});
            }
            MvditApp.getInstance().getLogger().info("Ejecutando backup: " + execStr);
            //pb.redirectError();
            Process p = pb.start();

            InputStream is = p.getInputStream();

            int in = -1;
            String out="";
            while ((in = is.read()) != -1) {
                out+=((char) in);
            }
            if(!MvditUtils.stringEmpty(out)){
                MvditApp.getInstance().getLogger().error("Salida de mysqldump: " + out);
            }
            int proccessCompleted = p.waitFor();

            if (proccessCompleted == 0) {
                MvditApp.getInstance().getLogger().info("Incializando backup con el nombre " + filename + ".zip");

                byte[] buffer = new byte[1024];
                FileOutputStream fos = new FileOutputStream(filename + ".zip");
                File file;
                try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                    ZipEntry ze = new ZipEntry("db_backup.sql");
                    zos.putNextEntry(ze);
                    file = new File(filename + ".sql");
                    FileInputStream ins = new FileInputStream(file);
                    int len;
                    while ((len = ins.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    ins.close();
                    zos.closeEntry();
                }

                file.delete();

                MvditApp.getInstance().getLogger().info("Se ha creado el backup con el nombre " + filename + ".zip");
                return filename + ".zip";
            } else {
                throw new MvditRuntimeException("Se ha producido un error desconocido al crear el backup de la base de datos!");
            }
        } catch (Exception ex) {
            MvditApp.getInstance().getLogger().error("Error: " + ex.getMessage() + "|" + ex.getLocalizedMessage());
            throw new MvditRuntimeException(ex);
        }

    }

}
