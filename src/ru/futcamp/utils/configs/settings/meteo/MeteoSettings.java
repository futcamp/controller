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

package ru.futcamp.utils.configs.settings.meteo;

public class MeteoSettings {
    private MeteoTimersSettings timers;
    private String db;
    private MeteoDeviceSettings[] devices;
    private LcdSettings[] displays;

    public MeteoDeviceSettings[] getDevices() {
        return devices;
    }

    public void setDevices(MeteoDeviceSettings[] devices) {
        this.devices = devices;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public LcdSettings[] getDisplays() {
        return displays;
    }

    public void setDisplays(LcdSettings[] displays) {
        this.displays = displays;
    }

    public MeteoTimersSettings getTimers() {
        return timers;
    }

    public void setTimers(MeteoTimersSettings timers) {
        this.timers = timers;
    }
}
