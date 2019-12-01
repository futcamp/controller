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

package ru.futcamp.controller.subcontrollers.modules.vision;

import ru.futcamp.IAppModule;
import ru.futcamp.utils.configs.IConfigs;

import java.util.TimerTask;

/**
 * Therm task
 */
public class VisionTask extends TimerTask implements IAppModule {
    private IVision vision;
    private IConfigs cfg;

    private int counter = 0;
    private int warmCnt = 0;
    private String modName;

    public VisionTask(String name, IAppModule ...dep) {
        this.modName = name;
        this.vision = (IVision) dep[0];
        this.cfg = (IConfigs) dep[1];
    }

    @Override
    public void run() {
        processWarming();
        processCams();
    }

    private void processWarming() {
        warmCnt++;

        if (warmCnt != cfg.getVisionCfg().getTimers().getWarming())
            return;
        warmCnt = 0;

        vision.warmCameras();
    }

    private void processCams() {
        counter++;

        if (counter != cfg.getVisionCfg().getTimers().getMain())
            return;
        counter = 0;
    }

    public String getModName() {
        return modName;
    }
}
