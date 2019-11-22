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

package ru.futcamp.utils.configs;

import com.alibaba.fastjson.JSON;
import ru.futcamp.IAppModule;
import ru.futcamp.utils.configs.settings.*;
import ru.futcamp.utils.configs.settings.light.LightSettings;
import ru.futcamp.utils.configs.settings.meteo.MeteoSettings;
import ru.futcamp.utils.configs.settings.secure.SecureSettings;
import ru.futcamp.utils.configs.settings.therm.ThermSettings;
import ru.futcamp.utils.configs.settings.vision.VisionSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Configs implements IConfigs, IAppModule {
    private TelegramSettings tgCfg;
    private HttpSettings httpCfg;
    private MeteoSettings meteoCfg;
    private SecureSettings secureCfg;
    private CtrlSettings ctrlCfg;
    private ThermSettings thermCfg;
    private LightSettings lightCfg;
    private VisionSettings visionCfg;

    private String modName;

    public Configs(String name, IAppModule ...dep) {
        this.modName = name;
    }

    /**
     * Reading and parsing configs
     * @param fileName Path to configs file
     * @throws Exception Reading configs file exception
     */
    public void readFromFile(String fileName, SettingsType set) throws Exception {
        String str = "";
        String data = "";
        File fileDir = new File(fileName);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                                new FileInputStream(fileDir), "UTF8"));

        while ((str = in.readLine()) != null) {
            data += str;
        }
        in.close();

        switch (set) {
            case TGBOT_SET:
                tgCfg = JSON.parseObject(data, TelegramSettings.class);
                break;

            case HTTP_SET:
                httpCfg = JSON.parseObject(data, HttpSettings.class);
                break;

            case METEO_SET:
                meteoCfg = JSON.parseObject(data, MeteoSettings.class);
                break;

            case SECURE_SET:
                secureCfg = JSON.parseObject(data, SecureSettings.class);
                break;

            case CTRL_SET:
                ctrlCfg = JSON.parseObject(data, CtrlSettings.class);
                break;

            case THERM_SET:
                thermCfg = JSON.parseObject(data, ThermSettings.class);
                break;

            case LIGHT_SET:
                lightCfg = JSON.parseObject(data, LightSettings.class);
                break;

            case VISION_SET:
                visionCfg = JSON.parseObject(data, VisionSettings.class);
                break;
        }
    }

    /**
     * Get telegram bot configs
     * @return Telegram configs
     */
    public TelegramSettings getTelegramCfg() {
        return this.tgCfg;
    }

    /**
     * Get http settings
     * @return Http server configs
     */
    public HttpSettings getHttpCfg() { return this.httpCfg; }

    /**
     * Get meteo settings
     * @return Meteo settings
     */
    public MeteoSettings getMeteoCfg() {
        return meteoCfg;
    }

    /**
     * Get security settings
     * @return Security settings
     */
    public SecureSettings getSecureCfg() {
        return secureCfg;
    }

    /**
     * Get modules enable settings
     * @param name Name of module
     * @return State
     */
    public boolean getModCfg(String name) {
        for (ModSettings mod : ctrlCfg.getModules()) {
            if (mod.getName().equals(name))
                return mod.isEnable();
        }
        return false;
    }

    /**
     * Get controller settings
     * @return Controller settings
     */
    public CtrlSettings getCtrlCfg() {
        return ctrlCfg;
    }

    /**
     * Get therm control configs
     * @return Therm control configs
     */
    public ThermSettings getThermCfg() {
        return thermCfg;
    }

    /**
     * Get light configs
     * @return Light control configs
     */
    public LightSettings getLightCfg() {
        return lightCfg;
    }

    public String getModName() {
        return modName;
    }

    /**
     * Get vision configs
     * @return Vision configs
     */
    public VisionSettings getVisionCfg() {
        return visionCfg;
    }
}
