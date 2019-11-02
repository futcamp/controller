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

package ru.futcamp.controller.modules.secure.mod;

import ru.futcamp.net.web.HttpClient;

/**
 * Security communication
 */
public class SecureModule {
    private String ip;

    public SecureModule(String ip) {
        this.ip = ip;
    }

    /**
     * Send secure alarm update state to device
     * @param state Alarm state
     * @throws Exception If fail to send request
     */
    public void setSecureAlarm(boolean state) throws Exception {
        HttpClient client = new HttpClient("http://" + ip + "/alarm?state=" + state);
        client.getRequest(2000);
    }

    /**
     * Sync states with device
     * @param radio Radio state
     * @param lamp Lamp state
     * @throws Exception If fail to send states
     */
    public void setMIHStates(boolean radio, boolean lamp) throws Exception {
        HttpClient client = new HttpClient("http://" + ip + "/mih?radio=" + radio + "&lamp=" + lamp);
        client.getRequest(2000);
    }
}
