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

package ru.futcamp.controller.subcontrollers.modules.hum;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.subcontrollers.Events;
import ru.futcamp.controller.subcontrollers.modules.hum.db.IHumDB;
import ru.futcamp.controller.subcontrollers.modules.hum.db.HumDB;
import ru.futcamp.controller.subcontrollers.modules.hum.db.HumDBData;
import ru.futcamp.controller.subcontrollers.modules.meteo.IMeteoStation;
import ru.futcamp.controller.subcontrollers.modules.socket.IPowerSocket;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Humidity control class
 */
public class HumControl implements IHumControl, IAppModule {
    private ILogger log;
    private IMeteoStation meteo;
    private IConfigs cfg;
    private IPowerSocket socket;

    private Map<String, IHumDevice> devices = new HashMap<>();
    private String modName;

    public HumControl(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
        this.meteo = (IMeteoStation) dep[1];
        this.cfg = (IConfigs) dep[2];
        this.socket = (IPowerSocket) dep[3];
    }

    /**
     * Save device state to db
     */
    private void saveStates(IHumDevice device) throws Exception {
        RedisSettings set = cfg.getHumCfg().getDb();
        IHumDB db = null;

        try {
            db = new HumDB(set.getIp(), set.getTable());
            HumDBData data = new HumDBData(device.isStatus(), device.getThreshold());
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
     * @param device Humidity control device
     */
    public void addDevice(IHumDevice device) {
        devices.put(device.getAlias(), device);
    }

    /**
     * Load states from ru.futcamp.db
     * @throws Exception If fail to load states
     */
    public void loadStates() throws Exception {
        RedisSettings set = cfg.getHumCfg().getDb();
        IHumDB db = null;

        try {
            db = new HumDB(set.getIp(), set.getTable());
            for(Map.Entry<String, IHumDevice> entry : devices.entrySet()) {
                IHumDevice device = entry.getValue();
                HumDBData data = db.loadData(device.getName());

                device.setStatus(data.isStatus());
                device.setThreshold(data.getThreshold());

                log.info("Loaded hum status from DB for device \"" + device.getName() + "\" is \"" + device.isStatus() + "\"", "HUM");
                log.info("Loaded hum threshold from DB for device \"" + device.getName() + "\" is \"" + device.getThreshold() + "\"", "HUM");
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
        IHumDevice device = devices.get(alias);
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
        IHumDevice device = devices.get(alias);
        device.setThreshold(40);

        boolean status = device.isStatus();
        device.setStatus(!status);

        if (device.isStatus()) {
            log.info("Set hum status \"" + device.getName() + "\" to \"true\"", "HUM");
        } else {
            device.setHumidifier(false);
            log.info("Set hum status \"" + device.getName() + "\" to \"false\"", "HUM");
        }

        /*
         * Syncing states with device
         */
        try {
            socket.setStatus(device.getSocket(), device.isHumidifier(), device.isStatus());
        } catch (Exception e) {
            log.error("Fail to sync hum device " + device.getName(), "HUM");
        }

        /*
         * Save to DB
         */
        try {
            saveStates(device);
        } catch (Exception e) {
            log.error("Fail to save device \"" + device.getName() + "\" state to db", "HUM");
        }
    }

    /**
     * Get device by name
     * @param alias Alias of device
     * @return Found device
     */
    public HumInfo getHumInfo(String alias) throws Exception {
        HumInfo info = new HumInfo();

        IHumDevice device = devices.get(alias);
        if (device == null) {
            throw new Exception("Hum device \""+alias+"\" not found");
        }

        info.setStatus(device.isStatus());
        info.setHeater(device.isHumidifier());
        info.setName(device.getName());
        info.setThreshold(device.getThreshold());
        info.setSensor(device.getSensor());

        return info;
    }

    /**
     * Get all devices info
     * @return Found info
     */
    public List<HumInfo> getHumInfo() {
        List<HumInfo> infoList = new LinkedList<>();

        for(Map.Entry<String, IHumDevice> entry : devices.entrySet()) {
            HumInfo info = new HumInfo();

            IHumDevice device = entry.getValue();
            info.setStatus(device.isStatus());
            info.setHeater(device.isHumidifier());
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
        int curHum = 0;

        for(Map.Entry<String, IHumDevice> entry : devices.entrySet()) {
            IHumDevice device = entry.getValue();

            if (device.isStatus()) {
                try {
                    meteo.getMeteoInfo(device.getSensor()).getHum();
                } catch (Exception e) {
                    log.error("Fail to get meteo info: " + e.getMessage(), "HUM");
                    continue;
                }

                if (curHum < device.getThreshold()) {
                    if (!device.isHumidifier()) {
                        log.info("Set humidifier status \"" + device.getName() + "\" to \"true\"", "HUM");
                        device.setHumidifier(true);
                        try {
                            socket.setStatus(device.getSocket(), device.isHumidifier(), device.isStatus());
                        } catch (Exception e) {
                            log.error("Fail to sync hum device " + device.getName(), "HUM");
                        }
                    }
                }
                if (curHum > device.getThreshold()) {
                    if (device.isHumidifier()) {
                        log.info("Set hum status \"" + device.getName() + "\" to \"false\"", "HUM");
                        device.setHumidifier(false);
                        try {
                            socket.setStatus(device.getSocket(), device.isHumidifier(), device.isStatus());
                        } catch (Exception e) {
                            log.error("Fail to sync hum device " + device.getName(), "HUM");
                        }
                    }
                }
            }
        }
    }

    public String getModName() {
        return modName;
    }

    /**
     * Generate new event
     * @param socket Power socket
     * @param event Event type
     * @throws Exception If fail to gen event
     */
    public void genEvent(String socket, Events event) throws Exception {
        for (Map.Entry<String, IHumDevice> entry : devices.entrySet()) {
            IHumDevice device = entry.getValue();
            if (device.getSocket().equals(socket)) {
                switch (event) {
                    case SWITCH_STATUS_EVENT:
                        switchStatus(device.getAlias());
                        break;
                }
                return;
            }
        }
    }
}
