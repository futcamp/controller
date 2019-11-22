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

public class MeteoData {
    private int temp;
    private int humidity;
    private int pressure;

    public synchronized int getTemp() {
        return temp;
    }

    public synchronized void setTemp(int temp) {
        this.temp = temp;
    }

    public synchronized int getHumidity() {
        return humidity;
    }

    public synchronized void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public synchronized int getPressure() {
        return pressure;
    }

    public synchronized void setPressure(int pressure) {
        this.pressure = pressure;
    }
}
