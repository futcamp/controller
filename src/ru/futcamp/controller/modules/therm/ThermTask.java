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

package ru.futcamp.controller.modules.therm;

import ru.futcamp.controller.modules.meteo.IMeteoStation;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.TimerTask;

/**
 * Therm task
 */
public class ThermTask extends TimerTask {
    private ILogger log;
    private IThermControl therm;
    private IMeteoStation meteo;
    private IConfigs cfg;

    private int counter = 0;

    public ThermTask(ILogger log, IThermControl therm, IMeteoStation meteo, IConfigs cfg) {
        this.log = log;
        this.therm = therm;
        this.meteo = meteo;
        this.cfg = cfg;
    }

    @Override
    public void run() {
        counter++;

        if (counter != cfg.getThermCfg().getInterval())
            return;
        counter = 0;

        for (IThermDevice device : therm.getDevices()) {
            if (device.isStatus()) {
                int curTemp = meteo.getDevice(device.getSensor()).getTemp();

                if (curTemp < device.getThreshold()) {
                    device.setHeater(true);
                }
                if (curTemp > device.getThreshold()) {
                    device.setHeater(false);
                }
            } else {
                device.setHeater(false);
            }

            /*
             * Syncing states with device
             */
            try {
                device.syncStates();
                break;
            } catch (Exception e) {
                log.error("Fail to sync therm device " + device.getName(), "THERMTASK");
            }
        }
    }
}
