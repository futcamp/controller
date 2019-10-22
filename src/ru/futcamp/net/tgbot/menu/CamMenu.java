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
import ru.futcamp.controller.modules.meteo.IMeteoDevice;
import ru.futcamp.net.web.HttpClient;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.TelegramCamGroupSettings;
import ru.futcamp.utils.configs.settings.TelegramCamSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class CamMenu implements IMenu {
    private IConfigs cfg;

    public CamMenu(IConfigs cfg) {
        this.cfg = cfg;
    }

    /**
     * Update telegram message
     * @param bot Telegram bot
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());

        if (!upd.getMessage().getText().equals("Список камер") &&
                !upd.getMessage().getText().equals("Камеры")) {
            if (savePhotoToFile(upd.getMessage().getText())) {
                SendPhoto ph = new SendPhoto().setChatId(upd.getMessage().getChatId());
                ph.setPhoto(new File("/tmp/photo.jpg"));
                bot.execute(ph);
            } else {
                msg.setText("Ошибка получения фото");
            }
            return;
        }

        msg.setText("Список камер");
        setButtons(msg);
        bot.execute(msg);
    }

    /**
     * Get photo from cam device and save to file
     * @param camName Camera name
     * @return Status
     */
    private boolean savePhotoToFile(String camName) {
        for (TelegramCamGroupSettings camGroup : cfg.getTelegramCfg().getCamgroups()) {
            for (TelegramCamSettings cam : camGroup.getCams()) {
                if (camName.equals(cam.getName())) {
                    HttpClient client = new HttpClient("http://" + cam.getIp() + "/camera?dev=" + cam.getChannel());
                    try {
                        client.saveImage("/tmp/photo.jpg");
                    } catch (Exception e) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Set buttons for response
     * @param sendMessage Sending message
     */
    private void setButtons(SendMessage sendMessage) {
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
