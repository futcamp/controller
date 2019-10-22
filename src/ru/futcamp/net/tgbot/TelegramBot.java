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
import ru.futcamp.net.tgbot.menu.BotMenu;
import ru.futcamp.net.tgbot.menu.IBotMenu;
import ru.futcamp.net.tgbot.menu.IMenu;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.HashMap;
import java.util.Map;

import static ru.futcamp.net.tgbot.menu.LevelMenu.*;

/**
 * Telegram bot class
 */
public class TelegramBot extends TelegramLongPollingBot implements ITelegramBot {
    private IConfigs cfg;
    private ILogger log;
    private IMenu mainMenu;
    private IMenu meteoMenu;
    private IMenu camMenu;
    private IMenu secMenu;
    private IMenu meteoStatMenu;

    private String key;
    private String login;
    private Map<String, IBotMenu> level = new HashMap<>();

    public TelegramBot(ILogger log, IConfigs cfg, IMenu mainMenu, IMenu meteoMenu, IMenu camMenu,
                       IMenu secMenu, IMenu meteoStatMenu) {
        this.cfg = cfg;
        this.log = log;
        this.mainMenu = mainMenu;
        this.meteoMenu = meteoMenu;
        this.camMenu = camMenu;
        this.secMenu = secMenu;
        this.meteoStatMenu = meteoStatMenu;
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
        for (String user : cfg.getTelegramCfg().getUsers()) {
            this.level.put(user, new BotMenu());
        }
    }

    @Override
    public String getBotUsername() {
        return this.login;
    }

    @Override
    public void onUpdateReceived(Update upd) {
        Message msg = upd.getMessage();
        String user = msg.getFrom().getUserName();

        if (!checkUser(user)) {
            log.error("Bad username \""+ user + "\"", "TGBOT");
            return;
        }

        log.info("New telegram message \"" + msg.getText() + "\" from chat " + msg.getChatId(),
                "TGBOT");
        changeLevel(msg.getText(), user);

        try {
            IBotMenu menu = level.get(user);
            switch (menu.getLevel()) {
                case METEO_MENU:
                    meteoMenu.updateMessage(this, upd, menu);
                    break;

                case CAM_MENU:
                    camMenu.updateMessage(this, upd, menu);
                    break;

                case SECURE_MENU:
                    secMenu.updateMessage(this, upd, menu);
                    break;

                case METEO_STAT_MENU:
                    meteoStatMenu.updateMessage(this, upd, menu);
                    break;

                default:
                    mainMenu.updateMessage(this, upd, menu);
                    break;
            }
        } catch (Exception e) {
            log.error("Fail to process telegram bot message: " + e.getMessage(), "TGBOT");
        }
    }

    @Override
    public String getBotToken() {
        return this.key;
    }

    /**
     * Change menu level
     * @param msg Text message
     * @param user Telegram user
     */
    private void changeLevel(String msg, String user) {
        /*
         * Main level
         */
        if (level.get(user).getLevel().equals(MAIN_MENU)) {
            if (msg.equals("Метео"))  {
                level.get(user).setLevel(METEO_MENU);
                return;
            }
            if (msg.equals("Камеры"))  {
                level.get(user).setLevel(CAM_MENU);
                return;
            }
            if (msg.equals("Сигналка"))  {
                level.get(user).setLevel(SECURE_MENU);
                return;
            }
        }

        /*
         * Meteo level
         */
        if (level.get(user).getLevel().equals(METEO_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(MAIN_MENU);
                return;
            }
            if (msg.equals("Статистика")) {
                level.get(user).setLevel(METEO_STAT_MENU);
                return;
            }
        }

        if (level.get(user).getLevel().equals(METEO_STAT_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(METEO_MENU);
                return;
            }
        }

        /*
         * Cams level
         */
        if (level.get(user).getLevel().equals(CAM_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(MAIN_MENU);
                return;
            }
        }

        /*
         * Secure level
         */
        if (level.get(user).getLevel().equals(SECURE_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(MAIN_MENU);
                return;
            }
        }
    }
}
