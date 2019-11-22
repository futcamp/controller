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
import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.TimeMgmt;
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.SecureDB;
import ru.futcamp.controller.modules.secure.mod.SecureModule;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.RedisSettings;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;

/**
 * Main In Home security system
 */
public class ManInHome extends MIHData implements IMainInHome, IAppModule {
    private ILogger log;
    private ILightControl light;
    private IConfigs cfg;

    private String modName;

    public ManInHome(String name, IAppModule ...dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.light = (ILightControl) dep[1];
        this.cfg = (IConfigs) dep[2];
    }

    /**
     * Sync states with device
     */
    public void syncStates() throws Exception {
        SecureModule mod = new SecureModule(cfg.getSecureCfg().getMih());
        mod.setMIHStates(isRadio(), isLamp());
    }

    public void switchStatus() throws Exception {
        setStatus(!isStatus());

        /*
         * Save time to db
         */
        RedisSettings set = cfg.getSecureCfg().getDb();
        ISecureDB db = null;
        try {
            db = new SecureDB(set.getIp(), set.getTable());
            db.saveMIHStatus(super.isStatus());
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        update();
    }

    /**
     * Change current on/off time
     * @param time Type of time
     * @param action Action of time
     * @throws Exception If fail to set new time
     */
    public void changeTime(TimeMgmt time, ActMgmt action) throws Exception {
        int hour;

        if (time.equals(TimeMgmt.TIME_MGMT_ON)) {
            hour = getTimeOn();

            switch (action) {
                case SET_MGMT_HOUR_PLUS:
                    setTimeOn(hour + 1);
                    break;

                case SET_MGMT_HOUR_MINUS:
                    setTimeOn(hour - 1);
                    break;
            }
        } else if (time.equals(TimeMgmt.TIME_MGMT_OFF)) {
            hour = getTimeOff();

            switch (action) {
                case SET_MGMT_HOUR_PLUS:
                    setTimeOff(hour + 1);
                    break;

                case SET_MGMT_HOUR_MINUS:
                    setTimeOff(hour - 1);
                    break;
            }
        }

        /*
         * Save time to db
         */
        RedisSettings set = cfg.getSecureCfg().getDb();
        ISecureDB db = null;
        try {
            db = new SecureDB(set.getIp(), set.getTable());
            db.saveMIHTimeOn(super.getTimeOn());
            db.saveMIHTimeOff(super.getTimeOff());
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        update();
    }

    /**
     * Get Man In Home information
     * @return Information
     */
    public MIHInfo getMIHInfo() {
        MIHInfo info = new MIHInfo();

        info.setLamp(isLamp());
        info.setRadio(isRadio());
        info.setStatus(isStatus());
        info.setTimeOff(getTimeOff());
        info.setTimeOn(getTimeOn());

        return info;
    }

    /**
     * Loading MIH data from db
     * @throws Exception If fail to load
     */
    public void loadDataFromDb() throws Exception {
        RedisSettings set = cfg.getSecureCfg().getDb();
        ISecureDB db = null;
        try {
            db = new SecureDB(set.getIp(), set.getTable());
            super.setStatus(db.getMIHStatus());
            super.setTimeOn(db.getMIHTimeOn());
            super.setTimeOff(db.getMIHTimeOff());
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Update states
     */
    public void update() {
        int curTime = TimeControl.getCurHour();

        if (isStatus()) {
            /*
             * Switch on street lamps
             */

            if (curTime >= getTimeOn() && curTime < getTimeOff()) {
                for (String lamp : cfg.getSecureCfg().getLamps()) {
                    try {
                        light.setLightStatus(lamp, true);
                    } catch (Exception e) {
                        log.error("Fail to set light status for lamp \"" + lamp + "\"", "MIH");
                    }
                }

                setLamp(true);
                /*
                 * Switch on radio
                 */
                if (curTime % 2 == 0) {
                    setRadio(false);
                } else {
                    setRadio(true);
                }
            } else {
                /*
                 * Switch off street lamps
                 */

                setLamp(false);
                setRadio(false);
                for (String lamp : cfg.getSecureCfg().getLamps()) {
                    try {
                        light.setLightStatus(lamp, false);
                    } catch (Exception e) {
                        log.error("Fail to set light status for lamp \"" + lamp + "\"", "MIH");
                    }
                }
            }
        } else {
            setLamp(false);
            setRadio(false);
            for (String lamp : cfg.getSecureCfg().getLamps()) {
                try {
                    light.setLightStatus(lamp, false);
                } catch (Exception e) {
                    log.error("Fail to set light status for lamp \"" + lamp + "\"", "MIH");
                }
            }
        }

        /*
         * Sync states with device
         */
        try {
            syncStates();
        } catch (Exception e) {
            log.error("Fail to sync MIH states: " + e.getMessage(), "SECURE");
        }
    }

    public String getModName() {
        return modName;
    }
}
