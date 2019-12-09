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
import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.TimeMgmt;
import ru.futcamp.controller.subcontrollers.modules.light.ILightControl;
import ru.futcamp.controller.subcontrollers.modules.secure.db.ISecureDB;
import ru.futcamp.controller.subcontrollers.modules.secure.db.SecureDB;
import ru.futcamp.controller.subcontrollers.modules.socket.IPowerSocket;
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
    private IPowerSocket socket;

    private String modName;
    private String radioSocket;
    private String lampSocket;

    public ManInHome(String name, IAppModule ...dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.light = (ILightControl) dep[1];
        this.cfg = (IConfigs) dep[2];
        this.socket = (IPowerSocket) dep[3];
    }

    public void switchStatus() throws Exception {
        setStatus(!isStatus());

        if (!isStatus()) {
            setRadio(false);
            setLamp(false);
        }

        log.info("Switch MIH status to \"" + isStatus() + "\"", "MIH");

        /*
         * Save time to ru.futcamp.db
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

        setLamp(false);
        setRadio(false);
        for (String lamp : cfg.getSecureCfg().getLamps()) {
            try {
                light.setLightStatus(lamp, false);
            } catch (Exception e) {
                log.error("Fail to set light status for lamp \"" + lamp + "\"", "MIH");
            }
        }

        /*
         * Sync states with device
         */
        try {
            socket.setStatus(lampSocket, isLamp(), isStatus());
            socket.setStatus(radioSocket, isRadio(), isStatus());
        } catch (Exception e) {
            log.error("Fail to sync MIH states: " + e.getMessage(), "SECURE");
        }
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
         * Save time to ru.futcamp.db
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
     * Loading MIH data from ru.futcamp.db
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
    public void getUpdate() {
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

                if (!isLamp() || !isRadio()) {
                    setLamp(true);
                    setRadio(true);
                    /*
                     * Sync states with device
                     */
                    try {
                        socket.setStatus(lampSocket, isLamp(), isStatus());
                        socket.setStatus(radioSocket, isRadio(), isStatus());
                    } catch (Exception e) {
                        log.error("Fail to sync MIH states: " + e.getMessage(), "SECURE");
                    }
                }
            } else {
                if (isRadio() || isLamp()) {
                    setLamp(false);
                    setRadio(false);
                    for (String lamp : cfg.getSecureCfg().getLamps()) {
                        try {
                            light.setLightStatus(lamp, false);
                        } catch (Exception e) {
                            log.error("Fail to set light status for lamp \"" + lamp + "\"", "MIH");
                        }
                    }
                    /*
                     * Sync states with device
                     */
                    try {
                        socket.setStatus(lampSocket, isLamp(), isStatus());
                        socket.setStatus(radioSocket, isRadio(), isStatus());
                    } catch (Exception e) {
                        log.error("Fail to sync MIH states: " + e.getMessage(), "SECURE");
                    }
                }
            }
        }
    }

    public String getModName() {
        return modName;
    }

    public void setSockets(String lampSocket, String radioSocket) {
        this.lampSocket = lampSocket;
        this.radioSocket = radioSocket;
    }
}
