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

import ru.futcamp.controller.modules.light.ILightDevice;
import ru.futcamp.controller.modules.meteo.IMeteoDevice;
import ru.futcamp.controller.modules.meteo.db.MeteoDBData;
import ru.futcamp.controller.modules.secure.ISecureDevice;
import ru.futcamp.controller.modules.therm.IThermDevice;

import java.util.List;

public interface IController {
    void startModules();
    List<IMeteoDevice> getMeteoDevices();
    IMeteoDevice getMeteoDevice(String name);
    void setSecureState(String ip, int channel, boolean state);
    void setSecureStatus(boolean status);
    boolean isSecureStatus();
    boolean isSecureAlarm();
    List<ISecureDevice> getSecureDevices();
    List<MeteoDBData> getMeteoDataByDate(String sensor, String date);
    void saveSecureStates();
    List<IThermDevice> getThermDevices();
    IThermDevice getThermDeviceByAlias(String alias);
    void saveThermState(IThermDevice device) throws Exception;
    void saveMIHStates();
    void setMIHStatus(boolean status);
    void setMIHRadio(boolean status);
    void setMIHLamp(boolean status);
    void setMIHTimeOn(boolean status);
    void setMIHTimeOn(int time);
    void setMIHTimeOff(int time);
    boolean isMIHStatus();
    boolean isMIHRadio();
    boolean isMIHLamp();
    int getMIHTimeOn();
    int getMIHTimeOff();
    ILightDevice getLightDeviceByAlias(String alias);
    ILightDevice getLightDevice(String name);
    List<ILightDevice> getLightDevices();
    void saveLightState(ILightDevice device);
    List<ILightDevice> getLightDevicesGroup(String group);
}
