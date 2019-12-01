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

package ru.futcamp.controller.subcontrollers.modules.meteo;

import ru.futcamp.controller.subcontrollers.modules.meteo.mod.MeteoModuleData;
import ru.futcamp.controller.subcontrollers.modules.meteo.mod.MeteoModule;

/**
 * Meteo station sensor
 */
public class MeteoDevice extends MeteoData implements IMeteoDevice {
    private String name;
    private String alias;
    private String type;
    private String address;
    private int channel;
    private int delta;

    public MeteoDevice(String name, String alias, String type, String addr, int chan, int delta) {
        this.name = name;
        this.alias = alias;
        this.type = type;
        this.address = addr;
        this.channel = chan;
        this.delta = delta;
    }

    /**
     * Syncing data with meteo device
     * @throws Exception If fail to sync data
     */
    public void syncMeteoData() throws Exception {
        MeteoModuleData data = MeteoModule.getMeteoData(this.address, this.type, this.channel);
        setTemp(data.getTemp());
        setHumidity(data.getHum());
        setPressure(data.getPres());
    }

    @Override
    public int getTemp() {
        return super.getTemp() + delta;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
