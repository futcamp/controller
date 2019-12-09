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

package ru.futcamp.controller.subcontrollers;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.subcontrollers.modules.monitor.IMonitor;
import ru.futcamp.controller.subcontrollers.modules.monitor.IMonitorDevice;
import ru.futcamp.controller.subcontrollers.modules.monitor.MonitorDevice;
import ru.futcamp.controller.subcontrollers.modules.socket.IPowerSocket;
import ru.futcamp.controller.subcontrollers.modules.socket.ISocketDevice;
import ru.futcamp.controller.subcontrollers.modules.socket.SocketDevice;
import ru.futcamp.controller.subcontrollers.modules.socket.SocketInfo;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.monitor.MonitorDeviceSettings;
import ru.futcamp.utils.configs.settings.monitor.MonitorSettings;
import ru.futcamp.utils.configs.settings.socket.SocketDeviceSettings;
import ru.futcamp.utils.configs.settings.socket.SocketSettings;
import ru.futcamp.utils.log.ILogger;

import java.util.Timer;
import java.util.TimerTask;

public class MiscController implements IMiscController, IAppModule {
    private ILogger log;
    private IConfigs cfg;
    private IMonitor monitor;
    private Runnable monitorTask;
    private IPowerSocket socket;

    private String modName;

    public MiscController(String name, IAppModule ...dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.monitor = (IMonitor) dep[2];
        this.monitorTask = (Runnable) dep[3];
        this.socket = (IPowerSocket) dep[4];
    }

    public boolean start() {
        if (cfg.getModCfg("monitor")) {
            if (!startMonitorModule())
                return false;
        }
        if (cfg.getModCfg("socket")) {
            if (!startSocketModule())
                return false;
        }
        return true;
    }

    private boolean startMonitorModule() {
        MonitorSettings monCfg = cfg.getMonitorCfg();

        /*
         * Add devices from cfg
         */
        for (MonitorDeviceSettings dev : monCfg.getDevices()) {
            IMonitorDevice device = new MonitorDevice(dev.getName(), dev.getAlias(), dev.getIp(), dev.getModule());
            monitor.addDevice(device);
            log.info("Add new monitor device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\"", "MISCCTRL");
        }

        /*
         * Loading states from DB
         */
        try {
            monitor.loadStates();
        } catch (Exception e) {
            log.error("Fail to load monitor states from db: " + e.getMessage(), "MISCCTRL");
        }

        /*
         * Run task
         */
        Timer monTmr = new Timer(true);
        monTmr.scheduleAtFixedRate((TimerTask) monitorTask, 0, 1000);
        return true;
    }

    private boolean startSocketModule() {
        SocketSettings sockCfg = cfg.getSockCfg();

        /*
         * Add devices from cfg
         */
        for (SocketDeviceSettings dev : sockCfg.getDevices()) {
            ISocketDevice device = new SocketDevice(dev.getAlias(), dev.getIp(), dev.getChannel(), dev.getModule());
            socket.addDevice(device);
            log.info("Add new socket device alias \"" + dev.getAlias() + "\" ip \"" + dev.getIp() + "\" chan \"" +
                    dev.getChannel() + "\"", "MISCCTRL");
        }
        return true;
    }

    public SocketInfo getSocketInfo(String ip, int channel) throws Exception { return socket.getSocketInfo(ip, channel); }

    public void genSocketEvent(String ip, int channel, Events event) { socket.genEvent(ip, channel, event); }

    @Override
    public String getModName() {
        return this.modName;
    }
}
