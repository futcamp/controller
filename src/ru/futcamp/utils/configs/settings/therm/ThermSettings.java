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

package ru.futcamp.utils.configs.settings.therm;

import ru.futcamp.utils.configs.settings.RedisSettings;

public class ThermSettings {
    private int interval;
    private RedisSettings db;
    private ThermDeviceSettings[] devices;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public ThermDeviceSettings[] getDevices() {
        return devices;
    }

    public void setDevices(ThermDeviceSettings[] devices) {
        this.devices = devices;
    }

    public RedisSettings getDb() {
        return db;
    }

    public void setDb(RedisSettings db) {
        this.db = db;
    }
}
