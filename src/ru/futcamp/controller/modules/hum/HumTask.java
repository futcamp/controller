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

package ru.futcamp.controller.modules.hum;

import ru.futcamp.IAppModule;
import ru.futcamp.utils.configs.IConfigs;

import java.util.TimerTask;

/**
 * Therm task
 */
public class HumTask extends TimerTask implements IAppModule {
    private IHumControl hum;
    private IConfigs cfg;

    private int counter = 0;
    private String modName;

    public HumTask(String name, IAppModule ...dep) {
        this.modName = name;
        this.hum = (IHumControl) dep[0];
        this.cfg = (IConfigs) dep[1];
    }

    @Override
    public void run() {
        counter++;

        if (counter != cfg.getHumCfg().getInterval())
            return;
        counter = 0;

        hum.getUpdate();
    }

    public String getModName() {
        return modName;
    }
}
