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

package ru.futcamp.controller.subcontrollers.modules.secure;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.events.EventListener;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.subcontrollers.modules.light.ILightControl;
import ru.futcamp.controller.subcontrollers.modules.secure.db.ISecureDB;
import ru.futcamp.controller.subcontrollers.modules.secure.db.SecureDB;
import ru.futcamp.controller.subcontrollers.modules.vision.IVision;
import ru.futcamp.net.notifier.INotifier;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Security process
 */
public class Security extends SecureData implements ISecurity, EventListener, IAppModule {
    private ILogger log;
    private INotifier notify;
    private IConfigs cfg;
    private ILightControl light;
    private IVision vision;

    private List<ISecureDevice> devices = new LinkedList<>();
    private boolean alarm;

    private String modName;

    public Security(String name, IAppModule ...dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.notify = (INotifier) dep[1];
        this.cfg = (IConfigs) dep[2];
        this.light = (ILightControl) dep[3];
        this.vision = (IVision) dep[4];
    }

    private void saveStates() throws Exception {
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

        log.info("Switch secure status to \"" + isStatus() + "\"", "SECURE");

        if (!isStatus()) {
            setAlarm(false);
        }
        saveStates();

        for (ISecureDevice device : devices) {
            device.syncSecureAlarm(isAlarm());
        }
    }

    /**
     * Loading states from ru.futcamp.db
     * @throws Exception If fail to load states from ru.futcamp.db
     */
    public void loadStates() throws Exception {
        RedisSettings set = cfg.getSecureCfg().getDb();
        ISecureDB db = null;
        try {
            db = new SecureDB(set.getIp(), set.getTable());
            setStatus(db.getStatus());
            setAlarm(db.getAlarm());
            log.info("Loaded secure status from DB is \"" + db.getStatus() + "\"", "SECURE");
            log.info("Loaded secure alarm status from DB is \"" + db.getAlarm() + "\"", "SECURE");
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

    @Override
    public void getEvent(Events event, String module, String ip, int channel) {
        if (module.equals(modName)) {
            switch (event) {
                case SYNC_EVENT:
                    for (ISecureDevice device : devices) {
                        if (device.getIp().equals(ip)) {
                            try {
                                device.syncSecureAlarm(isAlarm());
                            } catch (Exception e) {
                                log.error("Fail to first start sync of secure device \"" + device.getName() + "\"", "LIGHT");
                            }
                        }
                    }

                    break;

                case SECURE_OPEN_EVENT:
                    if (!isAlarm() && isStatus()) {
                        for (ISecureDevice device : devices) {
                            if (device.getIp().equals(ip) && device.getChannel() == channel) {
                                setAlarm(true);
                                /*
                                 * Send notify
                                 */
                                try {
                                    notify.sendNotify("ОХРАНА", "Внимание! Проникновение! Открыта: " + device.getAlias());
                                    if (device.isWatch()) {
                                        Thread.sleep(500);
                                        vision.getPhoto(device.getCamera(), "/tmp/secure.jpg");
                                        notify.sendNotifyPhoto(device.getCamera(), "/tmp/secure.jpg");
                                    }
                                } catch (Exception e) {
                                    log.error("Fail to send secure notify: " + e.getMessage(), "SECURE");
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
}
