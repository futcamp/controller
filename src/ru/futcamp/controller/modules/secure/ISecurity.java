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

package ru.futcamp.controller.modules.secure;

import java.util.List;

public interface ISecurity {
    boolean isStatus();
    void setStatus(boolean status);
    boolean isAlarm();
    void setAlarm(boolean alarm);
    void saveStates() throws Exception;
    List<ISecureDevice> getDevices();
    void addDevice(ISecureDevice device);
    void setDBFileName(String fileName);
    void loadStates() throws Exception;
    void setDeviceState(String ip, int channel, boolean state);
}
