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

package ru.futcamp.controller.subcontrollers.modules.monitor;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.subcontrollers.modules.monitor.db.IMonitorDB;
import ru.futcamp.controller.subcontrollers.modules.monitor.db.MonitorDB;
import ru.futcamp.net.notifier.INotifier;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Devices monitor
 */
public class Monitor implements IMonitor, IAppModule {
    private List<IMonitorDevice> devices = new LinkedList<>();
    private String modName;

    private ILogger log;
    private INotifier notify;
    private IConfigs cfg;

    public Monitor(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
        this.notify = (INotifier) dep[1];
        this.cfg = (IConfigs) dep[2];
    }

    /**
     * Save new status to database
     * @param device Light device
     * @throws Exception If fail to save status
     */
    private void saveStatus(IMonitorDevice device) throws Exception {
        RedisSettings set = cfg.getMonitorCfg().getDb();
        IMonitorDB db = null;

        try {
            db = new MonitorDB(set.getIp(), set.getTable());
            db.saveStatus(device.getName(), device.isStatus());
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Loading states from db
     * @throws Exception If fail to load states
     */
    public void loadStates() throws Exception {
        RedisSettings set = cfg.getMonitorCfg().getDb();
        IMonitorDB db = null;

        try {
            db = new MonitorDB(set.getIp(), set.getTable());
            for (IMonitorDevice device : devices) {
                device.setStatus(db.getStatus(device.getName()));
                log.info("Loaded status from DB for monitoring device \"" + device.getName() + "\" is \"" + device.isStatus() + "\"", "LIGHT");
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Check all devices status
     */
    public void checkStatus() {
        for (IMonitorDevice device : devices) {
            boolean status = device.getDeviceStatus(device.getModule());
            if (status != device.isStatus()) {
                try {
                    notify.sendNotify("МОНИТОР", "Устройство <b>" + device.getAlias() + "</b> " + (status ? "онлайн" : "не отвечает!"));
                } catch (Exception e) {
                    log.error("Fail to send notify message" + e.getMessage(), "MONITOR");
                }

                log.info("Device \"" + device.getName() + "\" has new status \"" + status + "\"", "MONITOR");

                try {
                    device.setStatus(status);
                    saveStatus(device);
                } catch (Exception e) {
                    log.error("Fail to save device \"" + device.getName() + "\" status to db: " + e.getMessage(), "MONITOR");
                }
            }
            device.setStatus(status);
        }
    }

    /**
     * Add new monitoring device
     * @param device Device
     */
    public void addDevice(IMonitorDevice device) {
        devices.add(device);
    }

    public String getModName() {
        return modName;
    }
}
