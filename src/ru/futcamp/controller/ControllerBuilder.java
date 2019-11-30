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
import ru.futcamp.IBuilder;
import ru.futcamp.controller.events.EventManager;
import ru.futcamp.controller.modules.hum.HumControl;
import ru.futcamp.controller.modules.hum.HumTask;
import ru.futcamp.controller.modules.light.LightControl;
import ru.futcamp.controller.modules.meteo.MeteoDisplay;
import ru.futcamp.controller.modules.meteo.MeteoStation;
import ru.futcamp.controller.modules.meteo.MeteoTask;
import ru.futcamp.controller.modules.monitor.Monitor;
import ru.futcamp.controller.modules.monitor.MonitorTask;
import ru.futcamp.controller.modules.secure.ManInHome;
import ru.futcamp.controller.modules.secure.SecureTask;
import ru.futcamp.controller.modules.secure.Security;
import ru.futcamp.controller.modules.therm.ThermControl;
import ru.futcamp.controller.modules.therm.ThermTask;
import ru.futcamp.controller.modules.vision.Vision;
import ru.futcamp.controller.modules.vision.VisionTask;

public class ControllerBuilder implements IBuilder {
    /**
     * Make new controller module
     * @param name Name of module
     * @return Module
     */
    public IAppModule makeModule(String name, IAppModule ...dep) {
        if (name.equals("evmngr")) {
            return new EventManager(name, dep);
        } else if (name.equals("meteolcd")) {
            return new MeteoDisplay(name, dep);
        } else if (name.equals("meteo")) {
            return new MeteoStation(name, dep);
        } else if (name.equals("meteotsk")) {
            return new MeteoTask(name, dep);
        } else if (name.equals("therm")) {
            return new ThermControl(name, dep);
        } else if (name.equals("thermtsk")) {
            return new ThermTask(name, dep);
        } else if (name.equals("hum")) {
            return new HumControl(name, dep);
        } else if (name.equals("humtsk")) {
            return new HumTask(name, dep);
        } else if (name.equals("secure")) {
            return new Security(name, dep);
        } else if (name.equals("mih")) {
            return new ManInHome(name, dep);
        } else if (name.equals("securetsk")) {
            return new SecureTask(name, dep);
        } else if (name.equals("light")) {
            return new LightControl(name, dep);
        } else if (name.equals("ctrl")) {
            return new Controller(name, dep);
        } else if (name.equals("vision")) {
            return new Vision(name, dep);
        } else if (name.equals("vistask")) {
            return new VisionTask(name, dep);
        } else if (name.equals("monitor")) {
            return new Monitor(name, dep);
        } else if (name.equals("montsk")) {
            return new MonitorTask(name, dep);
        }
        return null;
    }
}
