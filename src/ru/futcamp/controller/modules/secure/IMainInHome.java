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

public interface IMainInHome {
    void setStatus(boolean status);
    boolean isStatus();
    void setLamp(boolean lamp);
    boolean isLamp();
    void setRadio(boolean radio);
    boolean isRadio();
    void saveData() throws Exception;
    void loadDataFromDb() throws Exception;
    void setDBFileName(String fileName);
    void setTimeOn(int timeOn);
    int getTimeOn();
    void setTimeOff(int timeOff);
    int getTimeOff();
    void syncStates() throws Exception;
    String getIp();
    void setIp(String ip);
}
