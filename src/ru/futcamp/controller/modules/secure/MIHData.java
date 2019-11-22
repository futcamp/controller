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

public class MIHData {
    private boolean status;
    private boolean radio;
    private boolean lamp;
    private int timeOn;
    private int timeOff;

    public synchronized boolean isStatus() {
        return status;
    }

    public synchronized void setStatus(boolean status) {
        this.status = status;
    }

    public synchronized boolean isRadio() {
        return radio;
    }

    public synchronized void setRadio(boolean radio) {
        this.radio = radio;
    }

    public synchronized boolean isLamp() {
        return lamp;
    }

    public synchronized void setLamp(boolean lamp) {
        this.lamp = lamp;
    }

    public synchronized int getTimeOn() {
        return timeOn;
    }

    public synchronized void setTimeOn(int timeOn) {
        this.timeOn = timeOn;
    }

    public synchronized int getTimeOff() {
        return timeOff;
    }

    public synchronized void setTimeOff(int timeOff) {
        this.timeOff = timeOff;
    }
}
