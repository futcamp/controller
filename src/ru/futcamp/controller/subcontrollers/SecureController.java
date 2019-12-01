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
import ru.futcamp.controller.TimeMgmt;
import ru.futcamp.controller.events.EventListener;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.events.IEventManager;
import ru.futcamp.controller.subcontrollers.modules.secure.*;
import ru.futcamp.controller.subcontrollers.modules.vision.CamDevice;
import ru.futcamp.controller.subcontrollers.modules.vision.ICamDevice;
import ru.futcamp.controller.subcontrollers.modules.vision.IVision;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.secure.SecureDeviceSettings;
import ru.futcamp.utils.configs.settings.secure.SecureSettings;
import ru.futcamp.utils.configs.settings.vision.VisionDeviceSettings;
import ru.futcamp.utils.configs.settings.vision.VisionSettings;
import ru.futcamp.utils.log.ILogger;

import java.util.Timer;
import java.util.TimerTask;

public class SecureController implements ISecureController, IAppModule {
    private ILogger log;
    private IConfigs cfg;
    private ISecurity secure;
    private Runnable secureTask;
    private IMainInHome mih;
    private IVision vision;
    private Runnable visionTask;
    private IEventManager evMngr;

    private String modName;

    public SecureController(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.evMngr = (IEventManager) dep[2];
        this.secure = (ISecurity) dep[3];
        this.secureTask = (Runnable) dep[4];
        this.mih = (IMainInHome) dep[5];
        this.vision = (IVision) dep[6];
        this.visionTask = (Runnable) dep[7];
    }

    public boolean start() {
        if (cfg.getModCfg("security")) {
            if (!startSecureModule())
                return false;
        }
        if (cfg.getModCfg("vision")) {
            if (!startVisionModule())
                return false;
        }
        return true;
    }

    private boolean startSecureModule() {
        SecureSettings secCfg = cfg.getSecureCfg();

        /*
         * Add devices from configs
         */
        for (SecureDeviceSettings dev : secCfg.getDevices()) {
            SecureDevice device = new SecureDevice(dev.getName(), dev.getAlias(), dev.getIp(),
                    dev.getChannel(), dev.getType(), dev.getGroup(), dev.getCamera().getName(),
                    dev.getCamera().isEnable());
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

        /*
         * Add events
         */
        evMngr.addListener(Events.SYNC_EVENT, (EventListener) secure);
        evMngr.addListener(Events.SYNC_EVENT, (EventListener) mih);
        evMngr.addListener(Events.SECURE_OPEN_EVENT, (EventListener) secure);
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

    /*
     * Control functions
     */

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
