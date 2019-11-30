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

package ru.futcamp.utils.configs.settings.monitor;

import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.configs.settings.secure.SecureDeviceSettings;

public class MonitorSettings {
    private int interval;
    private RedisSettings db;
    private MonitorDeviceSettings[] devices;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public RedisSettings getDb() {
        return db;
    }

    public void setDb(RedisSettings db) {
        this.db = db;
    }

    public MonitorDeviceSettings[] getDevices() {
        return devices;
    }

    public void setDevices(MonitorDeviceSettings[] devices) {
        this.devices = devices;
    }
}
