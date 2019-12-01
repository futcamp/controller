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

package ru.futcamp.controller.subcontrollers.modules.light.mod;

import ru.futcamp.net.web.HttpClient;

/**
 * Light communication
 */
public class LightModule {
    private static final int TIMEOUT = 4000;

    /**
     * Sync light states with module device
     * @param ip Address of device
     * @param chan Channel of device
     * @param status New light status
     * @throws Exception If fail to sync states
     */
    public static void syncStates(String ip, int chan, boolean status) throws Exception {
        synchronized (LightModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/light?state=" + status + "&chan=" + chan);
            client.getRequest(TIMEOUT);
        }
    }

    /**
     * Check device status
     * @param ip Address of device
     * @throws Exception If fail to get status
     */
    public static void checkStatus(String ip) throws Exception {
        synchronized (LightModule.class) {
            HttpClient client = new HttpClient("http://" + ip + "/");
            client.getRequest(TIMEOUT);
        }
    }
}
