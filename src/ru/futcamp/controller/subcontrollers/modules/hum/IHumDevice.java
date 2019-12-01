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

package ru.futcamp.controller.subcontrollers.modules.hum;

public interface IHumDevice {
    String getName();
    void setName(String name);
    String getAlias();
    void setAlias(String alias);
    String getIp();
    void setIp(String ip);
    String getSensor();
    void setSensor(String sensor);
    void syncStates() throws Exception;
    boolean isStatus();
    void setStatus(boolean status);
    boolean isHeater();
    void setHeater(boolean heater);
    int getThreshold();
    void setThreshold(int threshold);
}
