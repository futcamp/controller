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

public class MeteoInfo {
    private String name;
    private int temp;
    private int hum;
    private int pres;
    private String alias;
    private int hour;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public int getPres() {
        return pres;
    }

    public void setPres(int pres) {
        this.pres = pres;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
