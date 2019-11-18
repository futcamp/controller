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
import ru.futcamp.controller.modules.therm.IThermDevice;
import ru.futcamp.controller.modules.therm.ThermInfo;
import ru.futcamp.utils.configs.IConfigs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class ThermMenu implements IMenu, IAppModule {
    private IController ctrl;
    private IConfigs cfg;

    private String modName;

    public ThermMenu(String name, IAppModule ...dep) {
        modName = name;
        this.ctrl = (IController) dep[0];
        this.cfg = (IConfigs) dep[1];
    }

    /**
     * Update telegram message
     * @param bot Telegram bot
     * @param upd Update message
     * @param menu Telegram menu pointer
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        String txt = "Обогрев помещений\n\n";

        for (ThermInfo info : ctrl.getThermInfo()) {
            txt += "<b>" + info.getAlias() + "</b>\n";
            txt += "Статус: <b>" + (info.isStatus() ? "Работает" : "Отключен") + "</b>\n";
            txt += "Обогреватель: <b>" + (info.isHeater() ? "Работает" : "Отключен") + "</b>\n";
            txt += "Текущая температура: <b>" + ctrl.getMeteoInfo(info.getSensor()).getTemp() + "°</b>\n";
            txt += "Держать температуру: <b>" + info.getThreshold() + "°</b>\n\n";
        }

        msg.setText(txt);
        msg.enableHtml(true);
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

        for (String[] row : cfg.getTelegramCfg().getMenu().getTherm()) {
            List<String> thermGroup = new LinkedList<>();

            for (String btn : row) {
                thermGroup.add(btn);
            }

            addButtonsRow(thermGroup, keyboard);
        }

        /*
         * Add back button to menu
         */
        List<String> backButton = new LinkedList<>();
        backButton.add("Обновить");
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

    public String getModName() {
        return modName;
    }
}
