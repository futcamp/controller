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

package ru.futcamp.tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.futcamp.IAppModule;
import ru.futcamp.tgbot.menu.BotMenu;
import ru.futcamp.tgbot.menu.IBotMenu;
import ru.futcamp.tgbot.menu.IMenu;
import ru.futcamp.tgbot.menu.LevelMenu;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.HashMap;
import java.util.Map;

import static ru.futcamp.tgbot.menu.LevelMenu.*;

/**
 * Telegram bot class
 */
public class TelegramBot extends TelegramLongPollingBot implements ITelegramBot, IAppModule {
    private IConfigs cfg;
    private ILogger log;
    private Map<LevelMenu, IMenu> menus = new HashMap<>();

    private String key;
    private String login;
    private Map<String, IBotMenu> level = new HashMap<>();

    private String modName;

    TelegramBot(String name, IAppModule... dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.cfg = (IConfigs) dep[1];
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
     * Add new menu item
     * @param level Menu level
     * @param menu Menu jbject
     */
    public void addMenu(LevelMenu level, IMenu menu) {
        this.menus.put(level, menu);
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

        log.info("New telegram message \"" + msg.getText() + "\" from user \"" + msg.getFrom().getUserName() +
                        "\" chat \"" + msg.getChatId() + "\"", "TGBOT");
        changeLevel(msg.getText(), user);

        try {
            IBotMenu menu = level.get(user);
            menus.get(menu.getLevel()).updateMessage(this, upd, menu);
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
            if (msg.equals("Охрана"))  {
                level.get(user).setLevel(SECURE_MENU);
                return;
            }
            if (msg.equals("Обогрев"))  {
                level.get(user).setLevel(THERM_MENU);
                return;
            }
            if (msg.equals("Свет"))  {
                level.get(user).setLevel(LIGHT_MENU);
                return;
            }
            if (msg.equals("Влажность"))  {
                level.get(user).setLevel(HUM_MENU);
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
         * Light menu
         */
        if (level.get(user).getLevel().equals(LIGHT_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(MAIN_MENU);
                return;
            }
            if (!msg.equals("Выбор освещения")) {
                level.get(user).setLevel(LIGHT_CTRL_MENU);
                level.get(user).setGroup(msg);
                return;
            }
        }
        if (level.get(user).getLevel().equals(LIGHT_CTRL_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(LIGHT_MENU);
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
            if (msg.equals("Человек в Доме")) {
                level.get(user).setLevel(MIH_MENU);
                return;
            }
        }
        if (level.get(user).getLevel().equals(MIH_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(SECURE_MENU);
                return;
            }
        }

        /*
         * Therm Control level
         */
        if (level.get(user).getLevel().equals(THERM_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(MAIN_MENU);
                return;
            }
            if (!msg.equals("Обновить")) {
                level.get(user).setLevel(THERM_CTRL_MENU);
                return;
            }
        }
        if (level.get(user).getLevel().equals(THERM_CTRL_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(THERM_MENU);
                return;
            }
        }

        /*
         * Humidity Control level
         */
        if (level.get(user).getLevel().equals(HUM_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(MAIN_MENU);
                return;
            }
            if (!msg.equals("Обновить")) {
                level.get(user).setLevel(HUM_CTRL_MENU);
                return;
            }
        }
        if (level.get(user).getLevel().equals(HUM_CTRL_MENU)) {
            if (msg.equals("Назад")) {
                level.get(user).setLevel(HUM_MENU);
                return;
            }
        }
    }

    public String getModName() {
        return modName;
    }
}
