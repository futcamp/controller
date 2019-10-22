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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.modules.therm.IThermDevice;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.TelegramThermSettings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ru.futcamp.net.tgbot.menu.LevelMenu.*;

/**
 * Cams menu of tg bot
 */
public class ThermMenu implements IMenu {
    private IController ctrl;
    private IConfigs cfg;

    public ThermMenu(IController ctrl, IConfigs cfg) {
        this.ctrl = ctrl;
        this.cfg = cfg;
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

        for (IThermDevice device : ctrl.getThermDevices()) {
            txt += "<b>" + device.getAlias() + "</b>\n";
            txt += "Статус: <b>" + (device.isStatus() ? "Работает" : "Отключен") + "</b>\n";
            txt += "Обогреватель: <b>" + (device.isHeater() ? "Работает" : "Отключен") + "</b>\n";
            txt += "Держать температуру: <b>" + device.getThreshold() + "°</b>\n\n";
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

        for (TelegramThermSettings therm : cfg.getTelegramCfg().getTherm()) {
            List<String> thermGroup = new LinkedList<>();

            for (String device : therm.getDevices()) {
                thermGroup.add(device);
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
}
