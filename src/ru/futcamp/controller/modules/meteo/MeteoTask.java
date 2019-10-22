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

package ru.futcamp.controller.modules.meteo;

import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.MeteoSettings;
import ru.futcamp.utils.log.ILogger;

import java.util.TimerTask;

/**
 * Meteo task
 */
public class MeteoTask extends TimerTask {
    private ILogger log;
    private IMeteoStation meteo;
    private IConfigs cfg;

    private int dbCounter = 0;
    private int meteoCounter;

    private static int MaxSensorRetries = 3;

    public MeteoTask(ILogger log, IMeteoStation meteo, IConfigs cfg) {
        this.log = log;
        this.meteo = meteo;
        this.cfg = cfg;
    }

    @Override
    public void run() {
        MeteoSettings mCfg = cfg.getMeteoCfg();
        dbCounter++;
        meteoCounter++;

        if (meteoCounter == mCfg.getInterval()) {
            meteoCounter = 0;
            for (IMeteoDevice device : meteo.getDevices()) {
                for (int i = 0; i < MaxSensorRetries; i++) {
                    try {
                        device.syncMeteoData();

                        if (device.isFail()) {
                            device.setFail(false);
                            log.info("Meteo sensor " + device.getName() + " is online", "METEOTASK");
                        }

                        try {
                            saveToDb();
                        } catch (Exception e) {
                            log.error("Fail to save meteo data to db: " + e.getMessage(), "METEOTASK");
                        }
                        break;
                    } catch (Exception e) {
                        if (!device.isFail() && (i == (MaxSensorRetries - 1))) {
                            log.error("Fail to sync meteo data with " + device.getName() + " : " + e.getMessage(),
                                    "METEOTASK");
                            device.setFail(true);
                        }
                        continue;
                    }
                }
            }
        }
    }

    /**
     * Save meteo data from sensor to db
     * @throws Exception
     */
    private void saveToDb() throws Exception {
        if (dbCounter != cfg.getMeteoCfg().getDbInterval())
            return;

        dbCounter = 0;

        for (IMeteoDevice device : meteo.getDevices()) {
            int lastHour = meteo.getLastHour(device.getAlias());
            int curHour = TimeControl.getCurHour();

            if (curHour != lastHour) {
                meteo.saveMeteoData(device);
            }
        }
    }
}
