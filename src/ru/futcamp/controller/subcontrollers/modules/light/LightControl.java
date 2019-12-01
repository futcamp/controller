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

package ru.futcamp.controller.subcontrollers.modules.light;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.events.EventListener;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.subcontrollers.modules.light.db.ILightDB;
import ru.futcamp.controller.subcontrollers.modules.light.db.LightDB;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Light control class
 */
public class LightControl implements ILightControl, EventListener, IAppModule {
    private IConfigs cfg;
    private ILogger log;

    private Map<String, ILightDevice> devices = new HashMap<>();

    private String modName;

    public LightControl(String name, IAppModule ...dep) {
        this.modName = name;
        this.cfg = (IConfigs) dep[0];
        this.log = (ILogger) dep[1];
    }

    /**
     * Save new status to database
     * @param device Light device
     * @throws Exception If fail to save status
     */
    private void saveStatus(ILightDevice device) throws Exception {
        RedisSettings set = cfg.getLightCfg().getDb();
        ILightDB db = null;

        try {
            db = new LightDB(set.getIp(), set.getTable());
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
     * Loading states from ru.futcamp.db
     * @throws Exception If fail to load states
     */
    public void loadStates() throws Exception {
        RedisSettings set = cfg.getLightCfg().getDb();
        ILightDB db = null;

        try {
            db = new LightDB(set.getIp(), set.getTable());
            for (Map.Entry<String, ILightDevice> entry : devices.entrySet()) {
                ILightDevice device = entry.getValue();
                device.setStatus(db.getStatus(device.getName()));
                device.syncStates();
                log.info("Loaded status from DB for light device \"" + device.getName() + "\" is \"" + device.isStatus() + "\"", "LIGHT");
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
     * Switch light device status
     * @param alias Alias of device
     * @throws Exception If fail to switch status
     */
    public void switchStatus(String alias) throws Exception {
        LightInfo info = getLightInfo(alias);
        setLightStatus(alias, !info.isStatus());
    }

    /**
     * Get device info
     * @param alias Alias of device
     * @return Light device
     * @throws Exception If device not found
     */
    public LightInfo getLightInfo(String alias) throws Exception {
        ILightDevice device = devices.get(alias);

        if (device == null) {
            throw new Exception("Device not found");
        }

        LightInfo info = new LightInfo();
        info.setAlias(alias);
        info.setStatus(device.isStatus());

        return info;
    }

    /**
     * Get info about all light devices
     * @return Light device info
     */
    public List<LightInfo> getLightInfo() {
        List<LightInfo> infoList = new LinkedList<>();

        for (Map.Entry<String, ILightDevice> entry : devices.entrySet()) {
            ILightDevice device = entry.getValue();

            LightInfo info = new LightInfo();
            info.setAlias(device.getAlias());
            info.setStatus(device.isStatus());
            infoList.add(info);
        }

        return infoList;
    }

    /**
     * Get light info from group
     * @param group Light group
     * @return Devices list
     */
    public List<LightInfo> getLightGroupInfo(String group) {
        List<LightInfo> infoList = new LinkedList<>();

        for (Map.Entry<String, ILightDevice> entry : devices.entrySet()) {
            ILightDevice device = entry.getValue();

            if (device.getGroup().equals(group)) {
                LightInfo info = new LightInfo();
                info.setAlias(device.getAlias());
                info.setStatus(device.isStatus());
                infoList.add(info);
            }
        }

        return infoList;
    }

    /**
     * Set light group status
     * @param group Light group
     * @param status Light status
     */
    public void setGroupStatus(String group, boolean status)  throws Exception {
        for (Map.Entry<String, ILightDevice> entry : devices.entrySet()) {
            ILightDevice device = entry.getValue();
            if (device.getGroup().equals(group)) {
                if (device.isStatus() == status)
                    continue;
                setLightStatus(device.getAlias(), status);
            }
        }
    }

    /**
     * Set light status by device alias
     * @param alias Alias of device
     * @param status Light status
     */
    public void setLightStatus(String alias, boolean status) throws Exception {
        ILightDevice device = devices.get(alias);

        if (device == null) {
            throw new Exception("Device \""+alias+"\" not found");
        }

        if (device.isStatus() == status)
            return;

        device.setStatus(status);
        device.syncStates();
        saveStatus(device);
    }

    /**
     * Add light device to list
     * @param device Light device
     */
    public void addDevice(ILightDevice device) {
        devices.put(device.getAlias(), device);
    }

    public String getModName() {
        return modName;
    }

    @Override
    public void getEvent(Events event, String module, String ip, int channel) {
        if (module.equals(modName)) {
            switch (event) {
                case SYNC_EVENT:
                    for (Map.Entry<String, ILightDevice> entry : devices.entrySet()) {
                        ILightDevice device = entry.getValue();
                        if (device.getIp().equals(ip)) {
                            try {
                                device.syncStates();
                            } catch (Exception e) {
                                log.error("Fail to first start sync of light device \"" + device.getName() + "\"", "LIGHT");
                            }
                            return;
                        }
                    }
                    break;

                case SWITCH_STATUS_EVENT:
                    for (Map.Entry<String, ILightDevice> entry : devices.entrySet()) {
                        ILightDevice device = entry.getValue();
                        if (device.getSwitchIP().equals(ip) && device.getSwitchChannel() == channel) {
                            try {
                                switchStatus(device.getAlias());
                            } catch (Exception e) {
                                log.error("Fail to switch status of light device \"" + device.getName() + "\"", "LIGHT");
                            }
                            return;
                        }
                    }
                    break;

                case SECURE_OPEN_EVENT:
                    try {
                        setGroupStatus("street", true);
                    } catch (Exception e) {
                        log.error("Fail to set light group status", "LIGHT");
                    }
                    break;
            }
        }
    }
}
