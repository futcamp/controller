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

package ru.futcamp.controller.modules.light;

import ru.futcamp.controller.modules.light.mod.LightModule;

/**
 * Light control device
 */
public class LightDevice extends LightData implements ILightDevice {
    private String name;
    private String alias;
    private String group;
    private String ip;
    private int channel;

    public LightDevice(String name, String alias, String group, String ip, int chan) {
        this.name = name;
        this.alias = alias;
        this.ip = ip;
        this.channel = chan;
        this.group = group;
    }

    /**
     * Syncing states with light device
     * @throws Exception If fail to sync states
     */
    public void syncStates() throws Exception {
        LightModule hdk = new LightModule(ip, channel);

        hdk.syncStates(isStatus());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
}
