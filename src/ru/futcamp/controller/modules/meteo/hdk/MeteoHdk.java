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

package ru.futcamp.controller.modules.meteo.hdk;

import com.alibaba.fastjson.JSON;
import ru.futcamp.net.web.HttpClient;

/**
 * Meteo Hardware Communication
 */
public class MeteoHdk {
    private String ip;
    private int channel;
    private String type;

    public MeteoHdk(String ip, String type, int chan) {
        this.ip = ip;
        this.channel = chan;
        this.type = type;
    }

    /**
     * Get meteo data from device
     * @return Meteo data
     * @throws Exception If fail to get data
     */
    public HdkMeteoData getMeteoData() throws Exception {
        HdkMeteoData data;

        HttpClient client = new HttpClient("http://" + ip + "/meteo?type=" + type + "&chan=" + channel);
        String response = client.getRequest(2000);
        data = JSON.parseObject(response, HdkMeteoData.class);

        return data;
    }
}
