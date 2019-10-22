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

package ru.futcamp.controller.modules.meteo;

import sun.awt.Mutex;

public class MeteoData {
    private int temp;
    private int humidity;
    private int pressure;
    private Mutex mtx = new Mutex();

    public int getTemp() {
        int t;

        mtx.lock();
        t = temp;
        mtx.unlock();

        return t;
    }

    public void setTemp(int temp) {
        mtx.lock();
        this.temp = temp;
        mtx.unlock();
    }

    public int getHumidity() {
        int h;

        mtx.lock();
        h = humidity;
        mtx.unlock();

        return h;
    }

    public void setHumidity(int humidity) {
        mtx.lock();
        this.humidity = humidity;
        mtx.unlock();
    }

    public int getPressure() {
        int p;

        mtx.lock();
        p = pressure;
        mtx.unlock();

        return p;
    }

    public void setPressure(int pressure) {
        mtx.lock();
        this.pressure = pressure;
        mtx.unlock();
    }
}
