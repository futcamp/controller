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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.futcamp.IAppModule;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.subcontrollers.modules.meteo.MeteoInfo;
import ru.futcamp.utils.configs.IConfigs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Meteo menu of tg bot
 */
public class MeteoMenu implements IMenu, IAppModule {
    private IController ctrl;
    private IConfigs cfg;

    private String modName;

    public MeteoMenu(String name, IAppModule ...dep) {
        modName = name;
        this.ctrl = (IController) dep[0];
        this.cfg = (IConfigs) dep[1];
    }

    /**
     * Update telegram message
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        StringBuilder txt = new StringBuilder();
        int pressure = 0;
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());

        List<MeteoInfo> infoList = ctrl.getMeteo().getMeteoInfo();
        txt.append("Метео данные\n\n");
        for (MeteoInfo info : infoList) {
            txt.append("<b>").append(info.getAlias()).append("</b>\nТемпература: <b>").append(info.getTemp()).append("°</b> ");
            if (info.getHum() != 0) {
                txt.append("Влажность: <b>").append(info.getHum()).append("%</b>\n\n");
            } else {
                txt.append("\n\n");
            }
            if (info.getPres() != 0) {
                pressure = info.getPres();
            }
        }
        if (pressure != 0) {
            txt.append("Атм. давление: <b>").append(pressure).append("mm</b>");
        }

        msg.enableHtml(true);
        msg.setText(txt.toString());
        setButtons(msg);

        bot.execute(msg);
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

        for (String[] row : cfg.getTelegramCfg().getMenu().getMeteo()) {
            List<String> meteoList = new LinkedList<>();
            Collections.addAll(meteoList, row);
            addButtonsRow(meteoList, keyboard);
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

    public String getModName() {
        return modName;
    }
}
