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
import ru.futcamp.controller.IController;
import ru.futcamp.controller.TimeMgmt;
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.MIHDBData;
import ru.futcamp.controller.modules.secure.mod.SecureModule;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;

/**
 * Main In Home security system
 */
public class ManInHome extends MIHData implements IMainInHome, IAppModule {
    private ISecureDB db;
    private ILogger log;
    private ILightControl light;
    private IConfigs cfg;

    private String ip;
    private String modName;

    public ManInHome(String name, IAppModule ...dep) {
        modName = name;
        this.db = (ISecureDB) dep[0];
        this.log = (ILogger) dep[1];
        this.light = (ILightControl) dep[2];
        this.cfg = (IConfigs) dep[3];
    }

    public void setDBFileName(String fileName) {
        this.db.setFileName(fileName);
    }

    public void setIp(String ip) { this.ip = ip; }

    /**
     * Sync states with device
     */
    public void syncStates() throws Exception {
        SecureModule mod = new SecureModule(ip);
        mod.setMIHStates(isRadio(), isLamp());
    }

    public void switchStatus() throws Exception {
        setStatus(!isStatus());
        saveStatus();
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
        saveStatus();
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
     * Save status to database
     * @throws Exception If fail to save status
     */
    private void saveStatus() throws Exception {
        try {
            db.connect();
            db.saveMIHStatus(new MIHDBData(isStatus(), getTimeOn(), getTimeOff()));
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Loading MIH data from db
     * @throws Exception If fail to load
     */
    public void loadDataFromDb() throws Exception {
        MIHDBData data;

        try {
            db.connect();
            data = db.loadMIHData();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        super.setStatus(data.isStatus());
        super.setTimeOn(data.getTimeOn());
        super.setTimeOff(data.getTimeOff());
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
