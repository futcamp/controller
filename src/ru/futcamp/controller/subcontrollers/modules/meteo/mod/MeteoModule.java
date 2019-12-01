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

package ru.futcamp.controller.subcontrollers.modules.meteo.mod;

import com.alibaba.fastjson.JSON;
import ru.futcamp.net.web.HttpClient;

/**
 * Meteo Hardware Communication
 */
public class MeteoModule {
    private static final int TIMEOUT = 4000;

    /**
     * Get meteo data from sensor device
     * @param ip Address of device
     * @param channel Channel of device
     * @param type Type of sensor
     * @return Meteo data
     * @throws Exception If fail to get data
     */
    public static MeteoModuleData getMeteoData(String ip, String type, int channel) throws Exception {
        MeteoModuleData data;

        synchronized (MeteoModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/meteo?type=" + type + "&chan=" + channel);
            String response = client.getRequest(TIMEOUT);
            data = JSON.parseObject(response, MeteoModuleData.class);
        }

        return data;
    }

    /**
     * Update meteo data in LCD buffer
     * @param ip Address of device
     * @param type Type of device
     * @param id Number of sensor on screen
     * @param value Meteo value
     * @throws Exception If fail to update data
     */
    public static void updateMeteoLcd(String ip, int id, int value, String type) throws Exception {
        synchronized (MeteoModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/update?type=" + type + "&id=" + id + "&value=" + value);
            client.getRequest(TIMEOUT);
        }
    }

    /**
     * Show data on screen
     * @param ip Address of device
     * @throws Exception If fail to show data
     */
    public static void displayMeteoLcd(String ip) throws Exception {
        synchronized (MeteoModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/display");
            client.getRequest(TIMEOUT);
        }
    }

    /**
     * Check device status
     * @param ip Address of device
     * @throws Exception If fail to get status
     */
    public static void checkStatus(String ip) throws Exception {
        synchronized (MeteoModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/");
            client.getRequest(TIMEOUT);
        }
    }
}
