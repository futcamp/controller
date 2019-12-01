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

package ru.futcamp.controller.subcontrollers.modules.therm;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.events.EventListener;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.subcontrollers.modules.meteo.IMeteoStation;
import ru.futcamp.controller.subcontrollers.modules.therm.db.IThermDB;
import ru.futcamp.controller.subcontrollers.modules.therm.db.ThermDB;
import ru.futcamp.controller.subcontrollers.modules.therm.db.ThermDBData;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Therm control class
 */
public class ThermControl implements IThermControl, EventListener, IAppModule {
    private ILogger log;
    private IMeteoStation meteo;
    private IConfigs cfg;

    private Map<String, IThermDevice> devices = new HashMap<>();

    private String modName;

    public ThermControl(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
        this.meteo = (IMeteoStation) dep[1];
        this.cfg = (IConfigs) dep[2];
    }

    /**
     * Save device state to ru.futcamp.db
     */
    private void saveStates(IThermDevice device) throws Exception {
        RedisSettings set = cfg.getThermCfg().getDb();
        IThermDB db = null;

        try {
            db = new ThermDB(set.getIp(), set.getTable());
            ThermDBData data = new ThermDBData(device.isStatus(), device.getThreshold());
            db.saveData(device.getName(), data);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Add new device
     * @param device Therm device
     */
    public void addDevice(IThermDevice device) {
        devices.put(device.getAlias(), device);
    }

    /**
     * Load states from ru.futcamp.db
     * @throws Exception If fail to load states
     */
    public void loadStates() throws Exception {
        RedisSettings set = cfg.getThermCfg().getDb();
        IThermDB db = null;

        try {
            db = new ThermDB(set.getIp(), set.getTable());
            for(Map.Entry<String, IThermDevice> entry : devices.entrySet()) {
                IThermDevice device = entry.getValue();
                ThermDBData data = db.loadData(device.getName());

                device.setStatus(data.isStatus());
                device.setThreshold(data.getThreshold());

                log.info("Loaded therm status from DB for device \"" + device.getName() + "\" is \"" + device.isStatus() + "\"", "THERM");
                log.info("Loaded therm threshold from DB for device \"" + device.getName() + "\" is \"" + device.getThreshold() + "\"", "THERM");
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
     * Change current threshold value
     * @param alias Alias of device
     * @param action Action of threshold
     * @throws Exception If fail to set new value
     */
    public void changeThreshold(String alias, ActMgmt action) throws Exception {
        IThermDevice device = devices.get(alias);
        switch (action) {
            case SET_MGMT_THRESOLD_MINUS:
                device.setThreshold(device.getThreshold() - 1);
                break;

            case SET_MGMT_THRESOLD_PLUS:
                device.setThreshold(device.getThreshold() + 1);
                break;
        }
        saveStates(device);
    }

    /**
     * Switch current status for device
     * @param alias Alias of device
     */
    public void switchStatus(String alias) throws Exception {
        IThermDevice device = devices.get(alias);

        boolean status = device.isStatus();
        device.setStatus(!status);
        saveStates(device);

        if (device.isStatus()) {
            log.info("Set therm status \"" + device.getName() + "\" to \"true\"", "THERM");
        } else {
            device.setHeater(false);
            log.info("Set therm status \"" + device.getName() + "\" to \"false\"", "THERM");
        }

        /*
         * Syncing states with device
         */
        try {
            device.syncStates();
        } catch (Exception e) {
            log.error("Fail to sync therm device " + device.getName(), "THERM");
        }
    }

    /**
     * Get device by name
     * @param alias Alias of device
     * @return Found device
     */
    public ThermInfo getThermInfo(String alias) throws Exception {
        ThermInfo info = new ThermInfo();

        IThermDevice device = devices.get(alias);
        if (device == null) {
            throw new Exception("Therm device \""+alias+"\" not found");
        }

        info.setStatus(device.isStatus());
        info.setHeater(device.isHeater());
        info.setName(device.getName());
        info.setThreshold(device.getThreshold());
        info.setSensor(device.getSensor());

        return info;
    }

    /**
     * Get all devices info
     * @return Found info
     */
    public List<ThermInfo> getThermInfo() {
        List<ThermInfo> infoList = new LinkedList<>();

        for(Map.Entry<String, IThermDevice> entry : devices.entrySet()) {
            ThermInfo info = new ThermInfo();

            IThermDevice device = entry.getValue();
            info.setStatus(device.isStatus());
            info.setHeater(device.isHeater());
            info.setName(device.getName());
            info.setThreshold(device.getThreshold());
            info.setAlias(device.getAlias());
            info.setSensor(device.getSensor());

            infoList.add(info);
        }

        return infoList;
    }

    /**
     * Update states
     */
    public void getUpdate() {
        int curTemp = 0;

        for(Map.Entry<String, IThermDevice> entry : devices.entrySet()) {
            IThermDevice device = entry.getValue();

            if (device.isStatus()) {
                try {
                    curTemp = meteo.getMeteoInfo(device.getSensor()).getTemp();
                } catch (Exception e) {
                    log.error("Fail to get meteo info: " + e.getMessage(), "THERM");
                    continue;
                }

                if (curTemp < device.getThreshold()) {
                    if (!device.isHeater()) {
                        log.info("Set heater status \"" + device.getName() + "\" to \"true\"", "THERM");
                        device.setHeater(true);
                        /*
                         * Syncing states with device
                         */
                        try {
                            device.syncStates();
                        } catch (Exception e) {
                            log.error("Fail to sync therm device " + device.getName(), "THERM");
                        }
                    }
                }
                if (curTemp > device.getThreshold()) {
                    if (device.isHeater()) {
                        log.info("Set heater status \"" + device.getName() + "\" to \"false\"", "THERM");
                        device.setHeater(false);
                        /*
                         * Syncing states with device
                         */
                        try {
                            device.syncStates();
                        } catch (Exception e) {
                            log.error("Fail to sync therm device " + device.getName(), "THERM");
                        }
                    }
                }
            }
        }
    }

    public String getModName() {
        return modName;
    }

    @Override
    public void getEvent(Events event, String module, String ip, int channel) {
        if (module.equals(modName)) {
            switch (event) {
                case SYNC_EVENT:
                    for (Map.Entry<String, IThermDevice> entry : devices.entrySet()) {
                        IThermDevice device = entry.getValue();
                        if (device.getIp().equals(ip)) {
                            try {
                                device.syncStates();
                            } catch (Exception e) {
                                log.error("Fail to first start sync of therm device \"" + device.getName() + "\"", "THERM");
                            }
                            return;
                        }
                    }
                    break;

                case SWITCH_STATUS_EVENT:
                    for (Map.Entry<String, IThermDevice> entry : devices.entrySet()) {
                        IThermDevice device = entry.getValue();
                        if (device.getIp().equals(ip)) {
                            try {
                                switchStatus(device.getAlias());
                            } catch (Exception e) {
                                log.error("Fail to switch status of therm device \"" + device.getName() + "\"", "THERM");
                            }
                            return;
                        }
                    }
                    break;
            }
        }
    }
}
