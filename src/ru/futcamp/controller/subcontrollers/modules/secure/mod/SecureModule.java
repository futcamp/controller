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

package ru.futcamp.controller.subcontrollers.modules.secure.mod;

import ru.futcamp.net.web.HttpClient;

/**
 * Security communication
 */
public class SecureModule {
    private static final int TIMEOUT = 4000;

    /**
     * Send secure alarm update state to device
     * @param ip Address of device
     * @param state Alarm state
     * @throws Exception If fail to send request
     */
    public static void setSecureAlarm(String ip, boolean state) throws Exception {
        synchronized (SecureModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/alarm?state=" + state);
            client.getRequest(TIMEOUT);
        }
    }

    /**
     * Sync states with device
     * @param ip Address of device
     * @param radio Radio state
     * @param lamp Lamp state
     * @throws Exception If fail to send states
     */
    public static void setMIHStates(String ip, boolean radio, boolean lamp) throws Exception {
        synchronized (SecureModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/mih?radio=" + radio + "&lamp=" + lamp);
            client.getRequest(TIMEOUT);
        }
    }

    /**
     * Check device status
     * @param ip Address of device
     * @throws Exception If fail to get status
     */
    public static void checkStatus(String ip) throws Exception {
        synchronized (SecureModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/");
            client.getRequest(TIMEOUT);
        }
    }
}
