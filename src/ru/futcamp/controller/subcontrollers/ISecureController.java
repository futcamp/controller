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

import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.TimeMgmt;
import ru.futcamp.controller.subcontrollers.modules.secure.MIHInfo;
import ru.futcamp.controller.subcontrollers.modules.secure.SecureInfo;

public interface ISecureController {
    boolean start();

    void switchMIHStatus() throws Exception;
    MIHInfo getMIHInfo();
    void changeMIHTime(TimeMgmt time, ActMgmt action) throws Exception;

    SecureInfo getSecureInfo();
    void switchSecureStatus() throws Exception;

    void getVisionPhoto(String alias, String fileName) throws Exception;
    void getVisionPhoto(String alias, String fileName, boolean isLight) throws Exception;
}
