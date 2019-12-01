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

import ru.futcamp.controller.subcontrollers.modules.light.LightInfo;

import java.util.List;

public interface ILightController {
    boolean start();

    void switchLightStatus(String alias) throws Exception;
    void setGroupStatus(String group, boolean status) throws Exception;
    void setLightStatus(String alias, boolean status) throws Exception;
    LightInfo getLightInfo(String alias) throws Exception;
    List<LightInfo> getLightInfo();
    List<LightInfo> getLightGroupInfo(String group);
}
