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

public class SecureSettings {
    private int interval;
    private String db;
    private String mih;
    private String[] lamps;
    private SecureDeviceSettings[] devices;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public SecureDeviceSettings[] getDevices() {
        return devices;
    }

    public void setDevices(SecureDeviceSettings[] devices) {
        this.devices = devices;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getMih() {
        return mih;
    }

    public void setMih(String mih) {
        this.mih = mih;
    }

    public String[] getLamps() {
        return lamps;
    }

    public void setLamps(String[] lamps) {
        this.lamps = lamps;
    }
}
