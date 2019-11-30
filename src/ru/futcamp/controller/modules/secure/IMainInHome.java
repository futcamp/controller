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

import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.TimeMgmt;

public interface IMainInHome {
    void loadDataFromDb() throws Exception;
    void syncStates() throws Exception;
    void switchStatus() throws Exception;
    void changeTime(TimeMgmt time, ActMgmt action) throws Exception;
    MIHInfo getMIHInfo();
    void getUpdate();
}
