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

import ru.futcamp.IAppModule;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.SecureDBData;
import ru.futcamp.net.INotifier;
import ru.futcamp.tgbot.menu.SecureMenu;
import ru.futcamp.utils.log.ILogger;
import sun.awt.Mutex;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Security process
 */
public class Security implements ISecurity, IAppModule {
    private ISecureDB db;
    private ILogger log;
    private INotifier notify;

    private List<ISecureDevice> devices = new LinkedList<>();
    private boolean status;
    private boolean alarm;
    private Mutex mtxStat = new Mutex();

    private String modName;

    public Security(String name, IAppModule ...dep) {
        modName = name;
        this.db = (ISecureDB) dep[0];
        this.log = (ILogger) dep[1];
        this.notify = (INotifier) dep[2];
    }

    /**
     * Set path to database file
     * @param fileName Path to db
     */
    public void setDBFileName(String fileName) {
        db.setFileName(fileName);
    }

    /**
     * Get security info and states
     * @return Security info and states
     */
    public SecureInfo getSecureInfo() {
        List<SecureModInfo> modules = new LinkedList<>();

        for (ISecureDevice device : devices) {
            SecureModInfo mod = new SecureModInfo();
            mod.setAlias(device.getAlias());
            mod.setState(device.isState());
            modules.add(mod);
        }

        SecureInfo info = new SecureInfo();
        info.setAlarm(isAlarm());
        info.setStatus(isStatus());
        info.setModules(modules);

        return info;
    }

    /**
     * Switch current security status
     * @throws Exception If fail to save to DB new status
     */
    public void switchStatus() throws Exception {
        setStatus(!isStatus());
        saveStates();
        update(null);
    }

    /**
     * New action from sensor callback
     * @param ip IP of sensor
     */
    public void newAction(String ip, int chan) {
        for (ISecureDevice device : devices) {
            if (device.getIp().equals(ip) && device.getChannel() == chan) {
                device.setState(true);

                if (isStatus()) {
                    setAlarm(true);
                }
                update(device.getAlias());
                break;
            }
        }
    }

    /**
     * Update security states
     * @param alias Alias of device
     */
    private void update(String alias) {
        if (!isStatus()) {
            setAlarm(false);
        }

        if (isAlarm()) {
            /*
             * Send telegram message
             */
            try {
                notify.sendNotify("Внимание! Проникновение! Открыта: " + alias);
            } catch (Exception e) {
                log.error("Fail to send secure notify: " + e.getMessage(), "SECURE");
            }
            /*
             * Switch on all street lamps
             */
        }

        /*
         * Switch on siren
         */
        for (ISecureDevice device : devices) {
            if (!isStatus()) {
                device.setState(false);
            }
            try {
                device.syncSecureAlarm(isAlarm());
            } catch (Exception e) {
                log.error("Fail to sync alarm with device \""+device.getName()+"\": " + e.getMessage(), "SECURE");
            }
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

    /**
     * Add new device to secure base
     * @param device Secure device
     */
    public void addDevice(ISecureDevice device) {
        devices.add(device);
    }

    public String getModName() {
        return modName;
    }

    /**
     * Save states to database
     * @throws Exception If fail to save states
     */
    private void saveStates() throws Exception {
        try {
            db.connect();
            db.saveStates(new SecureDBData(isStatus(), isAlarm()));
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    private void setStatus(boolean status) {
        mtxStat.lock();
        this.status = status;
        mtxStat.unlock();
    }

    private boolean isStatus() {
        boolean status;

        mtxStat.lock();
        status = this.status;
        mtxStat.unlock();

        return status;
    }

    private void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    private boolean isAlarm() {
        return alarm;
    }
}
