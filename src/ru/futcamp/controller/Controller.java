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

package ru.futcamp.controller;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.subcontrollers.Events;
import ru.futcamp.controller.subcontrollers.ILightController;
import ru.futcamp.controller.subcontrollers.IMeteoController;
import ru.futcamp.controller.subcontrollers.IMiscController;
import ru.futcamp.controller.subcontrollers.ISecureController;

/**
 * Smart home controller
 */
public class Controller implements IController, IAppModule {
    private IMeteoController meteo;
    private ISecureController secure;
    private ILightController light;
    private IMiscController misc;

    private String modName;

    public Controller(String name, IAppModule ...dep) {
        modName = name;
        this.meteo = (IMeteoController) dep[0];
        this.secure = (ISecureController) dep[1];
        this.light = (ILightController) dep[2];
        this.misc = (IMiscController) dep[3];
    }

    /**
     * Start meteo modules
     */
    public boolean startAll() {
        if (!meteo.start()) {
            return false;
        }
        if (!secure.start()) {
            return false;
        }
        if (!light.start()) {
            return false;
        }
        if (!misc.start()) {
            return false;
        }
        return true;
    }

    public IMeteoController getMeteo() {
        return this.meteo;
    }

    public ISecureController getSecure() {
        return this.secure;
    }

    public ILightController getLight() {
        return this.light;
    }

    public IMiscController getMisc() {
        return this.misc;
    }

    public String getModName() {
        return modName;
    }
}
