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

package ru.futcamp.controller.subcontrollers.modules.light;

import ru.futcamp.controller.subcontrollers.Events;

import java.util.List;

public interface ILightControl {
    void loadStates() throws Exception;
    void addDevice(ILightDevice device);
    void switchStatus(String alias) throws Exception;
    List<LightInfo> getLightInfo();
    LightInfo getLightInfo(String alias) throws Exception;
    List<LightInfo> getLightGroupInfo(String group);
    void setGroupStatus(String group, boolean status) throws Exception;
    void setLightStatus(String alias, boolean status) throws Exception;
    void genEvent(String ip, int channel, Events event);
}
