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

package ru.futcamp.controller.subcontrollers;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.events.EventListener;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.events.IEventManager;
import ru.futcamp.controller.subcontrollers.modules.hum.HumDevice;
import ru.futcamp.controller.subcontrollers.modules.hum.HumInfo;
import ru.futcamp.controller.subcontrollers.modules.hum.IHumControl;
import ru.futcamp.controller.subcontrollers.modules.hum.IHumDevice;
import ru.futcamp.controller.subcontrollers.modules.meteo.IMeteoStation;
import ru.futcamp.controller.subcontrollers.modules.meteo.MeteoDevice;
import ru.futcamp.controller.subcontrollers.modules.meteo.MeteoInfo;
import ru.futcamp.controller.subcontrollers.modules.meteo.db.MeteoDB;
import ru.futcamp.controller.subcontrollers.modules.therm.IThermControl;
import ru.futcamp.controller.subcontrollers.modules.therm.IThermDevice;
import ru.futcamp.controller.subcontrollers.modules.therm.ThermDevice;
import ru.futcamp.controller.subcontrollers.modules.therm.ThermInfo;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.hum.HumDeviceSettings;
import ru.futcamp.utils.configs.settings.hum.HumSettings;
import ru.futcamp.utils.configs.settings.meteo.MeteoDeviceSettings;
import ru.futcamp.utils.configs.settings.meteo.MeteoSettings;
import ru.futcamp.utils.configs.settings.therm.ThermDeviceSettings;
import ru.futcamp.utils.configs.settings.therm.ThermSettings;
import ru.futcamp.utils.log.ILogger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MeteoController implements IMeteoController, IAppModule {
    private ILogger log;
    private IConfigs cfg;
    private IEventManager evMngr;
    private IMeteoStation meteo;
    private Runnable meteoTask;
    private IThermControl therm;
    private Runnable thermTask;
    private IHumControl hum;
    private Runnable humTask;

    private String modName;

    public MeteoController(String name, IAppModule ...dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.evMngr = (IEventManager) dep[2];
        this.meteo = (IMeteoStation) dep[3];
        this.meteoTask = (Runnable) dep[4];
        this.therm = (IThermControl) dep[5];
        this.thermTask = (Runnable) dep[6];
        this.hum = (IHumControl) dep[7];
        this.humTask = (Runnable) dep[8];
    }

    public boolean start() {
        if (cfg.getModCfg("meteo")) {
            if (!startMeteoModule())
                return false;
        }
        if (cfg.getModCfg("therm")) {
            if (!startThermModule())
                return false;
        }
        if (cfg.getModCfg("hum")) {
            if (!startHumModule())
                return false;
        }
        return true;
    }

    private boolean startMeteoModule() {
        MeteoSettings meteoCfg = cfg.getMeteoCfg();
        MeteoDB.setPath(meteoCfg.getDb());

        /*
         * Add devices from configs
         */
        for (MeteoDeviceSettings dev : meteoCfg.getDevices()) {
            MeteoDevice device = new MeteoDevice(dev.getName(), dev.getAlias(), dev.getType(), dev.getIp(),
                    dev.getChannel(), dev.getDelta());
            meteo.addDevice(device);
            log.info("Add new meteo device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\" chan \"" +
                    dev.getChannel() + "\"", "METEOCTRL");
        }

        /*
         * Run task
         */
        Timer meteoTmr = new Timer(true);
        meteoTmr.scheduleAtFixedRate((TimerTask) meteoTask, 0, 1000);
        return true;
    }

    private boolean startThermModule() {
        ThermSettings thermCfg = cfg.getThermCfg();

        /*
         * Add devices from cfg
         */
        for (ThermDeviceSettings dev : thermCfg.getDevices()) {
            IThermDevice device = new ThermDevice(dev.getName(), dev.getAlias(), dev.getIp(), dev.getSensor());
            therm.addDevice(device);
            log.info("Add new therm device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\"", "CTRL");
        }

        /*
         * Loading states from DB
         */
        try {
            therm.loadStates();
        } catch (Exception e) {
            log.error("Fail to load therm states from db: " + e.getMessage(), "CTRL");
            return false;
        }

        /*
         * Run task
         */
        Timer thermTmr = new Timer(true);
        thermTmr.scheduleAtFixedRate((TimerTask)thermTask, 0, 1000);

        /*
         * Add events
         */
        evMngr.addListener(Events.SYNC_EVENT, (EventListener) therm);
        return true;
    }

    private boolean startHumModule() {
        HumSettings humCfg = cfg.getHumCfg();

        /*
         * Add devices from cfg
         */
        for (HumDeviceSettings dev : humCfg.getDevices()) {
            IHumDevice device = new HumDevice(dev.getName(), dev.getAlias(), dev.getIp(), dev.getSensor());
            hum.addDevice(device);
            log.info("Add new hum device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\"", "CTRL");
        }

        /*
         * Loading states from DB
         */
        try {
            hum.loadStates();
        } catch (Exception e) {
            log.error("Fail to load hum states from db: " + e.getMessage(), "CTRL");
            return false;
        }

        /*
         * Run task
         */
        Timer humTmr = new Timer(true);
        humTmr.scheduleAtFixedRate((TimerTask)humTask, 0, 1000);

        /*
         * Add events
         */
        evMngr.addListener(Events.SYNC_EVENT, (EventListener) hum);
        return true;
    }

    /*
     * Control functions
     */

    /**
     * Get all meteo info
     * @return Meteo info list
     */
    public List<MeteoInfo> getMeteoInfo() {
        return meteo.getMeteoInfo();
    }

    /**
     * Get meteo device by name
     * @param alias Alias of device
     * @return Meteo info
     */
    public MeteoInfo getMeteoInfo(String alias) throws Exception { return meteo.getMeteoInfo(alias); }

    /**
     * Get meteo info from sensor by date
     * @param alias Meteo sensor name
     * @param date Date
     * @return List of meteo data
     */
    public List<MeteoInfo> getMeteoInfoByDate(String alias, String date) throws Exception {
        return meteo.getMeteoInfoByDate(alias, date);
    }

    /**
     * Switch current therm status
     * @param alias Alias of device
     * @throws Exception If fail to set new status
     */
    public void switchThermStatus(String alias) throws Exception {
        therm.switchStatus(alias);
    }

    /**
     * Get therm control info from all devices
     * @return Info list
     */
    public List<ThermInfo> getThermInfo() { return therm.getThermInfo(); }

    /**
     * Get therm control info from one device
     * @param alias Alias of device
     * @return Therm info
     */
    public ThermInfo getThermInfo(String alias) throws Exception { return therm.getThermInfo(alias); }

    /**
     * Change current threshold value
     * @param alias Alias of device
     * @param action Action of threshold
     * @throws Exception If fail to set new value
     */
    public void changeThermThreshold(String alias, ActMgmt action) throws Exception {
        therm.changeThreshold(alias, action);
    }

    /**
     * Switch current therm status
     * @param alias Alias of device
     * @throws Exception If fail to set new status
     */
    public void switchHumStatus(String alias) throws Exception {
        hum.switchStatus(alias);
    }

    /**
     * Get therm control info from all devices
     * @return Info list
     */
    public List<HumInfo> getHumInfo() { return hum.getHumInfo(); }

    /**
     * Get therm control info from one device
     * @param alias Alias of device
     * @return Therm info
     */
    public HumInfo getHumInfo(String alias) throws Exception { return hum.getHumInfo(alias); }

    /**
     * Change current threshold value
     * @param alias Alias of device
     * @param action Action of threshold
     * @throws Exception If fail to set new value
     */
    public void changeHumThreshold(String alias, ActMgmt action) throws Exception {
        hum.changeThreshold(alias, action);
    }

    public String getModName() {
        return modName;
    }
}
