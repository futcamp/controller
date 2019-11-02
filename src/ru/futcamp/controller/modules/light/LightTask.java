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

package ru.futcamp.controller.modules.light;

import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.TimerTask;

/**
 * Therm task
 */
public class LightTask extends TimerTask {
    private ILogger log;
    private IConfigs cfg;
    private ILightControl light;

    private int counter = 0;

    public LightTask(ILogger log, IConfigs cfg, ILightControl light) {
        this.log = log;
        this.light = light;
        this.cfg = cfg;
    }

    @Override
    public void run() {
        counter++;

        if (counter != cfg.getLightCfg().getInterval())
            return;
        counter = 0;

        for (ILightDevice device : light.getDevices()) {
            /*
             * Syncing states with device
             */
            try {
                device.syncStates();
            } catch (Exception e) {
                log.error("Fail to sync light device: " + device.getName(), "LIGHTTASK");
            }
        }
    }
}
