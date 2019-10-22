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

package ru.futcamp.controller.modules.therm;

import sun.awt.Mutex;

public class ThermData {
    private boolean status;
    private boolean heater;
    private int threshold;
    private Mutex mtxStat = new Mutex();
    private Mutex mtxHeat = new Mutex();
    private Mutex mtxThresh = new Mutex();

    public boolean isStatus() {
        boolean stat;

        mtxStat.lock();
        stat = status;
        mtxStat.unlock();

        return stat;
    }

    public void setStatus(boolean status) {
        mtxStat.lock();
        this.status = status;
        mtxStat.unlock();
    }

    public boolean isHeater() {
        boolean heat;

        mtxHeat.lock();
        heat = heater;
        mtxHeat.unlock();

        return heat;
    }

    public void setHeater(boolean heater) {
        mtxHeat.lock();
        this.heater = heater;
        mtxHeat.unlock();
    }

    public int getThreshold() {
        int th;

        mtxThresh.lock();
        th = threshold;
        mtxThresh.unlock();

        return th;
    }

    public void setThreshold(int threshold) {
        mtxThresh.lock();
        this.threshold = threshold;
        mtxThresh.unlock();
    }
}
