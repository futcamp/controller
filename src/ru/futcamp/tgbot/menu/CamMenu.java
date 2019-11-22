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

package ru.futcamp.tgbot.menu;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.futcamp.IAppModule;
import ru.futcamp.controller.IController;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.vision.TelegramCamSettings;
import ru.futcamp.utils.log.ILogger;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class CamMenu implements IMenu, IAppModule {
    private IConfigs cfg;
    private IController ctrl;
    private ILogger log;

    private String modName;

    public CamMenu(String name, IAppModule ...dep) {
        modName = name;
        this.cfg = (IConfigs) dep[0];
        this.ctrl = (IController) dep[1];
        this.log = (ILogger) dep[2];
    }

    /**
     * Update telegram message
     * @param bot Telegram bot
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        String inMsg = upd.getMessage().getText();

        if (inMsg.equals("Подсветка")) {
            menu.setLight(true);
        } else if (inMsg.equals("Без света")) {
            menu.setLight(false);
        } else if (!inMsg.equals("Список камер") && !inMsg.equals("Камеры")) {
            try {
                if (cfg.getModCfg("light")) {
                    ctrl.getVisionPhoto(inMsg, "/tmp/photo.jpg", menu.isLight());
                } else {
                    ctrl.getVisionPhoto(inMsg, "/tmp/photo.jpg");
                }
                SendPhoto ph = new SendPhoto().setChatId(upd.getMessage().getChatId());
                ph.setPhoto(new File("/tmp/photo.jpg"));
                bot.execute(ph);
            } catch (Exception e) {
                log.error("Fail to get photo: " + e.getMessage(), "CAMMENU");
                SendMessage errMsg = new SendMessage().setChatId(upd.getMessage().getChatId());
                errMsg.setText("Ошибка получения фото!");
                bot.execute(errMsg);
            }
            return;
        }

        msg.setText("Список камер");
        setButtons(msg, menu);
        bot.execute(msg);
    }

    /**
     * Switch lamp states before and after snapshot
     * @param lamps List of lamps
     * @param status Status of lamp
     */
    private void switchLamps(String[] lamps, boolean status) {
        for (String lamp : lamps) {
            try {
                ctrl.setLightStatus(lamp, status);
            } catch (Exception e) {
                log.error("Fail to set lamp status for photo: " + e.getMessage(), "CAMMENU");
            }
        }
    }

    /**
     * Set buttons for response
     * @param sendMessage Sending message
     */
    private void setButtons(SendMessage sendMessage, IBotMenu menu) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();

        /*
         * Add buttons to menu
         */
        for (String[] row : cfg.getTelegramCfg().getMenu().getVision().getList()) {
            List<String> camGroup = new LinkedList<>();

            for (String btn : row) {
                if (btn.equals("%light%")) {
                    if (menu.isLight()) {
                        camGroup.add("Без света");
                    } else {
                        camGroup.add("Подсветка");
                    }
                } else {
                    camGroup.add(btn);
                }
            }

            addButtonsRow(camGroup, keyboard);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    /**
     * Add row with text buttons
     * @param buttons Text buttons
     * @param keyboard Keyboard row
     */
    private void addButtonsRow(List<String> buttons, List<KeyboardRow> keyboard) {
        KeyboardRow row = new KeyboardRow();

        for (String button : buttons) {
            row.add(new KeyboardButton(button));
        }

        keyboard.add(row);
    }

    @Override
    public String getModName() {
        return modName;
    }
}
