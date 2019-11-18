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

import ru.futcamp.IAppModule;
import ru.futcamp.controller.modules.light.db.ILightDB;
import ru.futcamp.controller.modules.light.db.LightDBData;
import ru.futcamp.controller.modules.therm.IThermDevice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Light control class
 */
public class LightControl implements ILightControl, IAppModule {
    private ILightDB db;

    private Map<String, ILightDevice> devices = new HashMap<>();

    private String modName;

    public LightControl(String name, IAppModule ...dep) {
        this.modName = name;
        this.db = (ILightDB) dep[0];
    }

    /**
     * Save status to db
     * @param device Light device
     * @throws Exception If fail tio save states
     */
    private void saveStates(ILightDevice device) throws Exception {
        try {
            db.connect();
            db.saveStates(new LightDBData(device.getName(), device.isStatus()));
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Get device by name
     * @param name Name of device
     * @return Light device
     */
    private ILightDevice getDeviceByName(String name) {
        for (Map.Entry<String, ILightDevice> entry : devices.entrySet()) {
            ILightDevice device = entry.getValue();
            if (device.getName().equals(name)) {
                return device;
            }
        }
        return null;
    }

    /**
     * Loading states from db
     * @throws Exception If fail to load states
     */
    public void loadStates() throws Exception {
        List<LightDBData> states;

        try {
            db.connect();
            states = db.loadLightStates();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        for (LightDBData datum : states) {
            ILightDevice device = getDeviceByName(datum.getName());
            device.setStatus(datum.isStatus());
        }
    }

    /**
     * Switch light device status
     * @param alias Alias of device
     * @throws Exception If fail to switch status
     */
    public void switchStatus(String alias) throws Exception {
        ILightDevice device = devices.get(alias);
        device.setStatus(!device.isStatus());
        device.syncStates();
        saveStates(device);
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
                device.setStatus(status);
                device.syncStates();
                saveStates(device);
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

        device.setStatus(status);
        device.syncStates();
        saveStates(device);
    }

    /**
     * Set database file name
     * @param fileName Path to db file
     */
    public void setDBFileName(String fileName) {
        db.setFileName(fileName);
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
}
