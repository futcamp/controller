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
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.events.IEventManager;
import ru.futcamp.controller.subcontrollers.ILightController;
import ru.futcamp.controller.subcontrollers.IMeteoController;
import ru.futcamp.controller.subcontrollers.IMiscController;
import ru.futcamp.controller.subcontrollers.ISecureController;

/**
 * Smart home controller
 */
public class Controller implements IController, IAppModule {
    private IEventManager evMngr;
    private IMeteoController meteo;
    private ISecureController secure;
    private ILightController light;
    private IMiscController misc;

    private String modName;

    public Controller(String name, IAppModule ...dep) {
        modName = name;
        this.evMngr = (IEventManager) dep[0];
        this.meteo = (IMeteoController) dep[1];
        this.secure = (ISecureController) dep[2];
        this.light = (ILightController) dep[3];
        this.misc = (IMiscController) dep[4];
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

    /**
     * Generate new event
     * @param ev Event type
     * @param mod Module
     * @param ip Address of device
     * @param chan Device channel
     * @throws Exception If fail to generate event
     */
    public void genEvent(Events ev, String mod, String ip, int chan) throws Exception {
        evMngr.genEvent(ev, mod, ip, chan);
    }

    public String getModName() {
        return modName;
    }
}
