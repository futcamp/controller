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

import ru.futcamp.controller.modules.secure.db.MIHDBData;
import sun.awt.Mutex;

public class MIHData {
    private boolean status;
    private boolean radio;
    private boolean lamp;
    private int timeOn;
    private int timeOff;
    private Mutex mtxStat = new Mutex();
    private Mutex mtxRadio = new Mutex();
    private Mutex mtxLamp = new Mutex();
    private Mutex mtxTmOn = new Mutex();
    private Mutex mtxTmOff = new Mutex();

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

    public boolean isRadio() {
        boolean rad;

        mtxRadio.lock();
        rad = radio;
        mtxRadio.unlock();

        return rad;
    }

    public void setRadio(boolean radio) {
        mtxRadio.lock();
        this.radio = radio;
        mtxRadio.unlock();
    }

    public boolean isLamp() {
        boolean l;

        mtxLamp.lock();
        l = lamp;
        mtxLamp.unlock();

        return l;
    }

    public void setLamp(boolean lamp) {
        mtxLamp.lock();
        this.lamp = lamp;
        mtxLamp.unlock();
    }

    public int getTimeOff() {
        int tm;

        mtxTmOff.lock();
        tm = timeOff;
        mtxTmOff.unlock();

        return tm;
    }

    public void setTimeOff(int timeOff) {
        mtxTmOff.lock();
        this.timeOff = timeOff;
        mtxTmOff.unlock();
    }

    public int getTimeOn() {
        int tm;

        mtxTmOn.lock();
        tm = timeOn;
        mtxTmOn.unlock();

        return tm;
    }

    public void setTimeOn(int timeOn) {
        mtxTmOn.lock();
        this.timeOn = timeOn;
        mtxTmOn.unlock();
    }
}
