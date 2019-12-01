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
import ru.futcamp.controller.events.EventListener;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.events.IEventManager;
import ru.futcamp.controller.subcontrollers.modules.light.ILightControl;
import ru.futcamp.controller.subcontrollers.modules.light.ILightDevice;
import ru.futcamp.controller.subcontrollers.modules.light.LightDevice;
import ru.futcamp.controller.subcontrollers.modules.light.LightInfo;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.light.LightDeviceSettings;
import ru.futcamp.utils.configs.settings.light.LightSettings;
import ru.futcamp.utils.log.ILogger;

import java.util.List;

public class LightController implements ILightController, IAppModule {
    private ILogger log;
    private IConfigs cfg;
    private IEventManager evMngr;
    private ILightControl light;

    private String modName;

    public LightController(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.evMngr = (IEventManager) dep[2];
        this.light = (ILightControl) dep[3];
    }

    public boolean start() {
        if (cfg.getModCfg("light")) {
            if (!startLightModule())
                return false;
        }
        return true;
    }

    private boolean startLightModule() {
        LightSettings lightCfg = cfg.getLightCfg();

        /*
         * Add devices from cfg
         */
        for (LightDeviceSettings dev : lightCfg.getDevices()) {
            ILightDevice device = new LightDevice(dev.getName(), dev.getAlias(), dev.getGroup(), dev.getIp(), dev.getChannel(), dev.getSwitcher().getIp(), dev.getSwitcher().getChannel());
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
         * Add events
         */
        evMngr.addListener(Events.SYNC_EVENT, (EventListener) light);
        evMngr.addListener(Events.SWITCH_STATUS_EVENT, (EventListener) light);
        return true;
    }

    /*
     * Control functions
     */

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

    public String getModName() {
        return modName;
    }
}
