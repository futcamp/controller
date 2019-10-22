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

public interface IMeteoDevice {
    String getName();
    void setName(String name);
    String getAlias();
    void setAlias(String alias);
    String getType();
    void setType(String type);
    String getAddress();
    void setAddress(String address);
    int getChannel();
    void setChannel(int channel);
    void syncMeteoData() throws Exception;
    int getHumidity();
    int getPressure();
    int getTemp();
    boolean isFail();
    void setFail(boolean fail);
}
