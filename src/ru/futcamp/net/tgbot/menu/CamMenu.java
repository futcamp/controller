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

package ru.futcamp.net.tgbot.menu;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.modules.light.ILightDevice;
import ru.futcamp.net.web.HttpClient;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.TelegramCamGroupSettings;
import ru.futcamp.utils.configs.settings.TelegramCamSettings;
import ru.futcamp.utils.log.ILogger;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class CamMenu implements IMenu {
    private IConfigs cfg;
    private IController ctrl;
    private ILogger log;

    public CamMenu(IConfigs cfg, IController ctrl, ILogger log) {
        this.cfg = cfg;
        this.ctrl = ctrl;
        this.log = log;
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
            if (savePhotoToFile(upd.getMessage().getText(), menu)) {
                SendPhoto ph = new SendPhoto().setChatId(upd.getMessage().getChatId());
                ph.setPhoto(new File("/tmp/photo.jpg"));
                bot.execute(ph);
            } else {
                msg.setText("Ошибка получения фото");
            }
            return;
        }

        msg.setText("Список камер");
        setButtons(msg, menu);
        bot.execute(msg);
    }

    /**
     * Get photo from cam device and save to file
     * @param camName Camera name
     * @return Status
     */
    private boolean savePhotoToFile(String camName, IBotMenu menu) {
        for (TelegramCamGroupSettings camGroup : cfg.getTelegramCfg().getCamgroups()) {
            for (TelegramCamSettings cam : camGroup.getCams()) {
                if (camName.equals(cam.getName())) {
                    /*
                     * Switch on lamps
                     */
                    if (menu.isLight()) {
                        switchLamps(cam.getLamps(), true);
                    }

                    /*
                     * GetPhoto
                     */
                    HttpClient client = new HttpClient("http://" + cam.getIp() + "/camera?dev=" + cam.getChannel());
                    try {
                        client.saveImage("/tmp/photo.jpg");
                    } catch (Exception e) {
                        return false;
                    }

                    /*
                     * Switch off lamps
                     */
                    if (menu.isLight()) {
                        switchLamps(cam.getLamps(), false);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Switch lamp states before and after snapshot
     * @param lamps List of lamps
     * @param status Status of lamp
     */
    private void switchLamps(String[] lamps, boolean status) {
        for (String lamp : lamps) {
            ILightDevice lampDev = ctrl.getLightDevice(lamp);
            lampDev.setStatus(status);
            try {
                lampDev.syncStates();
            } catch (Exception e) {
                log.error("Fail to sync lamp status for photo: " + e.getMessage(), "CAMMENU");
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
        for (TelegramCamGroupSettings camGroup : cfg.getTelegramCfg().getCamgroups()) {
            List<String> cams = new LinkedList<>();

            for (TelegramCamSettings cam : camGroup.getCams()) {
                cams.add(cam.getName());
            }

            addButtonsRow(cams, keyboard);
        }

        /*
         * Add back button to menu
         */
        List<String> backButton = new LinkedList<>();
        if (menu.isLight())
            backButton.add("Без света");
        else
            backButton.add("Подсветка");
        backButton.add("Назад");
        addButtonsRow(backButton, keyboard);

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
}
