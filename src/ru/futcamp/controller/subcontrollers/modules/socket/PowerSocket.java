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

import ru.futcamp.IAppModule;
import ru.futcamp.controller.subcontrollers.Events;
import ru.futcamp.utils.log.ILogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Humidity control class
 */
public class PowerSocket implements IPowerSocket, IAppModule {
    private ILogger log;

    private Map<String, ISocketDevice> devices = new HashMap<>();
    private String modName;

    public PowerSocket(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
    }

    /**
     * Add new device
     * @param device Humidity control device
     */
    public void addDevice(ISocketDevice device) {
        devices.put(device.getAlias(), device);
    }

    /**
     * Switch current status for device
     * @param alias Alias of device
     */
    public void setStatus(String alias, boolean dev, boolean status) {
        ISocketDevice device = devices.get(alias);

        device.setStatus(status);
        device.setDevice(dev);

        if (device.isStatus()) {
            log.info("Set socket status \"" + device.getAlias() + "\" to \"true\"", "SOCKET");
        } else {
            log.info("Set socket status \"" + device.getAlias() + "\" to \"false\"", "SOCKET");
        }

        /*
         * Syncing states with device
         */
        try {
            device.syncStates();
        } catch (Exception e) {
            log.error("Fail to sync socket device " + device.getAlias(), "SOCKET");
        }
    }

    /**
     * Get device by name
     * @param alias Alias of device
     * @return Found device
     */
    public SocketInfo getSocketInfo(String alias) throws Exception {
        SocketInfo info = new SocketInfo();

        ISocketDevice device = devices.get(alias);
        if (device == null) {
            throw new Exception("Socket device \""+alias+"\" not found");
        }

        info.setStatus(device.isStatus());

        return info;
    }

    /**
     * Get device by ip & channel
     * @return Found device
     */
    public SocketInfo getSocketInfo(String ip, int channel) throws Exception {
        for(Map.Entry<String, ISocketDevice> entry : devices.entrySet()) {
            ISocketDevice device = entry.getValue();
            if (device.getIp().equals(ip) && device.getChannel() == channel) {
                SocketInfo info = new SocketInfo();
                info.setStatus(device.isStatus());
                info.setAlias(device.getAlias());
                info.setModule(device.getModule());
                return info;
            }
        }

        throw new Exception("Socket device \""+ ip + ":" + channel +"\" not found");
    }

    /**
     * Get all devices info
     * @return Found info
     */
    public List<SocketInfo> getSocketInfo() {
        List<SocketInfo> infoList = new LinkedList<>();

        for(Map.Entry<String, ISocketDevice> entry : devices.entrySet()) {
            SocketInfo info = new SocketInfo();

            ISocketDevice device = entry.getValue();
            info.setStatus(device.isStatus());
            info.setAlias(device.getAlias());

            infoList.add(info);
        }

        return infoList;
    }

    public String getModName() {
        return modName;
    }

    public void genEvent(String ip, int channel, Events event) {
        switch (event) {
            case SYNC_EVENT:
                for (Map.Entry<String, ISocketDevice> entry : devices.entrySet()) {
                    ISocketDevice device = entry.getValue();
                    if (device.getIp().equals(ip)) {
                        try {
                            device.syncStates();
                        } catch (Exception e) {
                            log.error("Fail to first start sync of socket device \"" + device.getAlias() + "\"", "SOCKET");
                        }
                    }
                }
                break;
        }
    }
}
