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
import ru.futcamp.controller.modules.meteo.mod.MeteoModule;

/**
 * Displaying meteo data on LCD
 */
public class MeteoDisplay implements IMeteoDisplay, IAppModule {
    private String modName;

    public MeteoDisplay(String name, IAppModule ...dep) {
        this.modName = name;
    }

    /**
     * Update meteo data on lcd
     * @param ip Address of device
     * @param id Number of sensor
     * @param value Meteo value of sensor
     * @param type Sensor type
     * @throws Exception If fail to update data
     */
    public void updateData(String ip, int id, int value, String type) throws Exception {
        MeteoModule.updateMeteoLcd(ip, id, value, type);
    }

    /**
     * Show meteo data on lcd
     * @param ip Address of device
     * @throws Exception If fail to show data
     */
    public void showData(String ip) throws Exception {
        MeteoModule.displayMeteoLcd(ip);
    }

    public String getModName() {
        return modName;
    }
}
