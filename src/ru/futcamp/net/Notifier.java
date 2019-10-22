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

package ru.futcamp.net;

import ru.futcamp.net.web.HttpClient;
import ru.futcamp.utils.configs.IConfigs;

import java.net.URLEncoder;

/**
 * Telegram notifier
 */
public class Notifier implements INotifier {
    private IConfigs cfg;

    public Notifier(IConfigs cfg) {
        this.cfg = cfg;
    }

    /**
     * Send telegram notify to user
     * @param message Sending message
     * @throws Exception If fail to send notify
     */
    public void sendNotify(String message) throws Exception {
        for (String chatID : cfg.getTelegramCfg().getChats()) {
            HttpClient client = new HttpClient("https://api.telegram.org/bot" + cfg.getTelegramCfg().getKey() +
                                                "/sendMessage?chat_id=" + chatID + "&text=" +
                                                URLEncoder.encode(message, "UTF-8"));
            client.getRequest(2000);
        }
    }
}
