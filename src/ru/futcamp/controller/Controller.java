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

import ru.futcamp.IAppModule;
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.light.ILightDevice;
import ru.futcamp.controller.modules.light.LightDevice;
import ru.futcamp.controller.modules.light.LightInfo;
import ru.futcamp.controller.modules.meteo.IMeteoStation;
import ru.futcamp.controller.modules.meteo.MeteoDevice;
import ru.futcamp.controller.modules.meteo.MeteoInfo;
import ru.futcamp.controller.modules.secure.*;
import ru.futcamp.controller.modules.therm.IThermControl;
import ru.futcamp.controller.modules.therm.IThermDevice;
import ru.futcamp.controller.modules.therm.ThermDevice;
import ru.futcamp.controller.modules.therm.ThermInfo;
import ru.futcamp.controller.modules.vision.CamDevice;
import ru.futcamp.controller.modules.vision.ICamDevice;
import ru.futcamp.controller.modules.vision.IVision;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.light.LightDeviceSettings;
import ru.futcamp.utils.configs.settings.light.LightSettings;
import ru.futcamp.utils.configs.settings.meteo.MeteoDeviceSettings;
import ru.futcamp.utils.configs.settings.meteo.MeteoSettings;
import ru.futcamp.utils.configs.settings.secure.SecureDeviceSettings;
import ru.futcamp.utils.configs.settings.secure.SecureSettings;
import ru.futcamp.utils.configs.settings.therm.ThermDeviceSettings;
import ru.futcamp.utils.configs.settings.therm.ThermSettings;
import ru.futcamp.utils.configs.settings.vision.VisionDeviceSettings;
import ru.futcamp.utils.configs.settings.vision.VisionSettings;
import ru.futcamp.utils.log.ILogger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Smart home controller
 */
public class Controller implements IController, IAppModule {
    private ILogger log;
    private IConfigs cfg;
    private IMeteoStation meteo;
    private Runnable meteoTask;
    private ISecurity secure;
    private Runnable secureTask;
    private IThermControl therm;
    private Runnable thermTask;
    private IMainInHome mih;
    private ILightControl light;
    private Runnable lightTask;
    private IVision vision;
    private Runnable visionTask;

    private String modName;

    public Controller(String name, IAppModule ...dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.meteo = (IMeteoStation) dep[2];
        this.meteoTask = (Runnable) dep[3];
        this.secure = (ISecurity) dep[4];
        this.secureTask = (Runnable) dep[5];
        this.therm = (IThermControl) dep[6];
        this.thermTask = (Runnable) dep[7];
        this.mih = (IMainInHome) dep[8];
        this.light = (ILightControl) dep[9];
        this.lightTask = (Runnable) dep[10];
        this.vision = (IVision) dep[11];
        this.visionTask = (Runnable) dep[12];
    }

    private boolean startMeteoModule() {
        MeteoSettings meteoCfg = cfg.getMeteoCfg();
        meteo.setDBFileName(meteoCfg.getDb());

        /*
         * Add devices from configs
         */
        for (MeteoDeviceSettings dev : meteoCfg.getDevices()) {
            MeteoDevice device = new MeteoDevice(dev.getName(), dev.getAlias(), dev.getType(), dev.getIp(),
                    dev.getChannel(), dev.getDelta());
            meteo.addDevice(device);
            log.info("Add new meteo device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\" chan \"" +
                    dev.getChannel() + "\"", "CTRL");
        }

        /*
         * Run task
         */
        Timer meteoTmr = new Timer(true);
        meteoTmr.scheduleAtFixedRate((TimerTask)meteoTask, 0, 1000);
        return true;
    }

    private boolean startSecureModule() {
        SecureSettings secCfg = cfg.getSecureCfg();

        /*
         * Add devices from configs
         */
        for (SecureDeviceSettings dev : secCfg.getDevices()) {
            SecureDevice device = new SecureDevice(dev.getName(), dev.getAlias(), dev.getIp(),
                    dev.getChannel(), dev.getType(), dev.getGroup());
            secure.addDevice(device);
            log.info("Add new secure device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\" chan \"" +
                    dev.getChannel() + "\"", "CTRL");
        }

        /*
         * Loading states from DB
         */
        try {
            secure.loadStates();
        } catch (Exception e) {
            log.error("Fail to load secure states from db: " + e.getMessage(), "CTRL");
            return false;
        }

        /*
         * Loading states from DB
         */
        try {
            mih.loadDataFromDb();
        } catch (Exception e) {
            log.error("Fail to load ManInHome states from db: " + e.getMessage(), "CTRL");
            return false;
        }

        /*
         * Run task
         */
        Timer secureTmr = new Timer(true);
        secureTmr.scheduleAtFixedRate((TimerTask)secureTask, 0, 1000);
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
        return true;
    }

    private boolean startLightModule() {
        LightSettings lightCfg = cfg.getLightCfg();

        /*
         * Add devices from cfg
         */
        for (LightDeviceSettings dev : lightCfg.getDevices()) {
            ILightDevice device = new LightDevice(dev.getName(), dev.getAlias(), dev.getGroup(), dev.getIp(), dev.getChannel());
            light.addDevice(device);
            log.info("Add new light device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\" chan \"" +
                    device.getChannel() + "\"", "CTRL");
        }

        /*
         * Loading states from DB
         */
        try {
            light.loadStates();
        } catch (Exception e) {
            log.error("Fail to load light states from db: " + e.getMessage(), "CTRL");
            return false;
        }

        /*
         * Run task
         */
        Timer lightTmr = new Timer(true);
        lightTmr.scheduleAtFixedRate((TimerTask)lightTask, 0, 1000);
        return true;
    }

    private boolean startVisionModule() {
        VisionSettings visionCfg = cfg.getVisionCfg();

        /*
         * Add devices from cfg
         */
        for (VisionDeviceSettings dev : visionCfg.getDevices()) {
            ICamDevice device = new CamDevice(dev.getName(), dev.getAlias(), dev.getIp(), dev.getChannel(), dev.isWarming(), dev.getLamps());
            vision.addCamera(device);
            log.info("Add new vision device name \"" + dev.getName() + "\" ip \"" + dev.getIp() + "\" chan \"" +
                    dev.getChannel() + "\"", "CTRL");
        }

        /*
         * Run task
         */
        Timer visionTmr = new Timer(true);
        visionTmr.scheduleAtFixedRate((TimerTask)visionTask, 0, 1000);
        return true;
    }

    /**
     * Start meteo modules
     */
    public boolean startModules() {
        if (cfg.getModCfg("meteo")) {
            if (!startMeteoModule())
                return false;
        }
        if (cfg.getModCfg("security")) {
            if (!startSecureModule())
                return false;
        }
        if (cfg.getModCfg("therm")) {
            if (!startThermModule())
                return false;
        }
        if (cfg.getModCfg("light")) {
            if (!startLightModule())
                return false;
        }
        if (cfg.getModCfg("vision")) {
            if (!startVisionModule())
                return false;
        }
        return true;
    }

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
    public MeteoInfo getMeteoInfo(String alias) { return meteo.getMeteoInfo(alias); }

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
     * Switch current MIH status
     * @throws Exception If fail to switch status
     */
    public void switchMIHStatus() throws Exception {
        mih.switchStatus();
    }

    /**
     * Change current on/off time
     * @param time Type of time
     * @param action Action of time
     * @throws Exception If fail to set new time
     */
    public void changeMIHTime(TimeMgmt time, ActMgmt action) throws Exception {
        mih.changeTime(time, action);
    }

    /**
     * Get Man In Home information
     * @return Information
     */
    public MIHInfo getMIHInfo() {
        return mih.getMIHInfo();
    }

    /**
     * Get security information
     * @return Security information
     */
    public SecureInfo getSecureInfo() { return secure.getSecureInfo(); }

    /**
     * Switch current security status
     * @throws Exception If fail to save new status to DB
     */
    public void switchSecureStatus() throws Exception { secure.switchStatus(); }

    /**
     * New security action from remote sensor
     * @param ip Address of sensor
     * @param chan Device channel
     */
    public void newSecureAction(String ip, int chan) { secure.newAction(ip, chan); }

    /**
     * Switch light device status
     * @param alias Alias of device
     * @throws Exception If fail to switch status
     */
    public void switchLightStatus(String alias) throws Exception {
        light.switchStatus(alias);
    }

    /**
     * Get light info list
     * @return Devices list
     */
    public List<LightInfo> getLightInfo() {
        return light.getLightInfo();
    }

    /**
     * Get light devices by group
     * @param group Light devices group
     * @return Light devices list
     */
    public List<LightInfo> getLightGroupInfo(String group) {
        return light.getLightGroupInfo(group);
    }

    /**
     * Set new status to light group
     * @param group Light devices group
     * @param status New status
     */
    public void setGroupStatus(String group, boolean status) throws Exception {
        light.setGroupStatus(group, status);
    }

    /**
     * Set light status by alias
     * @param alias Alias of device
     * @param status New status
     * @throws Exception If fail to set new status
     */
    public void setLightStatus(String alias, boolean status) throws Exception {
        light.setLightStatus(alias, status);
    }

    /**
     * Get light info
     * @param alias Alias of device
     * @return Light device
     * @throws Exception If fail to get light device
     */
    public LightInfo getLightInfo(String alias) throws Exception {
        return light.getLightInfo(alias);
    }

    /**
     * Get photo from camera
     * @param alias Alias of camera
     * @param fileName Path to photo
     * @throws Exception if fail to get photo
     */
    public void getVisionPhoto(String alias, String fileName) throws Exception {
        vision.getPhoto(alias, fileName);
    }

    /**
     * Get photo from cams with light module support
     * @param alias Alias of camera
     * @param fileName Path to photo
     * @param isLight Switch on/off light
     * @throws Exception If fail to get photo
     */
    public void getVisionPhoto(String alias, String fileName, boolean isLight) throws Exception {
        vision.getPhoto(alias, fileName, isLight);
    }

    public String getModName() {
        return modName;
    }
}
