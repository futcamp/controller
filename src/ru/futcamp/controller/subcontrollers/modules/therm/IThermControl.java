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

package ru.futcamp.controller.subcontrollers.modules.therm;

import ru.futcamp.controller.ActMgmt;

import java.util.List;

public interface IThermControl {
    List<ThermInfo> getThermInfo();
    ThermInfo getThermInfo(String alias) throws Exception;
    void switchStatus(String alias) throws Exception;
    void changeThreshold(String alias, ActMgmt action) throws Exception;
    void loadStates() throws Exception;
    void addDevice(IThermDevice device);
    void getUpdate();
}
