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

import ru.futcamp.controller.modules.meteo.db.MeteoDBData;

import java.util.List;

public interface IMeteoStation {
    void addDevice(IMeteoDevice device);
    IMeteoDevice getDevice(String name);
    List<IMeteoDevice> getDevices();
    void setDBFileName(String fileName);
    void saveMeteoData(IMeteoDevice device) throws Exception;
    int getLastHour(String sensor) throws Exception;
    List<MeteoDBData> getDataByDate(String sensor, String date) throws Exception;
}
