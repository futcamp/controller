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

package ru.futcamp.controller.modules.meteo.mod;

import com.alibaba.fastjson.JSON;
import ru.futcamp.net.web.HttpClient;

/**
 * Meteo Hardware Communication
 */
public class MeteoModule {
    private String ip;
    private int channel;
    private String type;
    private int id;
    private int value;

    public MeteoModule(String ip, String type, int chan) {
        this.ip = ip;
        this.channel = chan;
        this.type = type;
    }

    public MeteoModule(String ip, int id, int value, String type) {
        this.ip = ip;
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public MeteoModule(String ip) {
        this.ip = ip;
    }

    /**
     * Get meteo data from device
     * @return Meteo data
     * @throws Exception If fail to get data
     */
    public MeteoModuleData getMeteoData() throws Exception {
        MeteoModuleData data;

        HttpClient client = new HttpClient("http://" + ip + "/meteo?type=" + type + "&chan=" + channel);
        String response = client.getRequest(4000);
        data = JSON.parseObject(response, MeteoModuleData.class);

        return data;
    }

    /**
     * Update meteo data on lcd module
     * @throws Exception If fail to update data
     */
    public void updateMeteoLcd() throws Exception {
        HttpClient client = new HttpClient("http://" + ip + "/update?type=" + type + "&id=" + id + "&value=" + value);
        client.getRequest(4000);
    }

    /**
     * Show data on screen
     * @throws Exception If fail to show data
     */
    public void displayMeteoLcd() throws Exception {
        HttpClient client = new HttpClient("http://" + ip + "/display");
        client.getRequest(4000);
    }
}
