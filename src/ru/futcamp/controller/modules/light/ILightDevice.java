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

package ru.futcamp.controller.modules.light;

public interface ILightDevice {
    String getIp();
    void setIp(String ip);
    String getName();
    void setName(String name);
    String getAlias();
    void setAlias(String alias);
    int getChannel();
    void setChannel(int channel);
    String getGroup();
    void setGroup(String group);
    boolean isStatus();
    void setStatus(boolean status);
    void syncStates() throws Exception;
}
