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

package ru.futcamp.controller.modules.secure.db;

import java.sql.SQLException;

public interface ISecureDB {
    void saveStatus(boolean status);
    void saveAlarm(boolean alarm);
    void saveMIHStatus(boolean status);
    void saveMIHTimeOn(int hour);
    void saveMIHTimeOff(int hour);
    Boolean getStatus();
    Boolean getAlarm();
    Boolean getMIHStatus();
    Integer getMIHTimeOn();
    Integer getMIHTimeOff();
    void close() throws SQLException;
}
