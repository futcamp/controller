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

package ru.futcamp.net.notifier;

import ru.futcamp.IAppModule;
import ru.futcamp.net.web.HttpClient;
import ru.futcamp.tgbot.TelegramBot;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.TelegramSettings;

import java.net.URLEncoder;

/**
 * Telegram notifier
 */
public class Notifier implements INotifier, IAppModule {
    private IConfigs cfg;

    private String modName;

    public Notifier(String name, IAppModule ...dep) {
        this.modName = name;
        this.cfg = (IConfigs) dep[0];
    }

    /**
     * Send telegram notify to user
     * @param module Application module
     * @param message Sending message
     * @throws Exception If fail to send notify
     */
    public void sendNotify(String module, String message) throws Exception {
        TelegramSettings set = cfg.getTelegramCfg();

        for (String chatID : set.getChats()) {
            TelegramClient client = new TelegramClient(set.getKey(), chatID, set.getLogin());
            client.sendTextMessage(module, message);
        }
    }

    /**
     * Send photo notify
     * @param caption Caption of photo
     * @param fileName Path to photo file
     * @throws Exception If fail to send photo
     */
    public void sendNotifyPhoto(String caption, String fileName) throws Exception {
        TelegramSettings set = cfg.getTelegramCfg();

        for (String chatID : set.getChats()) {
            TelegramClient client = new TelegramClient(set.getKey(), chatID, set.getLogin());
            client.sendPhotoMessage(caption, fileName);
        }
    }

    public String getModName() {
        return modName;
    }
}
