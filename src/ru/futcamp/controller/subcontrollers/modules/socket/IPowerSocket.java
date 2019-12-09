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

package ru.futcamp.controller.subcontrollers.modules.socket;

import ru.futcamp.controller.subcontrollers.Events;

import java.util.List;

public interface IPowerSocket {
    List<SocketInfo> getSocketInfo();
    SocketInfo getSocketInfo(String alias) throws Exception;
    SocketInfo getSocketInfo(String ip, int channel) throws Exception;
    void setStatus(String alias, boolean dev, boolean status);
    void addDevice(ISocketDevice device);
    void genEvent(String ip, int channel, Events event);
}
