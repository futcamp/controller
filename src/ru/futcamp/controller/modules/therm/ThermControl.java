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

package ru.futcamp.controller.modules.therm;

import ru.futcamp.controller.modules.therm.db.IThermDB;
import ru.futcamp.controller.modules.therm.db.ThermDBData;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Therm control class
 */
public class ThermControl implements IThermControl {
    private IThermDB db;

    private List<IThermDevice> devices = new LinkedList<>();

    public ThermControl(IThermDB db) {
        this.db = db;
    }

    /**
     * Add new device
     * @param device Therm device
     */
    public void addDevice(IThermDevice device) {
        devices.add(device);
    }

    /**
     * Set database file name
     * @param fileName Path to database
     */
    public void setDBFileName(String fileName) {
        db.setFileName(fileName);
    }

    /**
     * Save device state to db
     * @param device Device
     */
    public void saveState(IThermDevice device) throws Exception {
        try {
            db.connect();
            db.saveStates(new ThermDBData(device.getName(), device.isStatus(), device.getThreshold()));
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Load states from db
     * @throws Exception If fail to load states
     */
    public void loadStates() throws Exception {
        List<ThermDBData> data;

        try {
            db.connect();
            data = db.loadThermData();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        for (ThermDBData datum : data) {
            IThermDevice device = getDevice(datum.getName());
            device.setStatus(datum.isStatus());
            device.setThreshold(datum.getThreshold());
        }
    }

    /**
     * Get device by name
     * @param name Name of device
     * @return Found device
     */
    public IThermDevice getDevice(String name) {
        for (IThermDevice device : devices) {
            if (device.getName().equals(name))
                return device;
        }
        return null;
    }

    /**
     * Get all therm devices list
     * @return Devices list
     */
    public List<IThermDevice> getDevices() {
        return devices;
    }

    /**
     * Get device by alias
     * @param alias Alias of device
     * @return Meteo device
     */
    public IThermDevice getDeviceByAlias(String alias) {
        for (IThermDevice device : devices) {
            if (device.getAlias().equals(alias))
                return device;
        }
        return null;
    }
}
