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

package ru.futcamp.controller.subcontrollers.modules.monitor;

import ru.futcamp.controller.subcontrollers.modules.light.mod.LightModule;
import ru.futcamp.controller.subcontrollers.modules.meteo.mod.MeteoModule;
import ru.futcamp.controller.subcontrollers.modules.secure.mod.SecureModule;
import ru.futcamp.controller.subcontrollers.modules.therm.mod.ThermModule;

public class MonitorDevice extends MonitorData implements IMonitorDevice {
    private String name;
    private String alias;
    private String ip;
    private String module;

    private static final int RETRIES = 4;

    public MonitorDevice(String name, String alias, String ip, String module) {
        this.name = name;
        this.alias = alias;
        this.ip = ip;
        this.module = module;
    }

    /**
     * Helper for check status function
     * @param module Module of device
     * @throws Exception If fail to check status
     */
    private void checkDeviceStatus(String module) throws Exception {
        switch (module) {
            case "meteo":
                MeteoModule.checkStatus(ip);
                break;

            case "therm":
                ThermModule.checkStatus(ip);
                break;

            case "secure":
                SecureModule.checkStatus(ip);
                break;

            case "light":
                LightModule.checkStatus(ip);
                break;
        }
    }

    /**
     * Get device status
     * @param module Module
     * @return Device status
     */
    public boolean getDeviceStatus(String module) {
        for (int i = 0; i < RETRIES; i++) {
            try {
                checkDeviceStatus(module);
                return true;
            } catch (Exception ignored) { }

            try {
                Thread.sleep(1000);
            } catch (Exception ignored) { }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getAlias() {
        return alias;
    }

    public String getModule() {
        return module;
    }
}
