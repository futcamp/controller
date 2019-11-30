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

public interface ISecureDevice {
    void syncSecureAlarm(boolean state) throws Exception;
    String getName();
    String getAlias();
    String getIp();
    int getChannel();
    String getGroup();
    String getType();
    boolean isStatus();
    void setStatus(boolean statue);
    String getCamera();
    boolean isWatch();
}
