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

package ru.futcamp.controller.modules.therm.mod;

import ru.futcamp.net.web.HttpClient;

/**
 * Therm control communication
 */
public class ThermModule {
    private String ip;

    public ThermModule(String ip) {
        this.ip = ip;
    }

    /**
     * Sync states with device
     * @param status Therm control status
     * @param heater Heater state
     * @throws Exception If fail to sync with device
     */
    public void syncStates(boolean status, boolean heater) throws Exception {
        HttpClient client = new HttpClient("http://" + ip + "/therm?status=" + status +
                                            "&heater=" + heater);
        client.getRequest(2000);
    }
}
