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

package ru.futcamp.controller.modules.secure;

import ru.futcamp.IAppModule;
import ru.futcamp.utils.configs.IConfigs;

import java.util.TimerTask;

public class SecureTask extends TimerTask implements Runnable, IAppModule {
    private IMainInHome mih;
    private IConfigs cfg;
    private ISecurity secure;

    private String modName;
    private int counter = 0;

    public SecureTask(String name, IAppModule ...dep) {
        this.modName = name;
        this.mih = (IMainInHome) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.secure = (ISecurity) dep[2];
    }

    @Override
    public void run() {
        counter++;

        if (counter != cfg.getSecureCfg().getInterval())
            return;
        counter = 0;

        mih.update();
        secure.update();
    }

    public String getModName() {
        return modName;
    }
}
