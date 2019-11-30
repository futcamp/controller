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

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.futcamp.IAppModule;
import ru.futcamp.tgbot.ITelegramBot;
import ru.futcamp.tgbot.menu.BotMenu;
import ru.futcamp.tgbot.menu.IBotMenu;
import ru.futcamp.tgbot.menu.IMenu;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.HashMap;
import java.util.Map;

import static ru.futcamp.tgbot.menu.LevelMenu.*;

/**
 * Telegram bot class
 */
public class TelegramClient extends TelegramLongPollingBot {
    private String key;
    private String user;
    private String chat;

    TelegramClient(String key, String chat, String user) {
        this.key = key;
        this.user = user;
        this.chat = chat;
    }

    /**
     * Sending text message to bot
     * @param module Application module
     * @param message Sendding message
     * @throws Exception If fail to send message
     */
    void sendTextMessage(String module, String message) throws Exception {
        SendMessage msg = new SendMessage().setChatId(chat);
        msg.enableHtml(true);
        msg.setText("<b>" + module + "</b>\n" + message);
        this.execute(msg);
    }

    /**
     * Sending photo message
     * @param caption Caption of photo
     * @param fileName Path to photo file
     * @throws Exception If fail to send photo
     */
    void sendPhotoMessage(String caption, String fileName) throws Exception {
        SendPhoto msg = new SendPhoto().setChatId(chat);
        msg.setCaption(caption);
        msg.setPhoto(fileName);
        this.execute(msg);
    }

    @Override
    public String getBotUsername() {
        return this.user;
    }

    @Override
    public void onUpdateReceived(Update upd) {
    }

    @Override
    public String getBotToken() {
        return this.key;
    }
}
