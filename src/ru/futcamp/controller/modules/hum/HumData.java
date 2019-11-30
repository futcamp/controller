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

package ru.futcamp.controller.modules.hum;

public class HumData {
    private boolean status;
    private boolean heater;
    private int threshold;

    public synchronized boolean isStatus() {
        return status;
    }

    public synchronized void setStatus(boolean status) {
        this.status = status;
    }

    public synchronized boolean isHeater() {
        return heater;
    }

    public synchronized void setHeater(boolean heater) {
        this.heater = heater;
    }

    public synchronized int getThreshold() {
        return threshold;
    }

    public synchronized void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
