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

package ru.futcamp.controller.modules.vision;

import ru.futcamp.controller.modules.vision.mod.CamModule;

/**
 * Web camera device
 */
public class CamDevice implements ICamDevice {
    private String name;
    private String alias;
    private String ip;
    private int channel;
    private boolean warm;
    private String[] lamps;

    public CamDevice(String name, String alias, String ip, int chan, boolean warm, String[] lamps) {
        this.name = name;
        this.alias = alias;
        this.ip = ip;
        this.channel = chan;
        this.warm = warm;
        this.lamps = lamps;
    }

    /**
     * Save photo from camera
     * @param fileName Path to photo
     * @throws Exception If fail to save photo
     */
    public synchronized void savePhoto(String fileName) throws Exception {
        CamModule module = new CamModule(ip, channel);
        module.savePhoto(fileName);
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

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String[] getLamps() {
        return lamps;
    }

    public void setLamps(String[] lamps) {
        this.lamps = lamps;
    }

    public boolean isWarm() {
        return warm;
    }

    public void setWarm(boolean warm) {
        this.warm = warm;
    }
}
