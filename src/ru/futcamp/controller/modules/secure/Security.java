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

package ru.futcamp.controller.modules.secure;

import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.SecureDBData;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Security process
 */
public class Security implements ISecurity {
    private ISecureDB db;

    private List<ISecureDevice> devices = new LinkedList<>();
    private boolean status;
    private boolean alarm;

    public Security(ISecureDB db) {
        this.db = db;
    }

    /**
     * Set path to database file
     * @param fileName Path to db
     */
    public void setDBFileName(String fileName) {
        db.setFileName(fileName);
    }

    /**
     * Set action detection state
     * @param ip IP of device
     * @param channel Channel of device
     * @param state New state of device
     */
    public void setDeviceState(String ip, int channel, boolean state) {
        for (ISecureDevice device : devices) {
            if (device.getIp().equals(ip) && device.getChannel() == channel) {
                device.setState(state);
                return;
            }
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    /**
     * Save states to database
     * @throws Exception If fail to save states
     */
    public void saveStates() throws Exception {
        try {
            db.connect();
            db.saveStates(new SecureDBData(isStatus(), isAlarm()));
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Loading states from db
     * @throws Exception If fail to load states from db
     */
    public void loadStates() throws Exception {
        try {
            db.connect();
            SecureDBData states = db.loadSecureStates();
            this.setStatus(states.isStatus());
            this.setAlarm(states.isAlarm());
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    public List<ISecureDevice> getDevices() {
        return devices;
    }

    public void addDevice(ISecureDevice device) {
        devices.add(device);
    }
}
