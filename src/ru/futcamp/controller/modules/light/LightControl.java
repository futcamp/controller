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

import ru.futcamp.controller.modules.light.db.ILightDB;
import ru.futcamp.controller.modules.light.db.LightDBData;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Light control class
 */
public class LightControl implements ILightControl {
    private ILightDB db;

    private List<ILightDevice> devices = new LinkedList<>();

    public LightControl(ILightDB db) {
        this.db = db;
    }

    /**
     * Save state to db
     * @param device Light device
     * @throws Exception If fail tio save states
     */
    public void saveState(ILightDevice device) throws Exception {
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
            ILightDevice device = getDevice(datum.getName());
            device.setStatus(datum.isStatus());
        }
    }

    /**
     * Set database file name
     * @param fileName Path to db file
     */
    public void setDBFileName(String fileName) {
        db.setFileName(fileName);
    }

    /**
     * Get all devices list
     * @return Devices list
     */
    public List<ILightDevice> getDevices() {
        return devices;
    }

    /**
     * Add light device to list
     * @param device
     */
    public void addDevice(ILightDevice device) {
        devices.add(device);
    }

    /**
     * Get device by name
     * @param name Name of device
     * @return Device pointer
     */
    public ILightDevice getDevice(String name) {
        for (ILightDevice device : devices) {
            if (device.getName().equals(name)) {
                return device;
            }
        }
        return null;
    }

    /**
     * Get device by name
     * @param alias Alias of device
     * @return Device pointer
     */
    public ILightDevice getDeviceByAlias(String alias) {
        for (ILightDevice device : devices) {
            if (device.getAlias().equals(alias)) {
                return device;
            }
        }
        return null;
    }

    /**
     * Get devices list by group
     * @param group Group name
     * @return Devices list
     */
    public List<ILightDevice> getDevicesGroup(String group) {
        List<ILightDevice> devs = new LinkedList<>();

        for (ILightDevice device : devices) {
            if (device.getGroup().equals(group)) {
                devs.add(device);
            }
        }

        return devs;
    }
}
