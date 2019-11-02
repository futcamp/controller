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

import java.util.List;

public interface ILightControl {
    void saveState(ILightDevice device) throws Exception;
    void loadStates() throws Exception;
    List<ILightDevice> getDevicesGroup(String group);
    List<ILightDevice> getDevices();
    ILightDevice getDevice(String name);
    ILightDevice getDeviceByAlias(String alias);
    void addDevice(ILightDevice device);
    void setDBFileName(String fileName);
}
