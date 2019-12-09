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

package ru.futcamp.controller.subcontrollers;

import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.subcontrollers.modules.hum.HumInfo;
import ru.futcamp.controller.subcontrollers.modules.meteo.MeteoInfo;
import ru.futcamp.controller.subcontrollers.modules.therm.ThermInfo;

import java.util.List;

public interface IMeteoController {
    boolean start();

    List<MeteoInfo> getMeteoInfo();
    MeteoInfo getMeteoInfo(String alias) throws Exception;
    List<MeteoInfo> getMeteoInfoByDate(String alias, String date) throws Exception;

    List<ThermInfo> getThermInfo();
    ThermInfo getThermInfo(String alias) throws Exception;
    void switchThermStatus(String alias) throws Exception;
    void changeThermThreshold(String alias, ActMgmt action) throws Exception;
    void genThermEvent(String socket, Events event) throws Exception;

    void switchHumStatus(String alias) throws Exception;
    List<HumInfo> getHumInfo();
    HumInfo getHumInfo(String alias) throws Exception;
    void changeHumThreshold(String alias, ActMgmt action) throws Exception;
    void genHumEvent(String socket, Events event) throws Exception;
}
