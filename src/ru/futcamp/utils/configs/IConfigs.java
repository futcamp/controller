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

import ru.futcamp.utils.configs.settings.*;
import ru.futcamp.utils.configs.settings.hum.HumSettings;
import ru.futcamp.utils.configs.settings.light.LightSettings;
import ru.futcamp.utils.configs.settings.meteo.MeteoSettings;
import ru.futcamp.utils.configs.settings.monitor.MonitorSettings;
import ru.futcamp.utils.configs.settings.secure.SecureSettings;
import ru.futcamp.utils.configs.settings.socket.SocketSettings;
import ru.futcamp.utils.configs.settings.therm.ThermSettings;
import ru.futcamp.utils.configs.settings.vision.VisionSettings;

public interface IConfigs {
    void readFromFile(String fileName, SettingsType set) throws Exception;
    TelegramSettings getTelegramCfg();
    HttpSettings getHttpCfg();
    MeteoSettings getMeteoCfg();
    SecureSettings getSecureCfg();
    boolean getModCfg(String name);
    CtrlSettings getCtrlCfg();
    ThermSettings getThermCfg();
    LightSettings getLightCfg();
    VisionSettings getVisionCfg();
    MonitorSettings getMonitorCfg();
    HumSettings getHumCfg();
    SocketSettings getSockCfg();
}
