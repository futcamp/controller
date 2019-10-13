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
import ru.futcamp.utils.configs.settings.HttpSettings;
import ru.futcamp.utils.configs.settings.SettingsType;
import ru.futcamp.utils.configs.settings.TelegramSettings;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Configs implements IConfigs {
    private TelegramSettings tgCfg;
    private HttpSettings httpCfg;

    /**
     * Reading and parsing configs
     * @param fileName Path to configs file
     * @throws Exception Reading configs file exception
     */
    public void readFromFile(String fileName, SettingsType set) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get(fileName)));

        switch (set) {
            case TGBOT_SET:
                tgCfg = JSON.parseObject(data, TelegramSettings.class);
                break;

            case HTTP_SET:
                httpCfg = JSON.parseObject(data, HttpSettings.class);
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
}
