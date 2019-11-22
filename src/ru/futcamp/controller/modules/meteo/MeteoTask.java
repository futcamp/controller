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

import ru.futcamp.IAppModule;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.meteo.LcdDeviceSettings;
import ru.futcamp.utils.configs.settings.meteo.LcdSettings;
import ru.futcamp.utils.configs.settings.meteo.MeteoSettings;
import ru.futcamp.utils.log.ILogger;

import java.util.TimerTask;

/**
 * Meteo task
 */
public class MeteoTask extends TimerTask implements IAppModule {
    private ILogger log;
    private IMeteoStation meteo;
    private IConfigs cfg;
    private IMeteoDisplay lcd;

    private int meteoCounter = 0;
    private int lcdCounter = 0;

    private static int MaxSensorRetries = 3;

    private String modName;

    public MeteoTask(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
        this.meteo = (IMeteoStation) dep[1];
        this.cfg = (IConfigs) dep[2];
        this.lcd = (IMeteoDisplay) dep[3];
    }

    @Override
    public void run() {
        processMeteo();
        processDisplay();
    }

    private void processDisplay() {
        MeteoSettings mCfg = cfg.getMeteoCfg();
        lcdCounter++;

        if (lcdCounter != mCfg.getTimers().getLcd())
            return;
        lcdCounter = 0;

        for (LcdSettings display : mCfg.getDisplays()) {
            for (LcdDeviceSettings device : display.getSensors()) {
                try {
                    int value = 0;

                    if (device.getType().equals("temp")) {
                        value = meteo.getMeteoInfo(device.getSensor()).getTemp();
                    } else if (device.getType().equals("hum")) {
                        value = meteo.getMeteoInfo(device.getSensor()).getHum();
                    }

                    lcd.updateData(display.getIp(), device.getId(), value, device.getType());
                } catch (Exception e) {
                    log.error("Fail to update meteo data on lcd \"" + display.getName() + "\" sensor \""+device.getSensor()+"\": " + e.getMessage(), "METEOTASK");
                }
            }
            try {
                lcd.showData(display.getIp());
            } catch (Exception e) {
                log.error("Fail to display meteo data on \"" + display.getName() + "\": " + e.getMessage(), "METEOTASK");
            }
        }
    }

    private void processMeteo() {
        MeteoSettings mCfg = cfg.getMeteoCfg();
        meteoCounter++;

        if (meteoCounter == mCfg.getTimers().getSensors()) {
            meteoCounter = 0;
            meteo.update();
        }
    }

    public String getModName() {
        return modName;
    }
}
