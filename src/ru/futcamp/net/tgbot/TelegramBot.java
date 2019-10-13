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

package ru.futcamp.net.tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

/**
 * Telegram bot class
 */
public class TelegramBot extends TelegramLongPollingBot implements ITelegramBot {
    private IConfigs cfg;
    private ILogger log;

    private String key;
    private String login;

    public TelegramBot(ILogger log, IConfigs cfg) {
        this.cfg = cfg;
        this.log = log;
    }

    /**
     * Check user login
     * @param login Incoming login
     * @return Exists status
     */
    private boolean checkUser(String login) {
        for (String user : cfg.getTelegramCfg().getUsers()) {
            if (user.equals(login)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set bot data
     * @param key Bot api key
     * @param login Bot username
     */
    public void setData(String key, String login) {
        this.key = key;
        this.login = login;
    }

    @Override
    public String getBotUsername() {
        return this.login;
    }

    @Override
    public void onUpdateReceived(Update e) {
        Message msg = e.getMessage();
        String user = msg.getFrom().getUserName();

        if (!checkUser(user)) {
            log.error("Bad username \""+ user + "\"", "TGBOT");
            return;
        }else {
            log.info(msg.getText(), "");
        }
    }

    @Override
    public String getBotToken() {
        return this.key;
    }
}
