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

package ru.futcamp.controller.subcontrollers.modules.socket;

import ru.futcamp.controller.subcontrollers.modules.socket.mod.SocketModule;

/**
 * Therm control device
 */
public class SocketDevice extends SocketData implements ISocketDevice {
    private String alias;
    private String ip;
    private int channel;
    private String module;

    public SocketDevice(String alias, String ip, int chan, String mod) {
        this.ip = ip;
        this.alias = alias;
        this.channel = chan;
        this.module = mod;
    }

    /**
     * Sync states with device
     * @throws Exception If fail to sync state
     */
    public void syncStates() throws Exception {
        SocketModule.syncState(ip, channel, isDevice(), isStatus());
    }

    public String getAlias() {
        return alias;
    }

    public String getIp() {
        return ip;
    }

    public int getChannel() {
        return this.channel;
    }

    public String getModule() {
        return module;
    }
}
