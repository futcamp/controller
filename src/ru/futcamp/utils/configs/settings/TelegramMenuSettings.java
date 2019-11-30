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

package ru.futcamp.utils.configs.settings;

import ru.futcamp.utils.configs.settings.hum.TelegramHumSettings;
import ru.futcamp.utils.configs.settings.light.TelegramLightSettings;
import ru.futcamp.utils.configs.settings.therm.TelegramThermSettings;
import ru.futcamp.utils.configs.settings.vision.TelegramCamSettings;

public class TelegramMenuSettings {
    private String[][] main;
    private String[][] meteo;
    private String[][] meteostat;
    private String[][] security;
    private String[][] mih;
    private TelegramThermSettings therm;
    private TelegramHumSettings hum;
    private TelegramLightSettings light;
    private TelegramCamSettings vision;

    public String[][] getMain() {
        return main;
    }

    public void setMain(String[][] main) {
        this.main = main;
    }

    public String[][] getMeteostat() {
        return meteostat;
    }

    public void setMeteostat(String[][] meteostat) {
        this.meteostat = meteostat;
    }

    public TelegramLightSettings getLight() {
        return light;
    }

    public void setLight(TelegramLightSettings light) {
        this.light = light;
    }

    public TelegramThermSettings getTherm() {
        return therm;
    }

    public void setTherm(TelegramThermSettings therm) {
        this.therm = therm;
    }

    public String[][] getMeteo() {
        return meteo;
    }

    public void setMeteo(String[][] meteo) {
        this.meteo = meteo;
    }

    public TelegramCamSettings getVision() {
        return vision;
    }

    public void setVision(TelegramCamSettings vision) {
        this.vision = vision;
    }

    public String[][] getSecurity() {
        return security;
    }

    public void setSecurity(String[][] security) {
        this.security = security;
    }

    public String[][] getMih() {
        return mih;
    }

    public void setMih(String[][] mih) {
        this.mih = mih;
    }

    public TelegramHumSettings getHum() {
        return hum;
    }

    public void setHum(TelegramHumSettings hum) {
        this.hum = hum;
    }
}
