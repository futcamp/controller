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

package ru.futcamp.controller;

import ru.futcamp.controller.modules.meteo.IMeteoDevice;
import ru.futcamp.controller.modules.meteo.IMeteoStation;
import ru.futcamp.controller.modules.meteo.MeteoDevice;
import ru.futcamp.controller.modules.meteo.db.MeteoDBData;
import ru.futcamp.controller.modules.secure.IMainInHome;
import ru.futcamp.controller.modules.secure.ISecureDevice;
import ru.futcamp.controller.modules.secure.ISecurity;
import ru.futcamp.controller.modules.secure.SecureDevice;
import ru.futcamp.controller.modules.therm.IThermControl;
import ru.futcamp.controller.modules.therm.IThermDevice;
import ru.futcamp.controller.modules.therm.ThermDevice;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.*;
import ru.futcamp.utils.log.ILogger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Smart home controller
 */
public class Controller implements IController {
    private ILogger log;
    private IConfigs cfg;
    private IMeteoStation meteo;
    private Runnable meteoTask;
    private ISecurity secure;
    private Runnable secureTask;
    private IThermControl thermCtrl;
    private Runnable thermTask;
    private IMainInHome mih;

    public Controller(ILogger log, IConfigs cfg, IMeteoStation meteo, Runnable meteoTask,
                      ISecurity secure, Runnable secureTask, IThermControl thermCtrl,
                      Runnable thermTask, IMainInHome mih) {
        this.log = log;
        this.cfg = cfg;
        this.meteo = meteo;
        this.meteoTask = meteoTask;
        this.secure = secure;
        this.secureTask = secureTask;
        this.thermCtrl = thermCtrl;
        this.thermTask = thermTask;
        this.mih = mih;
    }

    /**
     * Start meteo modules
     */
    public void startModules() {
        MeteoSettings meteoCfg = cfg.getMeteoCfg();
        SecureSettings secCfg = cfg.getSecureCfg();
        ThermSettings thermCfg = cfg.getThermCfg();
        /*
         * Prepare db
         */
        log.info("Loading controller databases", "CTRL");
        meteo.setDBFileName(meteoCfg.getDb());
        if (cfg.getModCfg("security")) {
            secure.setDBFileName(secCfg.getDb());
            try {
                secure.loadStates();
            } catch (Exception e) {
                log.error("Fail to load secure states from db: " + e.getMessage(), "CTRL");
                return;
            }
            mih.setDBFileName(secCfg.getDb());
            try {
                mih.loadDataFromDb();
            } catch (Exception e) {
                log.error("Fail to load ManInHome states from db: " + e.getMessage(), "CTRL");
                return;
            }
        }

        /*
         * Prepare modules
         */
        if (cfg.getModCfg("meteo")) {
            for (MeteoDeviceSettings dev : meteoCfg.getDevices()) {
                MeteoDevice device = new MeteoDevice(dev.getName(), dev.getAlias(), dev.getType(), dev.getIp(),
                        dev.getChannel(), dev.getDelta());
                meteo.addDevice(device);
                log.info("Add new meteo device name: " + dev.getName() + " ip: " + dev.getIp() + " chan: " +
                        dev.getChannel(), "CONTROLLER");
            }
        }
        if (cfg.getModCfg("security")) {
            for (SecureDeviceSettings dev : secCfg.getDevices()) {
                SecureDevice device = new SecureDevice(dev.getName(), dev.getAlias(), dev.getIp(),
                        dev.getChannel(), dev.getType(), dev.getGroup());
                secure.addDevice(device);
                log.info("Add new secure device name: " + dev.getName() + " ip: " + dev.getIp() + " chan: " +
                        dev.getChannel(), "CONTROLLER");
            }
            mih.setIp(secCfg.getMih());
        }
        if (cfg.getModCfg("therm")) {
            for (ThermDeviceSettings dev : thermCfg.getDevices()) {
                IThermDevice device = new ThermDevice(dev.getName(), dev.getAlias(), dev.getIp(), dev.getSensor());
                thermCtrl.addDevice(device);
                log.info("Add new therm device name: " + dev.getName() + " ip: " + dev.getIp(), "CTRL");
            }

            thermCtrl.setDBFileName(thermCfg.getDb());
            try {
                thermCtrl.loadStates();
            } catch (Exception e) {
                log.error("Fail to load therm states from db: " + e.getMessage(), "CTRL");
                return;
            }
        }

        /*
         * Starting all timers
         */
        log.info("Starting controller tasks", "CTRL");
        if (cfg.getModCfg("meteo")) {
            Timer meteoTmr = new Timer(true);
            meteoTmr.scheduleAtFixedRate((TimerTask)meteoTask, 0, 1000);
        }
        if (cfg.getModCfg("security")) {
            Timer secureTmr = new Timer(true);
            secureTmr.scheduleAtFixedRate((TimerTask)secureTask, 0, 1000);
        }
        if (cfg.getModCfg("therm")) {
            Timer thermTmr = new Timer(true);
            thermTmr.scheduleAtFixedRate((TimerTask)thermTask, 0, 1000);
        }
    }

    /**
     * Get all list of meteo devices
     * @return Meteo devices list
     */
    public List<IMeteoDevice> getMeteoDevices() {
        return meteo.getDevices();
    }

    /**
     * Get meteo device by name
     * @param name Name of device
     * @return Meteo device
     */
    public IMeteoDevice getMeteoDevice(String name) { return meteo.getDevice(name); }

    /**
     * Get meteo data from sensor by date
     * @param sensor Meteo sensor name
     * @param date Date
     * @return List of meteo data
     */
    public List<MeteoDBData> getMeteoDataByDate(String sensor, String date) {
        List<MeteoDBData> data = null;

        try {
            data = meteo.getDataByDate(sensor, date);
        } catch (Exception e) {
            log.error("Fail to get meteo data by date from sensor " + sensor + ": " + e.getMessage(), "CTRL");
        }

        return data;
    }

    /**
     * Set action detection state
     * @param ip IP of device
     * @param channel Channel of device
     * @param state New state of device
     */
    public void setSecureState(String ip, int channel, boolean state) { secure.setDeviceState(ip, channel, state); }

    /**
     * Set main status for security module
     * @param status New status
     */
    public void setSecureStatus(boolean status) {
        secure.setStatus(status);
    }

    /**
     * Get main status of security module
     * @return Secure status
     */
    public boolean isSecureStatus() {
        return secure.isStatus();
    }

    /**
     * Get alarm state of security
     * @return Alarm state
     */
    public boolean isSecureAlarm() {
        return secure.isAlarm();
    }

    /**
     * Get all list of secure devices
     * @return Secure devices list
     */
    public List<ISecureDevice> getSecureDevices() {
        return secure.getDevices();
    }

    /**
     * Save security states
     */
    public void saveSecureStates() {
        try {
            secure.saveStates();
        } catch (Exception e) {
            log.error("Fail to save secure states to DB: " + e.getMessage(), "CTRL");
        }
    }

    /**
     * Save "Man In Home" subsystem states
     */
    public void saveMIHStates() {
        try {
            mih.saveData();
        } catch (Exception e) {
            log.error("Fail to save MIH states to DB: " + e.getMessage(), "CTRL");
        }
    }

    public void setMIHStatus(boolean status) {
        mih.setStatus(status);
    }

    public void setMIHRadio(boolean status) {
        mih.setStatus(status);
    }

    public void setMIHLamp(boolean status) {
        mih.setStatus(status);
    }

    public void setMIHTimeOn(boolean status) {
        mih.setStatus(status);
    }

    public void setMIHTimeOn(int time) {
        mih.setTimeOn(time);
    }

    public void setMIHTimeOff(int time) {
        mih.setTimeOff(time);
    }

    public boolean isMIHStatus() {
        return mih.isStatus();
    }

    public boolean isMIHRadio() {
        return mih.isRadio();
    }

    public boolean isMIHLamp() {
        return mih.isLamp();
    }

    public int getMIHTimeOn() {
        return mih.getTimeOn();
    }

    public int getMIHTimeOff() {
        return mih.getTimeOff();
    }

    /**
     * Get devices list
     * @return Devices list
     */
    public List<IThermDevice> getThermDevices() {
        return thermCtrl.getDevices();
    }

    /**
     * Get therm device by alias
     * @param alias Alias of device
     * @return Device pointer
     */
    public IThermDevice getThermDeviceByAlias(String alias) {
        return thermCtrl.getDeviceByAlias(alias);
    }

    /**
     * Save therm state to db
     * @param device Device pointer
     * @throws Exception If fail to save state
     */
    public void saveThermState(IThermDevice device) throws Exception {
        thermCtrl.saveState(device);
    }
}
