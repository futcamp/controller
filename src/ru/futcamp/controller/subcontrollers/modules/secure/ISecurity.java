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

package ru.futcamp.controller.subcontrollers.modules.secure;

import ru.futcamp.controller.subcontrollers.Events;

public interface ISecurity {
    void addDevice(ISecureDevice device);
    void loadStates() throws Exception;
    void switchStatus() throws Exception;
    SecureInfo getSecureInfo();
    void genEvent(String ip, int channel, Events event);
}
