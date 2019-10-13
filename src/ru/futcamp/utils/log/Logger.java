///////////////////////////////////////////////////////////////////////
//
// Future Camp Project
//
// Copyright(C) 2019 Sergey Denisov.
//
// Written by Sergey Denisov aka LittleBuster(DenisovS21@gmail.com)
// Github:  https://github.com/LittleBuster
//          https://github.com/futcamp
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public Licence 3
// as published by the Free Software Foundation; either version 3
// of the Licence, or(at your option) any later version.
//
///////////////////////////////////////////////////////////////////////

package ru.futcamp.utils.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Application logger
 */
public class Logger implements ILogger {
    private String path;

    /**
     * Save log message to file
     * @param message Log message
     * @param fileName Name of log file
     * @throws IOException Saving exception
     */
    private synchronized void saveToFile(String message, String fileName) throws IOException {
        Date date = new Date();
        SimpleDateFormat dtm = new SimpleDateFormat("20YYMMdd");

        File file = new File(fileName + dtm.format(date) + ".log");
        FileWriter writer = new FileWriter(file, true);

        writer.append(message);
        writer.append("\n");

        writer.close();
    }

    /**
     * Compile log message for saving to file
     * @param message Log message
     * @param module Application module name
     * @param type Log message type
     */
    private void makeMessage(String message, String module, LogType type) {
        Date date = new Date();
        SimpleDateFormat dtm = new SimpleDateFormat("[HH:mm]");

        String out = dtm.format(date) + "[";

        switch (type) {
            case LOG_INFO:
                out += "INFO";
                break;
            case LOG_WARN:
                out += "WARN";
                break;
            case LOG_ERROR:
                out += "ERROR";
                break;
        }

        out += "][" + module + "] " + message;
        System.out.println(out);

        try {
            saveToFile(out, path);
        }
        catch (IOException e) {
            System.out.println("Fail to write to log file: " + e.getMessage());
        }
    }

    /**
     * Get logs files list
     * @return Files list
     */
    public List<String> getLogsList() {
        List<String> files = new LinkedList<>();

        File dir = new File(this.path);
        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);

        for (File file : lst) {
            String[] parts = file.getName().split(Pattern.quote("."));
            if (parts.length > 1 && parts[1].equals("log"))
                files.add(file.getName());
        }

        return files;
    }

    /**
     * Set logfile path
     * @param path Path to log file
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Info logger message
     * @param message Log message
     * @param module Application module
     */
    public void info(String message, String module) {
        makeMessage(message, module, LogType.LOG_INFO);
    }

    /**
     * Warning logger message
     * @param message Log message
     * @param module Application module
     */
    public void warning(String message, String module) {
        makeMessage(message, module, LogType.LOG_WARN);
    }

    /**
     * Error logger message
     * @param message Log message
     * @param module Application module
     */
    public void error(String message, String module) {
        makeMessage(message, module, LogType.LOG_ERROR);
    }
}
