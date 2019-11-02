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

package ru.futcamp.controller.modules.therm;

import ru.futcamp.controller.modules.therm.mod.ThermModule;

/**
 * Therm control device
 */
public class ThermDevice extends ThermData implements IThermDevice {
    private String name;
    private String alias;
    private String ip;
    private String sensor;

    public ThermDevice(String name, String alias, String ip, String sensor) {
        this.name = name;
        this.ip = ip;
        this.alias = alias;
        this.sensor = sensor;
    }

    /**
     * Sync states with device
     * @throws Exception If fail to sync state
     */
    public void syncStates() throws Exception {
        ThermModule hdk = new ThermModule(this.ip);
        hdk.syncStates(isStatus(), isHeater());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
}
