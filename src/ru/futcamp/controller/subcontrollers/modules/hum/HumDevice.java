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

package ru.futcamp.controller.subcontrollers.modules.hum;

/**
 * Therm control device
 */
public class HumDevice extends HumData implements IHumDevice {
    private String name;
    private String alias;
    private String socket;
    private String sensor;

    public HumDevice(String name, String alias, String socket, String sensor) {
        this.name = name;
        this.socket = socket;
        this.alias = alias;
        this.sensor = sensor;
    }

    @Override
    public void setStatus(boolean status) {
        super.setStatus(status);
    }

    @Override
    public void setThreshold(int threshold) {
        super.setThreshold(threshold);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
}
