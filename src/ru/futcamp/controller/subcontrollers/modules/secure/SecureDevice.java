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

package ru.futcamp.controller.subcontrollers.modules.secure;

import ru.futcamp.controller.subcontrollers.modules.secure.mod.SecureModule;

/**
 * Security device
 */
public class SecureDevice extends SecureData implements ISecureDevice {
    private String name;
    private String alias;
    private String ip;
    private int channel;
    private String type;
    private String group;
    private String camera;

    public SecureDevice(String name, String alias, String ip, int chan, String type, String group, String cam) {
        this.name = name;
        this.alias = alias;
        this.ip = ip;
        this.channel = chan;
        this.type = type;
        this.group = group;
        this.camera = cam;
    }

    /**
     * Syncing alarm state with device
     * @param state State of alarm
     * @throws Exception If fail to sync state
     */
    public void syncSecureAlarm(boolean state) throws Exception {
        SecureModule.setSecureAlarm(ip, state);
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCamera() {
        return camera;
    }
}
