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

package ru.futcamp.controller.subcontrollers.modules.socket.mod;

import ru.futcamp.net.web.HttpClient;

/**
 * Therm control communication
 */
public class SocketModule {
    private static final int TIMEOUT = 4000;

    /**
     * Sync states with device
     * @param ip Address of device
     * @param channel Device channel
     * @param status Therm control status
     * @throws Exception If fail to sync with device
     */
    public static void syncState(String ip, int channel, boolean device, boolean status) throws Exception {
        synchronized (SocketModule.class) {
            System.out.println("OK1");
            HttpClient client = new HttpClient("http://" + ip + "/socket?device=" + device + "&chan=" + channel + "&status=" + status);
            client.getRequest(TIMEOUT);
            System.out.println("EXIT1");
        }
    }

    /**
     * Check device status
     * @param ip Address of device
     * @throws Exception If fail to get status
     */
    public static void checkStatus(String ip) throws Exception {
        synchronized (SocketModule.class) {
            System.out.println("OK2");
            HttpClient client = new HttpClient("http://" + ip + "/");
            client.getRequest(TIMEOUT);
            System.out.println("EXIT2");
        }
    }
}
