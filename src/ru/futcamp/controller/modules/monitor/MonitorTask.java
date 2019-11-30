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

package ru.futcamp.controller.modules.monitor;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.utils.configs.IConfigs;

import java.util.TimerTask;

/**
 * Therm task
 */
public class MonitorTask extends TimerTask implements IAppModule {
    private IMonitor monitor;
    private IConfigs cfg;

    private int counter = 0;
    private String modName;

    public MonitorTask(String name, IAppModule ...dep) {
        this.modName = name;
        this.monitor = (IMonitor) dep[0];
        this.cfg = (IConfigs) dep[1];
    }

    @Override
    public void run() {
        counter++;

        if (counter != cfg.getMonitorCfg().getInterval())
            return;
        counter = 0;

        monitor.checkStatus();
    }

    public String getModName() {
        return modName;
    }
}
