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

import ru.futcamp.controller.modules.light.LightInfo;
import ru.futcamp.controller.modules.meteo.MeteoInfo;
import ru.futcamp.controller.modules.secure.MIHInfo;
import ru.futcamp.controller.modules.secure.SecureInfo;
import ru.futcamp.controller.modules.therm.ThermInfo;

import java.util.List;

public interface IController {
    boolean startModules();

    List<MeteoInfo> getMeteoInfo();
    MeteoInfo getMeteoInfo(String alias);
    List<MeteoInfo> getMeteoInfoByDate(String alias, String date) throws Exception;

    List<ThermInfo> getThermInfo();
    ThermInfo getThermInfo(String alias) throws Exception;
    void switchThermStatus(String alias) throws Exception;
    void changeThermThreshold(String alias, ActMgmt action) throws Exception;

    void switchMIHStatus() throws Exception;
    MIHInfo getMIHInfo();
    void changeMIHTime(TimeMgmt time, ActMgmt action) throws Exception;
    SecureInfo getSecureInfo();
    void switchSecureStatus() throws Exception;
    void newSecureAction(String ip, int chan);

    void switchLightStatus(String alias) throws Exception;
    LightInfo getLightInfo(String alias) throws Exception;
    List<LightInfo> getLightInfo();
    List<LightInfo> getLightGroupInfo(String group);
    void setGroupStatus(String group, boolean status) throws Exception;
    void setLightStatus(String alias, boolean status) throws Exception;
}
