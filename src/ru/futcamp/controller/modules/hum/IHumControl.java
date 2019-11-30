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

package ru.futcamp.controller.modules.hum;

import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.events.Events;

import java.util.List;

public interface IHumControl {
    List<HumInfo> getHumInfo();
    HumInfo getHumInfo(String alias) throws Exception;
    void switchStatus(String alias) throws Exception;
    void changeThreshold(String alias, ActMgmt action) throws Exception;
    void loadStates() throws Exception;
    void addDevice(IHumDevice device);
    void getUpdate();
}
