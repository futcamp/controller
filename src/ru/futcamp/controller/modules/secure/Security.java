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
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.SecureDB;
import ru.futcamp.net.INotifier;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Security process
 */
public class Security extends SecureData implements ISecurity, IAppModule {
    private ILogger log;
    private INotifier notify;
    private IConfigs cfg;
    private ILightControl light;

    private List<ISecureDevice> devices = new LinkedList<>();
    private boolean alarm;

    private String modName;

    public Security(String name, IAppModule ...dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.notify = (INotifier) dep[1];
        this.cfg = (IConfigs) dep[2];
        this.light = (ILightControl) dep[3];
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
            mod.setState(device.isStatus());
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

        RedisSettings set = cfg.getSecureCfg().getDb();
        ISecureDB db = null;
        try {
            db = new SecureDB(set.getIp(), set.getTable());
            db.saveStatus(isStatus());
            db.saveAlarm(isAlarm());
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        update();
    }

    /**
     * New action from sensor callback
     * @param ip IP of sensor
     */
    public void newAction(String ip, int chan) {
        for (ISecureDevice device : devices) {
            if (device.getIp().equals(ip) && device.getChannel() == chan) {
                device.setStatus(true);

                /*
                 * Send telegram message
                 */
                if (!isAlarm()) {
                    try {
                        notify.sendNotify("Внимание! Проникновение! Открыта: " + device.getAlias());
                    } catch (Exception e) {
                        log.error("Fail to send secure notify: " + e.getMessage(), "SECURE");
                    }
                    /*
                     * Switch on all street lamps
                     */
                    try {
                        light.setGroupStatus("street", true);
                    } catch (Exception e) {
                        log.error("Fail to switch on lamps: " + e.getMessage(), "SECURE");
                    }
                }

                if (isStatus()) {
                    setAlarm(true);
                }

                update();
                break;
            }
        }
    }

    /**
     * Update security states
     */
    public void update() {
        if (!isStatus()) {
            setAlarm(false);
        }

        /*
         * Switch on/off siren
         */
        for (ISecureDevice device : devices) {
            if (!isStatus()) {
                device.setStatus(false);
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
        RedisSettings set = cfg.getSecureCfg().getDb();
        ISecureDB db = null;
        try {
            db = new SecureDB(set.getIp(), set.getTable());
            setStatus(db.getStatus());
            setAlarm(db.getAlarm());
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

    private void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    private boolean isAlarm() {
        return alarm;
    }
}
