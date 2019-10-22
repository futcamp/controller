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

package ru.futcamp.utils.configs.settings;

public class MeteoSettings {
    private int interval;
    private int dbInterval;
    private String db;
    private MeteoDeviceSettings[] devices;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

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

    public int getDbInterval() {
        return dbInterval;
    }

    public void setDbInterval(int dbInterval) {
        this.dbInterval = dbInterval;
    }
}
