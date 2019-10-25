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

public class MIHDBData {
    private boolean status;
    private int timeOn;
    private int timeOff;

    public MIHDBData() { }

    public MIHDBData(boolean status, int timeOn, int timeOff) {
        this.status = status;
        this.timeOn = timeOn;
        this.timeOff = timeOff;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTimeOn() {
        return timeOn;
    }

    public void setTimeOn(int timeOn) {
        this.timeOn = timeOn;
    }

    public int getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(int timeOff) {
        this.timeOff = timeOff;
    }
}
